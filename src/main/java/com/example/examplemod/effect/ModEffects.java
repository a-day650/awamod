package com.example.examplemod.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEffects {
    public static final DeferredRegister<MobEffect> EFFECTS =
            DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, "awamod");

    public static final RegistryObject<MobEffect> BLEEDING =
            EFFECTS.register("bleeding", BleedingEffect::new);
    public static final RegistryObject<MobEffect> DAVEDIE =
            EFFECTS.register("davedie", DavedieEffect::new);
}