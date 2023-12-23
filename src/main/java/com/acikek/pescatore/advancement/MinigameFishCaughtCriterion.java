package com.acikek.pescatore.advancement;

import com.acikek.pescatore.Pescatore;
import com.acikek.pescatore.api.PescatoreAPI;
import com.acikek.pescatore.api.properties.MinigameFishRarity;
import com.acikek.pescatore.api.type.MinigameFishType;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.NumberRange;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.dynamic.Codecs;

import java.util.Optional;

public class MinigameFishCaughtCriterion extends AbstractCriterion<MinigameFishCaughtCriterion.Conditions> {

    public static final MinigameFishCaughtCriterion INSTANCE = new MinigameFishCaughtCriterion();

    @Override
    public Codec<Conditions> getConditionsCodec() {
        return Conditions.CODEC;
    }

    public void trigger(ServerPlayerEntity player, ItemStack rod, MinigameFishType type) {
        trigger(player, conditions -> conditions.matches(player, rod, type));
    }

    public record Conditions(Optional<LootContextPredicate> player, Optional<ItemPredicate> rod, Optional<MinigameFishType> type, Optional<MinigameFishRarity> rarity,
                             Optional<NumberRange.DoubleRange> size, Optional<NumberRange.IntRange> rarityCaught, Optional<NumberRange.IntRange> totalCaught) implements AbstractCriterion.Conditions {

        public static final Codec<Conditions> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        Codecs.createStrictOptionalFieldCodec(EntityPredicate.LOOT_CONTEXT_PREDICATE_CODEC, "player").forGetter(Conditions::player),
                        Codecs.createStrictOptionalFieldCodec(ItemPredicate.CODEC, "rod").forGetter(Conditions::rod),
                        Codecs.createStrictOptionalFieldCodec(MinigameFishType.CODEC, "fish_type").forGetter(Conditions::type),
                        Codecs.createStrictOptionalFieldCodec(MinigameFishRarity.CODEC, "fish_rarity").forGetter(Conditions::rarity),
                        Codecs.createStrictOptionalFieldCodec(NumberRange.DoubleRange.CODEC, "fish_size").forGetter(Conditions::size),
                        Codecs.createStrictOptionalFieldCodec(NumberRange.IntRange.CODEC, "rarity_caught").forGetter(Conditions::rarityCaught),
                        Codecs.createStrictOptionalFieldCodec(NumberRange.IntRange.CODEC, "total_caught").forGetter(Conditions::totalCaught)
                ).apply(instance, Conditions::new)
        );

        public static AdvancementCriterion<Conditions> create(Optional<ItemPredicate> rod, Optional<MinigameFishType> type, Optional<MinigameFishRarity> rarity, Optional<NumberRange.DoubleRange> size, Optional<NumberRange.IntRange> rarityCaught, Optional<NumberRange.IntRange> totalCaught) {
            return INSTANCE.create(new Conditions(Optional.empty(), rod, type, rarity, size, rarityCaught, totalCaught));
        }

        public static AdvancementCriterion<Conditions> type(MinigameFishType type) {
            return create(Optional.empty(), Optional.of(type), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());
        }

        public static AdvancementCriterion<Conditions> size(NumberRange.DoubleRange size) {
            return create(Optional.empty(), Optional.empty(), Optional.empty(), Optional.of(size), Optional.empty(), Optional.empty());
        }

        public static AdvancementCriterion<Conditions> totalCaught(NumberRange.IntRange totalCaught) {
            return create(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.of(totalCaught));
        }

        public static AdvancementCriterion<Conditions> rarityCaught(MinigameFishRarity rarity, NumberRange.IntRange rarityCaught) {
            return create(Optional.empty(), Optional.empty(), Optional.of(rarity), Optional.empty(), Optional.of(rarityCaught), Optional.empty());
        }

        public boolean matches(ServerPlayerEntity player, ItemStack rodStack, MinigameFishType type) {
            if (rod.isPresent() && !rod.get().test(rodStack)) {
                return false;
            }
            if (this.type.isPresent() && !this.type.get().equals(type)) {
                return false;
            }
            if (rarity.isPresent() && rarity.get() != type.rarity()) {
                return false;
            }
            if (size.isPresent() && !size.get().test(type.size().scale())) {
                return false;
            }
            if (rarityCaught.isPresent() && !rarityCaught.get().test(player.getStatHandler().getStat(type.rarity().getStat()))) {
                return false;
            }
            return totalCaught.isEmpty() || totalCaught.get().test(player.getStatHandler().getStat(PescatoreAPI.getTotalCaughtStat()));
        }
    }

    public static void register() {
        Registry.register(Registries.CRITERION, Pescatore.id("minigame_fish_caught"), INSTANCE);
    }
}
