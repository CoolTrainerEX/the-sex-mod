package com.we1rdoyt.entity.effect;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;

public class LibidoStatusEffect extends StatusEffect {
    private final double stdChance;

    protected LibidoStatusEffect(StatusEffectCategory category, int color, double stdChance) {
        super(category, color, ParticleTypes.HEART);
        this.stdChance = stdChance;
    }

    @Override
    public void applyInstantEffect(ServerWorld world, Entity effectEntity, Entity attacker, LivingEntity target,
            int amplifier, double proximity) {
        if (target.getRandom().nextDouble() < stdChance)
            target.addStatusEffect(new StatusEffectInstance(ModStatusEffects.STD, 3600));

        super.applyInstantEffect(world, effectEntity, attacker, target, amplifier, proximity);
    }
}