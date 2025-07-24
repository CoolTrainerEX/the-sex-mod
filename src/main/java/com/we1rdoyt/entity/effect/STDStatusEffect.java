package com.we1rdoyt.entity.effect;

import java.util.Random;

import com.we1rdoyt.TheSexMod;
import com.we1rdoyt.item.ModItems;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.PoisonStatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.server.world.ServerWorld;

public class STDStatusEffect extends PoisonStatusEffect {
    private static Random random = new Random();

    protected STDStatusEffect(StatusEffectCategory statusEffectCategory, int i) {
        super(statusEffectCategory, i);
    }

    @Override
    public boolean applyUpdateEffect(ServerWorld world, LivingEntity entity, int amplifier) {
        double chance = 0.5 - (entity.getEquippedStack(EquipmentSlot.BODY).getItem() == ModItems.CONDOM_HORSE_ARMOR
                ? 0.25
                : (entity.getEquippedStack(EquipmentSlot.HEAD).getItem() == ModItems.CONDOM_HELMET ? 0.125 : 0)
                        + (entity.getEquippedStack(EquipmentSlot.CHEST).getItem() == ModItems.CONDOM_CHESTPLATE ? 0.125
                                : 0)
                        + (entity.getEquippedStack(EquipmentSlot.LEGS).getItem() == ModItems.CONDOM_LEGGINGS ? 0.125
                                : 0)
                        + (entity.getEquippedStack(EquipmentSlot.FEET).getItem() == ModItems.CONDOM_BOOTS ? 0.125 : 0));

        TheSexMod.LOGGER.info("{}", chance);

        if (random.nextDouble() < chance)
            return super.applyUpdateEffect(world, entity, amplifier);
        else
            return false;
    }
}
