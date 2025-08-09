package com.example.examplemod;

import com.example.examplemod.entity.ModEntities;
import com.example.examplemod.event.ModEvents;
import com.example.examplemod.item.ModCreativeModeTabs;
import com.example.examplemod.item.ModItems;
import com.example.examplemod.thirst.Thirst;
import com.example.examplemod.thirst.ThirstManager;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import static com.example.examplemod.effect.ModEffects.EFFECTS;

@Mod("awamod") // 替换为你的MODID
public class AwaMod {
    public static final String MODID = "awamod";
    public AwaMod() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        EFFECTS.register(modEventBus);
        ModItems.register(modEventBus);
        ModCreativeModeTabs.register(modEventBus);
        ModEvents.register();
        modEventBus.addListener(this::registerCapabilities);
        ModEntities.ENTITIES.register(modEventBus);
    }
    private void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.register(Thirst.class); // 注册口渴能力
    }
}