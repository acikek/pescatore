package com.acikek.pescatore.item.armor;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ArmorItem;

import java.util.UUID;

public class CoelacanthChestplate extends ArmorItem {

    public static final UUID MODIFIER_UUID = UUID.fromString("44cdbfe0-9b2e-4dfc-b132-049b49daec2d");
    public static final EntityAttributeModifier MODIFIER = new EntityAttributeModifier(
            MODIFIER_UUID, "pescatore:coelacanth_chestplate_slowness", -0.35,
            EntityAttributeModifier.Operation.MULTIPLY_TOTAL
    );

    public CoelacanthChestplate(Settings settings) {
        super(CoelacanthArmorMaterial.INSTANCE, Type.CHESTPLATE, settings);
    }

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
        var map = HashMultimap.create(super.getAttributeModifiers(slot));
        if (slot == EquipmentSlot.CHEST) {
            map.put(EntityAttributes.GENERIC_MOVEMENT_SPEED, MODIFIER);
        }
        return map;
    }
}
