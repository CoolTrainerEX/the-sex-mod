package com.we1rdoyt.registry.tag;

import com.we1rdoyt.TheSexMod;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public final class ModItemTags {
    public static final TagKey<Item> REPAIRS_CONDOM_ARMOR = of("repairs_condom_armor");

    private static TagKey<Item> of(String id) {
        return TagKey.of(RegistryKeys.ITEM, Identifier.of(TheSexMod.MOD_ID, id));
    }
}