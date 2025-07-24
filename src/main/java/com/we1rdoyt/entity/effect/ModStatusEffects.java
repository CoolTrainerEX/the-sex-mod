package com.we1rdoyt.entity.effect;

import com.we1rdoyt.TheSexMod;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

public class ModStatusEffects {
    public static final RegistryEntry<StatusEffect> LIBIDO = register("libido",
            new LibidoStatusEffect(StatusEffectCategory.BENEFICIAL, 0xff0000));
    public static final RegistryEntry<StatusEffect> STD = register("std",
            new STDStatusEffect(StatusEffectCategory.HARMFUL, 0x00ff00));

    private static RegistryEntry<StatusEffect> register(String id, StatusEffect statusEffect) {
        return Registry.registerReference(Registries.STATUS_EFFECT, Identifier.of(TheSexMod.MOD_ID, id), statusEffect);
    }
}
