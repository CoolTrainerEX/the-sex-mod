package com.we1rdoyt;

import com.we1rdoyt.entity.effect.ModStatusEffects;
import com.we1rdoyt.entity.effect.STDStatusEffect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.random.Random;

public class EntityToEntitySex implements Sex {
    private final LivingEntity entity, target;
    private final boolean consent;
    private boolean started = false, ended = false;
    private int tick = 0, sexBar = 0, sexHealth = MAX_SEX_HEALTH;

    public EntityToEntitySex(LivingEntity entity, LivingEntity target) {
        this.entity = entity;
        this.target = target;

        for (LivingEntity livingEntity : new LivingEntity[] { entity, target }) {
            double chance = 0.5;

            if (entity.hasStatusEffect(ModStatusEffects.LIBIDO))
                chance += (entity.getStatusEffect(ModStatusEffects.LIBIDO).getAmplifier() + 1) * 0.25;

            if (entity.getRandom().nextDouble() < chance) {
                Sex.entityParticles(livingEntity, ParticleTypes.ANGRY_VILLAGER, (ServerWorld) livingEntity.getWorld());
                consent = false;
                return;
            }
        }

        consent = true;
    }

    @Override
    public LivingEntity getEntity() {
        return entity;
    }

    @Override
    public LivingEntity getTarget() {
        return target;
    }

    @Override
    public boolean getConsent() {
        return consent;
    }

    @Override
    public boolean isStarted() {
        return started;
    }

    @Override
    public boolean isEnded() {
        return ended;
    }

    @Override
    public void startSex() {
        entity.startRiding(target, true);
        started = true;
    }

    @Override
    public boolean tick() {
        if (entity.isRemoved() || target.isRemoved() || !target.hasPassenger(entity) || sexBar >= MAX_SEX_BAR || sexHealth <= 0)
            return false;

        if (tick != 0)
            return true;

        Random random = entity.getRandom();

        if (random.nextBoolean()) {
            sexBar += random.nextInt(MAX_SEX_BAR) + 1;
            Sex.entityParticles(entity, ParticleTypes.HAPPY_VILLAGER, (ServerWorld) entity.getWorld());
        } else {
            sexHealth--;
            target.damage((ServerWorld) target.getWorld(), entity.getDamageSources().mobAttack(entity), 1);
        }

        tick = (tick + 1) % MAX_TICKS;
        return true;
    }

    @Override
    public void endSex() {
        LivingEntity[] entities = { entity, target };

        entity.stopRiding();

        boolean entityHasSTD = entity.hasStatusEffect(ModStatusEffects.STD);
        boolean targetHasSTD = target.hasStatusEffect(ModStatusEffects.STD);

        if ((entityHasSTD && targetHasSTD) || (!entityHasSTD && !targetHasSTD))
            return;

        for (int i = 0; i < entities.length; i++) {
            LivingEntity source = entities[i];
            LivingEntity dest = entities[1 - i];

            if (source.hasStatusEffect(ModStatusEffects.STD) && !dest.hasStatusEffect(ModStatusEffects.STD)) {
                StatusEffectInstance statusEffectInstance = source.getStatusEffect(ModStatusEffects.STD);

                if (dest.getRandom().nextDouble() < ((STDStatusEffect) statusEffectInstance.getEffectType().value())
                        .getInfectChance() + (statusEffectInstance.getAmplifier() + 1) * 0.25)
                    dest.addStatusEffect(new StatusEffectInstance(statusEffectInstance));
            }
        }

        ended = true;
    }
}
