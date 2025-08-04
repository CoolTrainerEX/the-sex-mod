package com.we1rdoyt.advancement.criterion;

import java.util.Optional;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;

public class SexCompleteCriterion extends AbstractCriterion<SexCompleteCriterion.Conditions> {
    @Override
    public Codec<Conditions> getConditionsCodec() {
        return SexCompleteCriterion.Conditions.CODEC;
    }

    public void trigger(ServerPlayerEntity player, boolean successful) {
        super.trigger(player,
                conditions -> conditions.successful().isEmpty() || conditions.successful().get() == successful);
    }

    public record Conditions(Optional<LootContextPredicate> player, Optional<Boolean> successful)
            implements AbstractCriterion.Conditions {
        public static final Codec<Conditions> CODEC = RecordCodecBuilder.create(instance -> instance
                .group(EntityPredicate.LOOT_CONTEXT_PREDICATE_CODEC.optionalFieldOf("player")
                        .forGetter(SexCompleteCriterion.Conditions::player),
                        Codec.BOOL.optionalFieldOf("successful")
                                .forGetter(SexCompleteCriterion.Conditions::successful))
                .apply(instance, SexCompleteCriterion.Conditions::new));
    }
}
