package com.we1rdoyt.item.equipment;

import java.util.Map;

import com.google.common.collect.Maps;
import com.we1rdoyt.registry.tag.ModItemTags;

import net.minecraft.item.equipment.ArmorMaterial;
import net.minecraft.item.equipment.EquipmentType;
import net.minecraft.sound.SoundEvents;

public interface ModArmorMaterials {
        ArmorMaterial CONDOM = new ArmorMaterial(5, createDefenseMap(1, 2, 3, 1, 3), 15,
                        SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0, 0, ModItemTags.REPAIRS_CONDOM_ARMOR,
                        ModEquipmentAssetKeys.CONDOM);

        private static Map<EquipmentType, Integer> createDefenseMap(int bootsDefense, int leggingsDefense,
                        int chestplateDefense, int helmetDefense, int bodyDefense) {
                return Maps.newEnumMap(
                                Map.of(EquipmentType.BOOTS, bootsDefense, EquipmentType.LEGGINGS, leggingsDefense,
                                                EquipmentType.CHESTPLATE, chestplateDefense, EquipmentType.HELMET,
                                                helmetDefense, EquipmentType.BODY, bodyDefense));
        }
}
