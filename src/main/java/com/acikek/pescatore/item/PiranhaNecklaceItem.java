package com.acikek.pescatore.item;

import com.acikek.pescatore.Pescatore;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.UUID;

public class PiranhaNecklaceItem extends Item {

    public static final UUID MODIFIER_UUID = UUID.fromString("90c13013-e895-493f-96e9-4fb3c045680a");

    public PiranhaNecklaceItem(Settings settings) {
        super(settings);
    }

    public static EntityAttributeModifier getAttributeModifier(UUID uuid) {
        return new EntityAttributeModifier(uuid, "pescatore:piranha_necklace_damage", 1, EntityAttributeModifier.Operation.ADDITION);
    }

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
        Multimap<EntityAttribute, EntityAttributeModifier> modifiers = HashMultimap.create();
        if (!Pescatore.USE_TRINKETS && slot == EquipmentSlot.OFFHAND) {
            modifiers.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, getAttributeModifier(MODIFIER_UUID));
        }
        return modifiers;
    }
}
