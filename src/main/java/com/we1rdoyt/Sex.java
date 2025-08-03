package com.we1rdoyt;

import com.we1rdoyt.registry.tag.ModItemTags;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.server.world.ServerWorld;

/**
 * Sex logic
 * 
 * @author CoolTrainerEX
 */
public interface Sex {
    public static final int MAX_TICKS = 100, MAX_SEX_BAR = 100, MAX_SEX_BAR_ADD = 20, MAX_SEX_HEALTH = 3;

    public LivingEntity getEntity();

    public LivingEntity getTarget();

    public boolean getConsent();

    public boolean isStarted();

    public boolean isEnded();

    /**
     * Spawns particles on an entity
     * 
     * @param <T>    Particle effect
     * 
     * @param entity Entity to emit particles
     * @param world  World to emit particles in
     */
    public static <T extends ParticleEffect> void entityParticles(LivingEntity entity, T parameters,
            ServerWorld world) {
        world.spawnParticles(parameters, entity.getParticleX(0.5), entity.getBodyY(1),
                entity.getParticleZ(0.5), 5, 0.25, 0.25, 0.25, 1);
    }

    /**
     * Gets the STD resistance of an entity based on the worn equipment
     * 
     * @param entity Entity to check
     * @return STD resistance in percent
     * @see ModItemTags#STD_RESISTANT
     */
    public static double stdResistance(LivingEntity entity) {
        double resistance = 0;

        for (EquipmentSlot equipmentSlot : EquipmentSlot.values())
            if (entity.getEquippedStack(equipmentSlot).isIn(ModItemTags.STD_RESISTANT))
                resistance += 0.25;

        return resistance;
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
     * Successful sex round
     */
    public void success();

    /**
     * Unsuccessful sex round
     */
    public void fail();

    /**
     * Ends the sex process
     */
    public void endSex();

    /**
     * Breeds the two entities
     */
    public void breed();
}
