package com.we1rdoyt.entity.effect;

import net.minecraft.entity.effect.PoisonStatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class STDStatusEffect extends PoisonStatusEffect {
    private final double infectChance;

    protected STDStatusEffect(StatusEffectCategory statusEffectCategory, int i, double infectChance) {
        super(statusEffectCategory, i);
        this.infectChance = infectChance;
    }

    public double getInfectChance() {
        return infectChance;
    }
}