package com.example.examplemod.entity;

import com.example.examplemod.effect.ModEffects;
import com.example.examplemod.item.ModItems;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class ThrowingDaggerEntity extends ThrowableItemProjectile {
    private static final float DAMAGE = 6.0f;
    private int rotationTicks = 0;
    private static final float PENETRATE_THRESHOLD = 0.9F; // 穿透所需最小速度
    private static final float BOUNCE_THRESHOLD = 0.7F;    // 反弹所需最小速度
    private static final float DAMAGE_REDUCTION = 0.7F;    // 穿透/反弹后的伤害衰减

    public ThrowingDaggerEntity(EntityType<? extends ThrowingDaggerEntity> type, Level level) {
        super(type, level);
    }

    public ThrowingDaggerEntity(Level level, LivingEntity shooter) {
        super(ModEntities.THROWING_DAGGER.get(), shooter, level);
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.IRON_DAGGER.get();
    }

    @Override
    public void tick() {
        super.tick();
        this.rotationTicks++;

        if (this.isRemoved()){return;}

        Vec3 startPos = this.position();
        Vec3 endPos = startPos.add(this.getDeltaMovement());

        // 1. 先检测实体碰撞
        HitResult entityHit = ProjectileUtil.getEntityHitResult(
                this.level(),
                this,
                startPos,
                endPos,
                this.getBoundingBox().expandTowards(this.getDeltaMovement()).inflate(1.0),
                entity -> !entity.isSpectator() && entity.isPickable() && entity != this.getOwner()
        );

        if (entityHit != null) {
            this.onHit(entityHit); // 会触发onHitEntity
            return;
        }

        // 2. 再检测方块碰撞
        BlockHitResult blockHit = this.level().clip(
                new ClipContext(startPos, endPos, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this)
        );


        if (blockHit.getType() != HitResult.Type.MISS || this.tickCount > 100) {
            // 添加创造模式检查
            if (!(this.getOwner() instanceof Player p) || !p.getAbilities().instabuild) {
                ItemStack droppedStack = this.getItem().copy();
                droppedStack.hurt(1, this.random, null);

                if (droppedStack.getDamageValue() < droppedStack.getMaxDamage()) {
                    this.spawnAtLocation(droppedStack);
                    this.playSound(SoundEvents.CHERRY_WOOD_HIT, 0.8F, 1.5F);
                } else {
                    this.playSound(SoundEvents.ITEM_BREAK, 0.8F, 1.5F);
                }
            }
            this.playSound(SoundEvents.ARROW_HIT, 1.0F, 1.2F);
            this.discard();
        }

        // 更新位置
        this.setPos(endPos.x, endPos.y, endPos.z);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        Vec3 currentVelocity = this.getDeltaMovement();
        float speed = (float) currentVelocity.length(); // 获取当前速度标量

        if (speed > PENETRATE_THRESHOLD) {
            // 穿透逻辑
            handlePenetration(result.getEntity(), currentVelocity);
        } else if (speed > BOUNCE_THRESHOLD) {
            // 反弹逻辑
            handleBounce(result.getEntity(), currentVelocity);
        } else {
            // 速度不足则嵌入
            handleEmbed(result.getEntity());
        }
    }

    private void handlePenetration(Entity target, Vec3 velocity) {
        // 造成伤害（根据速度衰减）
        target.hurt(this.damageSources().thrown(this, this.getOwner()),
                DAMAGE * DAMAGE_REDUCTION);

        // 保持70%速度继续飞行
        this.setDeltaMovement(velocity.scale(0.7));

        // 播放穿透音效
        this.playSound(SoundEvents.ARROW_HIT, 1.0F, 1.2F);
    }

    private void handleBounce(Entity target, Vec3 velocity) {
        float speed = (float) velocity.length(); // 添加这行获取速度

        // 计算反弹方向（简单反射向量）
        Vec3 newVelocity = velocity.subtract(
                target.position().subtract(this.position()).normalize().scale(2 * velocity.dot(target.position().subtract(this.position()).normalize()))
        );

        // 造成衰减伤害
        target.hurt(this.damageSources().thrown(this, this.getOwner()),
                DAMAGE * 0.5F);

        // 设置反弹后速度（保留60%原速度）
        this.setDeltaMovement(newVelocity.normalize().scale(speed * 0.6F));
        this.setYRot(this.getYRot() + 180); // 翻转模型

        // 播放金属碰撞音效
        this.playSound(SoundEvents.SHIELD_BLOCK, 0.8F, 1.5F);
    }

    private void handleEmbed(Entity target) {
        // 固定在生物身上
        if (target instanceof LivingEntity living) {
            living.addEffect(new MobEffectInstance(
                    ModEffects.BLEEDING.get(),
                    60, // 1秒减速效果
                    5
            ));
        }
        if (!(this.getOwner() instanceof Player p) || !p.getAbilities().instabuild) {
            ItemStack droppedStack = this.getItem().copy();
            droppedStack.hurt(5, this.random, null);

            if (droppedStack.getDamageValue() < droppedStack.getMaxDamage()) {
                this.spawnAtLocation(droppedStack);
                this.playSound(SoundEvents.CHERRY_WOOD_HIT, 0.8F, 1.5F);
            } else {
                this.playSound(SoundEvents.ITEM_BREAK, 0.8F, 1.5F);
            }
        }
        this.discard();
    }


    @Override
    protected float getGravity() {
        return 0.05F; // 原版箭是0.05F，雪球是0.03F，越小飞得越平
    }


}