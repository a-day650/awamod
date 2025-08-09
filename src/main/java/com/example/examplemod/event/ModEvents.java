package com.example.examplemod.event;

import com.example.examplemod.effect.ModEffects;
import com.example.examplemod.thirst.ThirstManager;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ModEvents {
    private static final float LOW_HEALTH_THRESHOLD = 1f; // 100%血量触发


    public static void register() {
        MinecraftForge.EVENT_BUS.addListener(ModEvents::onLivingHurt);
        MinecraftForge.EVENT_BUS.addListener(ModEvents::onPlayerTick);
        MinecraftForge.EVENT_BUS.addListener(ModEvents::onLivingHeal);
    }

    // 受伤时施加流血效果
    public static void onLivingHurt(LivingHurtEvent event) {
        DamageSource source = event.getSource();
        if (source.is(DamageTypes.MOB_ATTACK) ||
                source.is(DamageTypes.PLAYER_ATTACK) ||
                source.is(DamageTypes.ARROW) ||
                source.is(DamageTypes.FALL) ||
                source.is(DamageTypes.EXPLOSION)) {

            LivingEntity target = event.getEntity();
            float damage = event.getAmount();

            // 计算流血时长和等级
            int bleedDuration = 10 + (int) (damage * 2) * 20;
            int bleedAmplifier = Math.min((int) damage, 3); // 限制最大等级为3

            target.addEffect(new MobEffectInstance(
                    ModEffects.BLEEDING.get(),
                    bleedDuration,
                    bleedAmplifier,
                    true, // 禁用原版粒子
                    true   // 显示图标
            ));
        }
    }

    // 低血量检测（每秒钟检测一次）
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (event.player.tickCount % 600 == 0) {
            ThirstManager.subtractThirst(event.player, 1);
        }
        Player player = event.player;
        if (player.tickCount % 20 != 0){ // 每秒检测一次
            float healthRatios = player.getHealth() / player.getMaxHealth();
            if (healthRatios < LOW_HEALTH_THRESHOLD-0.7f) {
                player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 20, 0));
            }
            if (healthRatios < LOW_HEALTH_THRESHOLD-0.6f) {
                player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 20*10, 0));
            }
        }
    }

    // 治疗时清除部分负面效果
    @SubscribeEvent
    public static void onLivingHeal(LivingHealEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity.getHealth() / entity.getMaxHealth() > 0.5f) {
            entity.removeEffect(MobEffects.WEAKNESS);
        }
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (!event.getEntity().level().isClientSide) {
            ThirstManager.setThirst(event.getEntity(), 20); // 复活时重置为满值
        }
    }
}