package com.we1rdoyt;

import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;

/**
 * Sex logic
 * 
 * @author CoolTrainerEX
 */
public interface Sex {
    public LivingEntity getEntity();

    public LivingEntity getTarget();

    public boolean getConsent();

    public boolean isStarted();

    public boolean isEnded();

    /**
     * Spawns particles to indicate that the entity does not consent
     * 
     * @param entity Entity to emit particles
     * @param world  World to emit particles in
     */
    public static void noConsentParticles(LivingEntity entity, ServerWorld world) {
        world.spawnParticles(ParticleTypes.ANGRY_VILLAGER, entity.getParticleX(0.5), entity.getBodyY(1),
                entity.getParticleZ(0.5), 5, 0.25, 0.25, 0.25, 1);
    }

    /**
     * Starts the sex process
     */
    public void startSex();

    /**
     * Sex tick process
     * 
     * @return True if run on next tick
     */
    public boolean tick();

    /**
     * Ends the sex process
     */
    public void endSex();
}
