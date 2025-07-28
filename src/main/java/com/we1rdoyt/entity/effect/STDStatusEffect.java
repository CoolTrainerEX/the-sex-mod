package com.we1rdoyt.entity.effect;

import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.util.Identifier;

public class STDStatusEffect extends StatusEffect {
    private final double infectChance;

    protected STDStatusEffect(StatusEffectCategory statusEffectCategory, int i, double amount, double infectChance) {
        super(statusEffectCategory, i);
        this.infectChance = infectChance;
        addAttributeModifier(EntityAttributes.MAX_HEALTH, Identifier.ofVanilla("effect.health_boost"), -amount, EntityAttributeModifier.Operation.ADD_VALUE);
    }

    public double getInfectChance() {
        return infectChance;
    }
}