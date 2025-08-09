package com.example.examplemod.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

// ModEntities.java
public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, "awamod");

    public static final RegistryObject<EntityType<ThrowingDaggerEntity>> THROWING_DAGGER =
            ENTITIES.register("throwing_dagger", () ->
                    EntityType.Builder.<ThrowingDaggerEntity>of(ThrowingDaggerEntity::new, MobCategory.MISC)
                            .sized(0.5F, 0.5F)
                            .clientTrackingRange(4)
                            .updateInterval(10)
                            .build("throwing_dagger"));
}
