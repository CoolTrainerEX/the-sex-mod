package com.we1rdoyt;

import com.we1rdoyt.entity.effect.ModStatusEffects;
import com.we1rdoyt.entity.effect.STDStatusEffect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;

public class MobToMobSex implements Sex {
    private final MobEntity mob, target;
    private final boolean consent;
    private boolean started = false, ended = false;
    private int tick = 0, sexBar = 0, sexHealth = MAX_SEX_HEALTH;

    public MobToMobSex(MobEntity mob, MobEntity target) {
        this.mob = mob;
        this.target = target;

        for (MobEntity mobEntity : new MobEntity[] { mob, target }) {
            double chance = 0.5;

            if (mobEntity.hasStatusEffect(ModStatusEffects.LIBIDO))
                chance += (mobEntity.getStatusEffect(ModStatusEffects.LIBIDO).getAmplifier() + 1) * 0.25;

            if (mobEntity.getRandom().nextDouble() >= chance) {
                Sex.entityParticles(mobEntity, ParticleTypes.ANGRY_VILLAGER, (ServerWorld) mobEntity.getWorld());
                consent = false;
                return;
            }
        }

        consent = true;
    }

    @Override
    public LivingEntity getEntity() {
        return mob;
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
        mob.startRiding(target, true);
        started = true;
    }

    @Override
    public boolean tick() {
        if (mob.isRemoved() || target.isRemoved() || !target.hasPassenger(mob) || sexBar >= MAX_SEX_BAR
                || sexHealth <= 0)
            return false;

        tick = (tick + 1) % MAX_TICKS;

        if (tick == 0)
            fail();
        else if (mob.getRandom().nextBoolean())
            success();

        return true;
    }

    @Override
    public void success() {
        int bonus = 0;

        for (MobEntity MobEntity : new MobEntity[] { mob, target })
            if (MobEntity.hasStatusEffect(ModStatusEffects.LIBIDO))
                bonus += (MobEntity.getStatusEffect(ModStatusEffects.LIBIDO).getAmplifier() + 1) * 5;

        sexBar += (double) MAX_SEX_BAR_ADD * Math.min(tick + bonus, MAX_TICKS) / MAX_TICKS;
        tick = 0;
        Sex.entityParticles(mob, ParticleTypes.HAPPY_VILLAGER, (ServerWorld) mob.getWorld());
        mob.playAmbientSound();
    }

    @Override
    public void fail() {
        sexHealth--;
        target.damage((ServerWorld) target.getWorld(), mob.getDamageSources().mobAttack(mob), 1);
        Sex.entityParticles(mob, ParticleTypes.ANGRY_VILLAGER, (ServerWorld) mob.getWorld());
    }

    @Override
    public void endSex() {
        MobEntity[] entities = { mob, target };

        ended = true;
        mob.stopRiding();

        boolean entityHasSTD = mob.hasStatusEffect(ModStatusEffects.STD);
        boolean targetHasSTD = target.hasStatusEffect(ModStatusEffects.STD);

        if ((entityHasSTD && targetHasSTD) || (!entityHasSTD && !targetHasSTD))
            return;

        for (int i = 0; i < entities.length; i++) {
            MobEntity source = entities[i];
            MobEntity dest = entities[1 - i];

            if (source.hasStatusEffect(ModStatusEffects.STD) && !dest.hasStatusEffect(ModStatusEffects.STD)) {
                StatusEffectInstance statusEffectInstance = source.getStatusEffect(ModStatusEffects.STD);

                if (dest.getRandom().nextDouble() < ((STDStatusEffect) statusEffectInstance.getEffectType().value())
                        .getInfectChance() + (statusEffectInstance.getAmplifier() + 1) * 0.25)
                    dest.addStatusEffect(new StatusEffectInstance(statusEffectInstance));
            }
        }
    }
}
