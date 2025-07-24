package com.we1rdoyt.item;

import com.we1rdoyt.TheSexMod;
import com.we1rdoyt.item.equipment.ModArmorMaterials;

import net.minecraft.component.type.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.equipment.EquipmentType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class ModItems extends Items {
    public static final Item OPIUM = register("opium",
            new Item.Settings().food(new FoodComponent.Builder().alwaysEdible().build()));
    public static final Item RUBBER = register("rubber");
    public static final Item CONDOM = register("condom");
    public static final Item CONDOM_HELMET = register("condom_helmet",
            new Item.Settings().armor(ModArmorMaterials.CONDOM, EquipmentType.HELMET));
    public static final Item CONDOM_CHESTPLATE = register("condom_chestplate",
            new Item.Settings().armor(ModArmorMaterials.CONDOM, EquipmentType.CHESTPLATE));
    public static final Item CONDOM_LEGGINGS = register("condom_leggings",
            new Item.Settings().armor(ModArmorMaterials.CONDOM, EquipmentType.LEGGINGS));
    public static final Item CONDOM_BOOTS = register("condom_boots",
            new Item.Settings().armor(ModArmorMaterials.CONDOM, EquipmentType.BOOTS));
    public static final Item CONDOM_HORSE_ARMOR = register("condom_horse_armor",
            new Item.Settings().horseArmor(ModArmorMaterials.CONDOM));
    public static final Item CONSENT = register("consent", new Item.Settings());

    public static void initialize() {
    }

    private static RegistryKey<Item> keyOf(String id) {
        return RegistryKey.of(RegistryKeys.ITEM, Identifier.of(TheSexMod.MOD_ID, id));
    }

    public static Item register(String id, Item.Settings settings) {
        return register(keyOf(id), Item::new, settings);
    }

    public static Item register(String id) {
        return register(keyOf(id), Item::new, new Item.Settings());
    }

}
