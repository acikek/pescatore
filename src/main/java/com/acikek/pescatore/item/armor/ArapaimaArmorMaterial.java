package com.acikek.pescatore.item.armor;

import com.acikek.pescatore.item.PescatoreItems;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;

public class ArapaimaArmorMaterial implements ArmorMaterial {

    public static final ArapaimaArmorMaterial INSTANCE = new ArapaimaArmorMaterial();

    @Override
    public int getDurability(ArmorItem.Type type) {
        return 260;
    }

    @Override
    public int getProtection(ArmorItem.Type type) {
        return 3;
    }

    @Override
    public int getEnchantability() {
        return 13;
    }

    @Override
    public SoundEvent getEquipSound() {
        return SoundEvents.ITEM_ARMOR_EQUIP_ELYTRA;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.ofItems(PescatoreItems.ARAPAIMA);
    }

    @Override
    public String getName() {
        return "pescatore_arapaima";
    }

    @Override
    public float getToughness() {
        return 1.0f;
    }

    @Override
    public float getKnockbackResistance() {
        return 0;
    }
}
