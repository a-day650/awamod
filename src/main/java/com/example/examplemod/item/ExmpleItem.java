package com.example.examplemod.item;

import com.example.examplemod.effect.ModEffects;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class ExmpleItem extends Item {
    public ExmpleItem(Properties properties) {  // 修正参数名
        super(properties);
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 8; //
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK; // 明确指定使用动画为"吃"
    }

    @Override
    public SoundEvent getEatingSound() {
        return SoundEvents.HONEY_DRINK;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        // 先调用父类逻辑处理食物效果等
        ItemStack result = super.finishUsingItem(stack, level, entity);

        // 在服务端播放自定义声音
        if (!level.isClientSide && entity instanceof Player player && entity.isAlive()) {
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED,20,1));
            player.playSound(SoundEvents.HONEY_BLOCK_BREAK);
            float newHealth = Math.min(entity.getHealth() + 6.0F, entity.getMaxHealth());
            if (player.getHealth() > 0.0F){entity.setHealth(newHealth);}
            if (player.hasEffect(ModEffects.BLEEDING.get())){
                MobEffectInstance bleeding = player.getEffect(ModEffects.BLEEDING.get());
                int amplifier = bleeding.getAmplifier();
                int duration = bleeding.getDuration();
                player.removeEffect(ModEffects.BLEEDING.get());
                player.addEffect(new MobEffectInstance(ModEffects.BLEEDING.get(),duration/3,Math.max(0, amplifier - 2)));
            }
            player.getCooldowns().addCooldown(this, 15);
        }

        return result;
    }
}