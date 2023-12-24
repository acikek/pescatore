package com.acikek.pescatore.item;

import com.google.common.collect.Multimap;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ItemStack;

import java.util.UUID;

public class PiranhaNecklaceItem extends TrinketItem {

    public PiranhaNecklaceItem(Settings settings) {
        super(settings);
    }

    public static EntityAttributeModifier getAttributeModifier(UUID uuid) {
        return new EntityAttributeModifier(uuid, "pescatore:piranha_necklace_damage", 1, EntityAttributeModifier.Operation.ADDITION);
    }

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getModifiers(ItemStack stack, SlotReference slot, LivingEntity entity, UUID uuid) {
        var modifiers = super.getModifiers(stack, slot, entity, uuid);
        modifiers.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, getAttributeModifier(uuid));
        return modifiers;
    }
}
