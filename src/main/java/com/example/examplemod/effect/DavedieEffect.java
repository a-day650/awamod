package com.example.examplemod.effect;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class DavedieEffect extends MobEffect {
    public DavedieEffect() {
        super(MobEffectCategory.HARMFUL, 0x0A9C0B);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        // 每2ticks执行一次
        return duration % 2 == 0;
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (!entity.level().isClientSide() && entity instanceof ServerPlayer player) {
            // 发送消息
            player.sendSystemMessage(
                    Component.literal("§c味真足！！！！！！！！！！！！！！！！！")
                            .withStyle(ChatFormatting.RED)
            );

            // 执行破坏逻辑
            daveBreak(player, amplifier);
        }
    }

    private void daveBreak(ServerPlayer player, int amplifier) {
        Level level = player.level();
        BlockPos belowPos = player.blockPosition().below();

        // 破坏范围计算 (amplifier+1确保至少有1格范围)
        int radius = Math.min(amplifier + 1, 3);

        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                BlockPos targetPos = belowPos.offset(x, 0, z);
                if (canBreakBlock(level, targetPos)) {
                    level.destroyBlock(targetPos, true, player);
                }
            }
        }
    }

    private boolean canBreakBlock(Level level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        return !state.isAir() &&
                !state.is(Blocks.BEDROCK) &&
                state.getDestroySpeed(level, pos) >= 0;
    }
}