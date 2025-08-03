package com.we1rdoyt;

import com.we1rdoyt.entity.effect.ModStatusEffects;
import com.we1rdoyt.entity.effect.STDStatusEffect;

import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.GameRules;

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

        for (MobEntity mobEntity : new MobEntity[] { mob, target })
            if (mobEntity.hasStatusEffect(ModStatusEffects.LIBIDO))
                bonus += (mobEntity.getStatusEffect(ModStatusEffects.LIBIDO).getAmplifier() + 1) * 5;

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
        MobEntity[] mobs = { mob, target };

        ended = true;
        mob.stopRiding();
        breed();

        boolean entityHasSTD = mob.hasStatusEffect(ModStatusEffects.STD);
        boolean targetHasSTD = target.hasStatusEffect(ModStatusEffects.STD);

        if ((entityHasSTD && targetHasSTD) || (!entityHasSTD && !targetHasSTD))
            return;

        for (int i = 0; i < mobs.length; i++) {
            MobEntity source = mobs[i];
            MobEntity dest = mobs[1 - i];

            if (source.hasStatusEffect(ModStatusEffects.STD) && !dest.hasStatusEffect(ModStatusEffects.STD)) {
                StatusEffectInstance statusEffectInstance = source.getStatusEffect(ModStatusEffects.STD);

                if (dest.getRandom().nextDouble() < ((STDStatusEffect) statusEffectInstance.getEffectType().value())
                        .getInfectChance() + (statusEffectInstance.getAmplifier() + 1) * 0.25 - Sex.stdResistance(dest))
                    dest.addStatusEffect(new StatusEffectInstance(statusEffectInstance));
            }
        }
    }

    @Override
    public void breed() {
        Random random = mob.getRandom();

        if (sexBar >= MAX_SEX_BAR && random.nextDouble() >= Sex.stdResistance(mob) + Sex.stdResistance(target)
                && mob.getWorld() instanceof ServerWorld world) {
            boolean sameType = mob.getType() == target.getType();
            MobEntity child;

            if (sameType && mob instanceof AnimalEntity animal
                    && target instanceof AnimalEntity animalTarget)
                animal.breed(world, animalTarget);
            else if (sameType && mob instanceof PassiveEntity passiveEntity
                    && target instanceof PassiveEntity passiveTarget
                    && (child = passiveEntity.createChild(world, passiveTarget)) != null) {
                child.setBaby(true);
                child.refreshPositionAndAngles(target.getX(), target.getY(), target.getZ(), 0, 0);
                passiveEntity.setBreedingAge(6000);
                passiveTarget.setBreedingAge(6000);
                world.sendEntityStatus(target, EntityStatuses.ADD_BREEDING_PARTICLES);

                if (world.getGameRules().getBoolean(GameRules.DO_MOB_LOOT))
                    world.spawnEntity(new ExperienceOrbEntity(world, target.getX(), target.getY(), target.getZ(),
                            random.nextInt(7) + 1));

                world.spawnEntityAndPassengers(child);
            } else {
                child = (MobEntity) (random.nextBoolean() ? mob : target).getType().create(world,
                        SpawnReason.BREEDING);

                child.setBaby(true);
                child.refreshPositionAndAngles(target.getX(), target.getY(), target.getZ(), 0, 0);

                if (mob instanceof PassiveEntity passiveEntity)
                    passiveEntity.setBreedingAge(6000);
                if (target instanceof PassiveEntity passiveEntity)
                    passiveEntity.setBreedingAge(6000);

                world.sendEntityStatus(target, EntityStatuses.ADD_BREEDING_PARTICLES);

                if (world.getGameRules().getBoolean(GameRules.DO_MOB_LOOT))
                    world.spawnEntity(new ExperienceOrbEntity(world, mob.getX(), mob.getY(), mob.getZ(),
                            random.nextInt(7) + 1));

                world.spawnEntityAndPassengers(child);
            }
        }
    }
}
