package com.acikek.pescatore.advancement;

import com.acikek.pescatore.Pescatore;
import com.acikek.pescatore.api.PescatoreAPI;
import com.acikek.pescatore.api.properties.MinigameFishRarity;
import com.acikek.pescatore.api.type.MinigameFishType;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.NumberRange;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import org.apache.commons.lang3.EnumUtils;

import java.util.Locale;

public class MinigameFishCaughtCriterion extends AbstractCriterion<MinigameFishCaughtCriterion.Conditions> {

    public static final Identifier ID = Pescatore.id("minigame_fish_caught");
    public static final MinigameFishCaughtCriterion INSTANCE = new MinigameFishCaughtCriterion();

    @Override
    public Identifier getId() {
        return ID;
    }

    @Override
    protected Conditions conditionsFromJson(JsonObject obj, LootContextPredicate playerPredicate, AdvancementEntityPredicateDeserializer predicateDeserializer) {
        var rod = ItemPredicate.fromJson(obj.get("rod"));
        var type = obj.has("fish_type") ? MinigameFishType.REGISTRY.get(new Identifier(JsonHelper.getString(obj, "fish_type"))) : null;
        var rarity = obj.has("fish_rarity") ? EnumUtils.getEnumIgnoreCase(MinigameFishRarity.class, JsonHelper.getString(obj, "fish_rarity"), null) : null;
        var size = NumberRange.FloatRange.fromJson(obj.get("fish_size"));
        var rarityCaught = NumberRange.IntRange.fromJson(obj.get("rarity_caught"));
        var totalCaught = NumberRange.IntRange.fromJson(obj.get("total_caught"));
        return new Conditions(rod, type, rarity, size, rarityCaught, totalCaught, playerPredicate);
    }

    public void trigger(ServerPlayerEntity player, ItemStack rod, MinigameFishType type) {
        trigger(player, conditions -> conditions.matches(player, rod, type));
    }

    public static class Conditions extends AbstractCriterionConditions {

        public ItemPredicate rod;
        public MinigameFishType type;
        public MinigameFishRarity rarity;
        public NumberRange.FloatRange size;
        public NumberRange.IntRange rarityCaught;
        public NumberRange.IntRange totalCaught;

        public Conditions(ItemPredicate rod, MinigameFishType type, MinigameFishRarity rarity, NumberRange.FloatRange size, NumberRange.IntRange rarityCaught, NumberRange.IntRange totalCaught, LootContextPredicate entity) {
            super(ID, entity);
            this.rod = rod;
            this.type = type;
            this.rarity = rarity;
            this.size = size;
            this.rarityCaught = rarityCaught;
            this.totalCaught = totalCaught;
        }

        public static AdvancementCriterion type(MinigameFishType type) {
            return new AdvancementCriterion(new Conditions(ItemPredicate.ANY, type, null, NumberRange.FloatRange.ANY, NumberRange.IntRange.ANY, NumberRange.IntRange.ANY, LootContextPredicate.EMPTY));
        }

        public static AdvancementCriterion size(NumberRange.FloatRange size) {
            return new AdvancementCriterion(new Conditions(ItemPredicate.ANY, null, null, size, NumberRange.IntRange.ANY, NumberRange.IntRange.ANY, LootContextPredicate.EMPTY));
        }

        public static AdvancementCriterion totalCaught(NumberRange.IntRange totalCaught) {
            return new AdvancementCriterion(new Conditions(ItemPredicate.ANY, null, null, NumberRange.FloatRange.ANY, NumberRange.IntRange.ANY, totalCaught, LootContextPredicate.EMPTY));
        }

        public static AdvancementCriterion rarityCaught(MinigameFishRarity rarity, NumberRange.IntRange rarityCaught) {
            return new AdvancementCriterion(new Conditions(ItemPredicate.ANY, null, rarity, NumberRange.FloatRange.ANY, rarityCaught, NumberRange.IntRange.ANY, LootContextPredicate.EMPTY));
        }

        public boolean matches(ServerPlayerEntity player, ItemStack rodStack, MinigameFishType type) {
            if (this.type != null && !this.type.equals(type)) {
                return false;
            }
            if (this.rarity != null && this.rarity != type.rarity()) {
                return false;
            }
            return rod.test(rodStack)
                    && size.test(type.size().scale())
                    && rarityCaught.test(player.getStatHandler().getStat(type.rarity().getStat()))
                    && totalCaught.test(player.getStatHandler().getStat(PescatoreAPI.getTotalCaughtStat()));
        }

        @Override
        public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
            var obj = super.toJson(predicateSerializer);
            obj.add("rod", rod.toJson());
            if (type != null) {
                obj.add("fish_type", new JsonPrimitive(type.getId().toString()));
            }
            if (rarity != null) {
                obj.add("fish_rarity", new JsonPrimitive(rarity.toString().toLowerCase(Locale.ROOT)));
            }
            obj.add("fish_size", size.toJson());
            obj.add("rarity_caught", rarityCaught.toJson());
            obj.add("total_caught", totalCaught.toJson());
            return obj;
        }
    }

    public static void register() {
        Criteria.register(MinigameFishCaughtCriterion.INSTANCE);
    }
}
