package com.example.examplemod.item;

import com.example.examplemod.effect.ModEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;

public class ModFoodProperties {
    public static final FoodProperties EXAMPLE_FOOD = new FoodProperties.Builder()
            .nutrition(2)  // 恢复的饥饿值
            .saturationMod(0.6f)  // 饱和度修饰符
            .alwaysEat()
            .build();
    public static final FoodProperties SAUSAGE = new FoodProperties.Builder()
            .nutrition(10)  // 恢复的饥饿值
            .saturationMod(1f)  // 饱和度修饰符
            .meat()
            .alwaysEat()
            .effect(()->new MobEffectInstance(ModEffects.DAVEDIE.get(),300,0),1.0f)
            .build();

}
