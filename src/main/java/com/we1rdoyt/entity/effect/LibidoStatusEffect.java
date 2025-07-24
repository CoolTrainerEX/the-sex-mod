package com.we1rdoyt.entity.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;

public class LibidoStatusEffect extends StatusEffect {

    protected LibidoStatusEffect(StatusEffectCategory category, int color) {
        super(category, color, ParticleTypes.HEART);
    }

    @Override
    public boolean applyUpdateEffect(ServerWorld world, LivingEntity entity, int amplifier) {
        return super.applyUpdateEffect(world, entity, amplifier);
    }
}
