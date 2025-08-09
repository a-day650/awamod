package com.example.examplemod.item;

import com.example.examplemod.AwaMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, AwaMod.MODID);
    public static final RegistryObject<CreativeModeTab> AWA_TAB = CREATIVE_MODE_TABS.register("awa_tab",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ModItems.EXAMPLE_ITEM.get())) // 设置图标
                    .title(Component.translatable("itemGroup.awamod.awa_tab")) // 设置标题(需要添加到语言文件)
                    .displayItems((params, output) -> {
                        // 添加物品到该组
                        output.accept(ModItems.EXAMPLE_ITEM.get());
                        output.accept(ModItems.SAUSAGE.get());
                        output.accept(ModItems.MYSTIC_HELMET.get());
                        output.accept(ModItems.MYSTIC_CHESTPLATE.get());
                        output.accept(ModItems.MYSTIC_LEGGINGS.get());
                        output.accept(ModItems.MYSTIC_BOOTS.get());
                        output.accept(ModItems.IRON_DAGGER.get());
                    })
                    .build());

    // 注册方法
    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
