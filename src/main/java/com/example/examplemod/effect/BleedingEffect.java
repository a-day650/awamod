package com.example.examplemod.effect;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import org.joml.Vector3f;

public class BleedingEffect extends MobEffect {
    public BleedingEffect() {
        super(MobEffectCategory.HARMFUL, 0x8B0000);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        // 服务端只处理伤害逻辑
        if (!entity.level().isClientSide()) {
            handleBleedingDamage(entity, amplifier);
            return;
        }
        // 客户端处理粒子效果（每5ticks一次）
        Float size = amplifier/2.5F;//小黑子999
        spawnBloodParticles(entity, amplifier, size);
    }


    private void spawnBloodParticles(LivingEntity entity, int amplifier, Float size) {
        // 确保在客户端执行
        if (!(entity.level() instanceof ClientLevel level)) return;

        RandomSource random = level.random;
        DustParticleOptions options = new DustParticleOptions(
                new Vector3f(0.8f, 0.0f, 0.0f), // 暗红色
                size // 粒子大小
        );

        // 根据效果等级增加粒子数量
        for (int i = 0; i < 3 + amplifier/4; i++) {
            double x = entity.getX() + (random.nextDouble() - 0.5) * entity.getBbWidth();
            double y = entity.getY() + random.nextDouble() * entity.getBbHeight();
            double z = entity.getZ() + (random.nextDouble() - 0.5) * entity.getBbWidth();

            level.addParticle(
                    options, // 使用创建好的粒子选项
                    x, y, z,
                    (random.nextDouble() - 0.5) * 0.02,
                    random.nextDouble() * 0.02,
                    (random.nextDouble() - 0.5) * 0.02
            );
        }
    }

    private void handleBleedingDamage(LivingEntity entity, int amplifier) {
        // 确保实体存活且世界存在
        if (!entity.isAlive() || entity.level() == null) return;

        // 计算伤害间隔（等级越高频率越快）
        int interval = Math.max(5, 100 / (amplifier + 1));  // 最小5ticks间隔

        if (entity.tickCount % interval == 0) {
            // 计算基础伤害（0.5-3.5颗心）
            float finalDamage = 1.0f + amplifier * 0.5f;

            // 应用伤害
            DamageSource damageSource = new DamageSource(
                    entity.level().registryAccess()
                            .registryOrThrow(Registries.DAMAGE_TYPE)
                            .getHolderOrThrow(ResourceKey.create(
                                    Registries.DAMAGE_TYPE,
                                    new ResourceLocation("awamod", "bleed_damage")
                            ))
            );

            entity.hurt(damageSource, finalDamage);

            // 播放音效（同步到客户端）
            if (entity instanceof ServerPlayer serverPlayer) {
                serverPlayer.connection.send(new ClientboundSoundPacket(
                        BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.GENERIC_HURT),
                        SoundSource.NEUTRAL,
                        entity.getX(), entity.getY(), entity.getZ(),
                        1.0f, 1.0f, entity.level().getRandom().nextLong()
                ));
            }
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }
}