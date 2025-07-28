package com.we1rdoyt.entity.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.particle.ParticleTypes;

public class LibidoStatusEffect extends StatusEffect {
    private final double stdChance;

    protected LibidoStatusEffect(StatusEffectCategory category, int color, double stdChance) {
        super(category, color, ParticleTypes.HEART);
        this.stdChance = stdChance;
    }

    @Override
    public void onApplied(LivingEntity entity, int amplifier) {
        if (entity.getRandom().nextDouble() < stdChance)
            entity.addStatusEffect(new StatusEffectInstance(ModStatusEffects.STD, StatusEffectInstance.INFINITE));

        super.onApplied(entity, amplifier);
    }
}