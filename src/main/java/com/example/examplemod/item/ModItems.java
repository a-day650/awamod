package com.example.examplemod.item;

import com.example.examplemod.AwaMod;
import com.example.examplemod.armor.ModArmorMaterials;
import net.minecraft.world.item.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    // 创建延迟注册器，用于注册物品
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, AwaMod.MODID);

    // 注册一个示例物品
    public static final RegistryObject<Item> EXAMPLE_ITEM = ITEMS.register("example_item",
            () -> new ExmpleItem(new Item.Properties().food(ModFoodProperties.EXAMPLE_FOOD)));

    public static final RegistryObject<Item> MYSTIC_HELMET = ITEMS.register("mystic_helmet",
            () -> new ArmorItem(ModArmorMaterials.MYSTIC_ARMOR, ArmorItem.Type.HELMET, new Item.Properties()));

    public static final RegistryObject<Item> MYSTIC_CHESTPLATE = ITEMS.register("mystic_chestplate",
            () -> new ArmorItem(ModArmorMaterials.MYSTIC_ARMOR, ArmorItem.Type.CHESTPLATE, new Item.Properties()));

    public static final RegistryObject<Item> MYSTIC_LEGGINGS= ITEMS.register("mystic_leggings",
            () -> new ArmorItem(ModArmorMaterials.MYSTIC_ARMOR, ArmorItem.Type.LEGGINGS, new Item.Properties()));

    public static final RegistryObject<Item> MYSTIC_BOOTS = ITEMS.register("mystic_boots",
            () -> new ArmorItem(ModArmorMaterials.MYSTIC_ARMOR, ArmorItem.Type.BOOTS, new Item.Properties()));
    public static final RegistryObject<Item> SAUSAGE  = ITEMS.register("sausage",
            () -> new Item(new Item.Properties().food(ModFoodProperties.SAUSAGE)));
    public static final RegistryObject<Item> IRON_DAGGER = ITEMS.register("iron_dagger",
            IronDagger::new);

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}