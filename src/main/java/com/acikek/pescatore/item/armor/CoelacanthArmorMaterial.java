package com.acikek.pescatore.item.armor;

import com.acikek.pescatore.item.PescatoreItems;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;

public class CoelacanthArmorMaterial implements ArmorMaterial {

    public static final CoelacanthArmorMaterial INSTANCE = new CoelacanthArmorMaterial();

    @Override
    public int getDurability(ArmorItem.Type type) {
        return 480;
    }

    @Override
    public int getProtection(ArmorItem.Type type) {
        return 9;
    }

    @Override
    public int getEnchantability() {
        return 10;
    }

    @Override
    public SoundEvent getEquipSound() {
        return SoundEvents.ITEM_ARMOR_EQUIP_TURTLE;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.ofItems(PescatoreItems.COELACANTH);
    }

    @Override
    public String getName() {
        return "pescatore_coelacanth";
    }

    @Override
    public float getToughness() {
        return 3.0f;
    }

    @Override
    public float getKnockbackResistance() {
        return 0.0f;
    }
}
