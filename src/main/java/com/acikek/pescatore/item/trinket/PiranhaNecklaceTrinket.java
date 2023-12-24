package com.acikek.pescatore.item.trinket;

import com.acikek.pescatore.item.PiranhaNecklaceItem;
import com.google.common.collect.Multimap;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.Trinket;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ItemStack;

import java.util.UUID;

public class PiranhaNecklaceTrinket implements Trinket {

    public static final PiranhaNecklaceTrinket INSTANCE = new PiranhaNecklaceTrinket();

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getModifiers(ItemStack stack, SlotReference slot, LivingEntity entity, UUID uuid) {
        var modifiers = Trinket.super.getModifiers(stack, slot, entity, uuid);
        modifiers.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, PiranhaNecklaceItem.getAttributeModifier(uuid));
        return modifiers;
    }
}
