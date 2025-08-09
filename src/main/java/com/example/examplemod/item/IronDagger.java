package com.example.examplemod.item;

import com.example.examplemod.entity.ThrowingDaggerEntity;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.Level;

public class IronDagger extends SwordItem {
    public IronDagger() {super(Tiers.IRON, 3, -1.8f, new Properties().durability(250));}

    @Override
    public boolean isDamageable(ItemStack stack) {
        return true; // 启用耐久系统
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        player.playSound(SoundEvents.ARROW_SHOOT, 1.0F, 1.2F);

        if (!level.isClientSide) {
            // 创建投掷匕首实体
            boolean isCreative = player.getAbilities().instabuild;

            ThrowingDaggerEntity dagger = new ThrowingDaggerEntity(level, player);
            dagger.setItem(isCreative ? ItemStack.EMPTY : stack.copy()); // 创造模式不复制物品

            dagger.shootFromRotation(
                    player,
                    player.getXRot(),
                    player.getYRot(),
                    0.5F,
                    1.5F,
                    1.0F
            );
            level.addFreshEntity(dagger);

            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
        }
        if (!player.getAbilities().instabuild) {
            player.getCooldowns().addCooldown(this, 15);
        }
        return InteractionResultHolder.success(stack);
    }
}