package com.example.examplemod.armor;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

public enum ModArmorMaterials implements ArmorMaterial {
    MYSTIC_ARMOR(
            "mystic",
            25,
            new int[]{3, 6, 8, 3},
            15,
            SoundEvents.ARMOR_EQUIP_NETHERITE,
            2.5f,
            0.1f,
            Ingredient.of(Items.NETHERITE_INGOT)
    );

    private final String name;
    private final int durabilityMultiplier;
    private final int[] slotProtections;
    private final int enchantmentValue;
    private final SoundEvent sound;
    private final float toughness;
    private final float knockbackResistance;
    private final Ingredient repairIngredient;

    ModArmorMaterials(
            String name,
            int durabilityMultiplier,
            int[] slotProtections,
            int enchantmentValue,
            SoundEvent sound,
            float toughness,
            float knockbackResistance,
            Ingredient repairIngredient
    ) {
        this.name = name;
        this.durabilityMultiplier = durabilityMultiplier;
        this.slotProtections = slotProtections;
        this.enchantmentValue = enchantmentValue;
        this.sound = sound;
        this.toughness = toughness;
        this.knockbackResistance = knockbackResistance;
        this.repairIngredient = repairIngredient;
    }

    @Override
    public int getDurabilityForType(ArmorItem.Type type) {
        return switch (type) {
            case BOOTS -> durabilityMultiplier * 13;
            case LEGGINGS -> durabilityMultiplier * 15;
            case CHESTPLATE -> durabilityMultiplier * 16;
            case HELMET -> durabilityMultiplier * 11;
        };
    }

    @Override
    public int getDefenseForType(ArmorItem.Type type) {
        return switch (type) {
            case BOOTS -> slotProtections[0];
            case LEGGINGS -> slotProtections[1];
            case CHESTPLATE -> slotProtections[2];
            case HELMET -> slotProtections[3];
        };
    }

    @Override
    public int getEnchantmentValue() {
        return enchantmentValue;
    }

    @Override
    public SoundEvent getEquipSound() {
        return sound;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return repairIngredient;
    }

    @Override
    public String getName() {
        return "awamod:"+name;
    }

    @Override
    public float getToughness() {
        return toughness;
    }

    @Override
    public float getKnockbackResistance() {
        return knockbackResistance;
    }
}