package com.we1rdoyt;

import java.util.function.Function;

import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class ModItems {
    public static final Item SEX_POTION = register("sex_potion", Item::new, new Item.Settings());

    public static void initialize() {
    }

    public static Item register(String name, Function<Item.Settings, Item> itemFactory, Item.Settings settings) {
        RegistryKey<Item> key = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(TheSexMod.MOD_ID, name));

        return Registry.register(Registries.ITEM, key, itemFactory.apply(settings.registryKey(key)));
    }
}
