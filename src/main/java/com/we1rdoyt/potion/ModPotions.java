package com.we1rdoyt.potion;

import com.we1rdoyt.TheSexMod;
import com.we1rdoyt.entity.effect.ModStatusEffects;
import com.we1rdoyt.item.ModItems;

import net.fabricmc.fabric.api.registry.FabricBrewingRecipeRegistryBuilder;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

public class ModPotions {
    public static final RegistryEntry<Potion> SEX = register("sex",
            new Potion("sex", new StatusEffectInstance(ModStatusEffects.LIBIDO, 3600)));
    public static final RegistryEntry<Potion> LONG_SEX = register("long_sex",
            new Potion("sex", new StatusEffectInstance(ModStatusEffects.LIBIDO, 9600)));
    public static final RegistryEntry<Potion> STRONG_SEX = register("strong_sex",
            new Potion("sex", new StatusEffectInstance(ModStatusEffects.LIBIDO, 1800, 1)));

    public static void initialize() {
        FabricBrewingRecipeRegistryBuilder.BUILD.register(builder -> {
            builder.registerPotionRecipe(Potions.AWKWARD, ModItems.OPIUM, SEX);
            builder.registerPotionRecipe(ModPotions.SEX, Items.REDSTONE, LONG_SEX);
            builder.registerPotionRecipe(ModPotions.SEX, Items.GLOWSTONE_DUST, STRONG_SEX);
        });
    }

    private static RegistryEntry<Potion> register(String name, Potion potion) {
        return Registry.registerReference(Registries.POTION, Identifier.of(TheSexMod.MOD_ID, name), potion);
    }
}
