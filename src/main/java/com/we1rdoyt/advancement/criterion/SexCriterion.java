package com.we1rdoyt.advancement.criterion;

import java.util.Optional;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;

public class SexCriterion extends AbstractCriterion<SexCriterion.Conditions> {
    @Override
    public Codec<Conditions> getConditionsCodec() {
        return SexCriterion.Conditions.CODEC;
    }

    public void trigger(ServerPlayerEntity player, LivingEntity entity, LivingEntity target, boolean consent) {
        super.trigger(player, conditions -> (conditions.entity().isEmpty() || conditions.entity().get()
                .test(EntityPredicate.createAdvancementEntityLootContext(player, entity)))
                && (conditions.target().isEmpty() || conditions.target().get().test(
                        EntityPredicate.createAdvancementEntityLootContext(player, target)))
                && (conditions.consent().isEmpty() || conditions.consent().get() == consent)
                && (conditions.isChild().isEmpty()
                        || target instanceof MobEntity targetMob && conditions.isChild().get() == targetMob.isBaby()));
    }

    public record Conditions(Optional<LootContextPredicate> player, Optional<LootContextPredicate> entity,
            Optional<LootContextPredicate> target, Optional<Boolean> consent, Optional<Boolean> isChild)
            implements AbstractCriterion.Conditions {
        public static final Codec<Conditions> CODEC = RecordCodecBuilder.create(instance -> instance
                .group(EntityPredicate.LOOT_CONTEXT_PREDICATE_CODEC.optionalFieldOf("player")
                        .forGetter(SexCriterion.Conditions::player),
                        EntityPredicate.LOOT_CONTEXT_PREDICATE_CODEC.optionalFieldOf("entity")
                                .forGetter(SexCriterion.Conditions::entity),
                        EntityPredicate.LOOT_CONTEXT_PREDICATE_CODEC.optionalFieldOf("target")
                                .forGetter(SexCriterion.Conditions::target),
                        Codec.BOOL.optionalFieldOf("consent")
                                .forGetter(SexCriterion.Conditions::consent),
                        Codec.BOOL.optionalFieldOf("is_child").forGetter(SexCriterion.Conditions::isChild))
                .apply(instance, SexCriterion.Conditions::new));
    }
}
