package com.we1rdoyt.entity.effect;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.particle.ParticleTypes;

public class LibidoStatusEffect extends StatusEffect {

    protected LibidoStatusEffect(StatusEffectCategory category, int color) {
        super(category, color, ParticleTypes.HEART);
    }
}
