package com.we1rdoyt;

import com.we1rdoyt.entity.effect.ModStatusEffects;
import com.we1rdoyt.entity.effect.STDStatusEffect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;

public class PlayerToEntitySex implements Sex {
    private final PlayerEntity player;
    private final LivingEntity entity;
    private final boolean consent;
    private boolean started = false, ended = false;

    public PlayerToEntitySex(PlayerEntity player, LivingEntity entity) {
        this.player = player;
        this.entity = entity;

        double chance = 0.5;

        if (entity.hasStatusEffect(ModStatusEffects.LIBIDO))
            chance += (entity.getStatusEffect(ModStatusEffects.LIBIDO).getAmplifier() + 1) * 0.25;

        if (entity.getRandom().nextDouble() < chance) {
            Sex.noConsentParticles(entity, (ServerWorld) entity.getWorld());
            consent = false;
            return;
        }

        consent = true;

    }

    @Override
    public LivingEntity getEntity() {
        return player;
    }

    @Override
    public LivingEntity getTarget() {
        return entity;
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
        player.startRiding(entity);
    }

    @Override
    public boolean tick() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'tick'");
    }

    @Override
    public void endSex() {
        LivingEntity[] entities = { player, entity };

        player.stopRiding();

        boolean playerHasSTD = player.hasStatusEffect(ModStatusEffects.STD);
        boolean entityHasSTD = entity.hasStatusEffect(ModStatusEffects.STD);

        if ((playerHasSTD && entityHasSTD) || (!playerHasSTD && !entityHasSTD))
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
    }
}
