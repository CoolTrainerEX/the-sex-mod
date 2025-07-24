package com.we1rdoyt;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroups {
    public static final RegistryKey<ItemGroup> SEX_ITEMS = register("sex_items");

    public static void initialize() {
        Registry.register(Registries.ITEM_GROUP, SEX_ITEMS, FabricItemGroup.builder()
                .displayName(Text.translatable("itemGroup.the-sex-mod"))
                .icon(() -> new ItemStack(ModItems.SEX_POTION))
                .build());

        ItemGroupEvents.modifyEntriesEvent(SEX_ITEMS).register(itemGroup -> {
            itemGroup.add(ModItems.SEX_POTION);
        });
    }

    public static final RegistryKey<ItemGroup> register(String name) {
        return RegistryKey.of(RegistryKeys.ITEM_GROUP, Identifier.of(TheSexMod.MOD_ID, name));
    }
}
