package com.acikek.pescatore.mixin;

import com.acikek.pescatore.item.armor.CoelacanthChestplate;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.DamageTypeTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Shadow public abstract ItemStack getEquippedStack(EquipmentSlot slot);

    @ModifyReturnValue(method = "applyArmorToDamage", at = @At("RETURN"))
    private float pescatore$applyCoelacanthChestplate(float original, @Local DamageSource source) {
        if (source.isIn(DamageTypeTags.BYPASSES_ARMOR)
                || getEquippedStack(EquipmentSlot.CHEST).getItem() instanceof CoelacanthChestplate) {
            return original;
        }
        return ((Entity) (Object) this).getWorld().random.nextFloat() <= 0.1f ? 0.0f : original;
    }
}
