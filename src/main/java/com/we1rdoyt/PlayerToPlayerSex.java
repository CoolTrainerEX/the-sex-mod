package com.we1rdoyt;

import com.we1rdoyt.entity.effect.ModStatusEffects;
import com.we1rdoyt.entity.effect.STDStatusEffect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;

public class PlayerToPlayerSex implements Sex {
    private final PlayerEntity player, target;
    private final boolean consent = true;
    private boolean started = false, ended = false;

    public PlayerToPlayerSex(PlayerEntity player, PlayerEntity target) {
        this.player = player;
        this.target = target;
    }

    @Override
    public LivingEntity getEntity() {
        return player;
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
        player.startRiding(target);
        started = true;
    }

    @Override
    public boolean tick() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'tick'");
    }

    @Override
    public void endSex() {
        PlayerEntity[] entities = { player, target };

        player.stopRiding();

        boolean playerHasSTD = player.hasStatusEffect(ModStatusEffects.STD);
        boolean targetHasSTD = target.hasStatusEffect(ModStatusEffects.STD);

        if ((playerHasSTD && targetHasSTD) || (!playerHasSTD && !targetHasSTD))
            return;

        for (int i = 0; i < entities.length; i++) {
            PlayerEntity source = entities[i];
            PlayerEntity dest = entities[1 - i];

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
