package com.acikek.pescatore.item.armor;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ArmorItem;

import java.util.UUID;

public class ArapaimaLeggings extends ArmorItem {

    public static final UUID MODIFIER_UUID = UUID.fromString("0ba170c5-b150-4f0f-83e4-ff1f8b289cee");
    public static final EntityAttributeModifier MODIFIER = new EntityAttributeModifier(
            MODIFIER_UUID, "pescatore:arapaima_leggings_swiftness", 0.10,
            EntityAttributeModifier.Operation.MULTIPLY_TOTAL
    );

    public ArapaimaLeggings(Settings settings) {
        super(ArapaimaArmorMaterial.INSTANCE, Type.LEGGINGS, settings);
    }

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
        var map = HashMultimap.create(super.getAttributeModifiers(slot));
        if (slot == EquipmentSlot.LEGS) {
            map.put(EntityAttributes.GENERIC_MOVEMENT_SPEED, MODIFIER);
        }
        return map;
    }
}
