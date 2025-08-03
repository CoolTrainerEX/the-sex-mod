package com.we1rdoyt.item;

import com.we1rdoyt.TheSexMod;
import com.we1rdoyt.potion.ModPotions;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroups {
    public static final RegistryKey<ItemGroup> SEX_ITEMS = register("sex_items");

    public static void initialize() {
        Registry.register(Registries.ITEM_GROUP, SEX_ITEMS,
                FabricItemGroup.builder().displayName(Text.translatable("itemGroup.the-sex-mod"))
                        .icon(() -> new ItemStack(ModItems.CONDOM)).entries((itemContext, entries) -> {
                            entries.add(ModItems.CONSENT);
                            entries.add(ModItems.RUBBER);
                            entries.add(ModItems.CONDOM);
                            entries.add(ModItems.CONDOM_HELMET);
                            entries.add(ModItems.CONDOM_CHESTPLATE);
                            entries.add(ModItems.CONDOM_LEGGINGS);
                            entries.add(ModItems.CONDOM_BOOTS);
                            entries.add(ModItems.CONDOM_HORSE_ARMOR);
                            entries.add(Items.POPPY);
                            entries.add(ModItems.OPIUM);
                            entries.add(PotionContentsComponent.createStack(Items.TIPPED_ARROW, ModPotions.SEX));
                            entries.add(PotionContentsComponent.createStack(Items.TIPPED_ARROW, ModPotions.LONG_SEX));
                            entries.add(PotionContentsComponent.createStack(Items.TIPPED_ARROW, ModPotions.STRONG_SEX));
                            entries.add(PotionContentsComponent.createStack(Items.POTION, ModPotions.SEX));
                            entries.add(PotionContentsComponent.createStack(Items.POTION, ModPotions.LONG_SEX));
                            entries.add(PotionContentsComponent.createStack(Items.POTION, ModPotions.STRONG_SEX));
                            entries.add(PotionContentsComponent.createStack(Items.SPLASH_POTION, ModPotions.SEX));
                            entries.add(PotionContentsComponent.createStack(Items.SPLASH_POTION, ModPotions.LONG_SEX));
                            entries.add(
                                    PotionContentsComponent.createStack(Items.SPLASH_POTION, ModPotions.STRONG_SEX));
                            entries.add(PotionContentsComponent.createStack(Items.LINGERING_POTION, ModPotions.SEX));
                            entries.add(
                                    PotionContentsComponent.createStack(Items.LINGERING_POTION, ModPotions.LONG_SEX));
                            entries.add(
                                    PotionContentsComponent.createStack(Items.LINGERING_POTION, ModPotions.STRONG_SEX));
                        }).build());

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(entries -> {
            entries.add(ModItems.CONSENT);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> {
            entries.add(ModItems.CONDOM_HELMET);
            entries.add(ModItems.CONDOM_CHESTPLATE);
            entries.add(ModItems.CONDOM_LEGGINGS);
            entries.add(ModItems.CONDOM_LEGGINGS);
            entries.add(ModItems.CONDOM_BOOTS);
            entries.add(ModItems.CONDOM_HORSE_ARMOR);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries -> {
            entries.add(ModItems.OPIUM);
            entries.add(ModItems.RUBBER);
            entries.add(ModItems.CONDOM);
        });
    }

    private static final RegistryKey<ItemGroup> register(String id) {
        return RegistryKey.of(RegistryKeys.ITEM_GROUP, Identifier.of(TheSexMod.MOD_ID, id));
    }
}