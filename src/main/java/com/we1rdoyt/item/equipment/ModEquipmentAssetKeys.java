package com.we1rdoyt.item.equipment;

import com.we1rdoyt.TheSexMod;

import net.minecraft.item.equipment.EquipmentAsset;
import net.minecraft.item.equipment.EquipmentAssetKeys;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

public interface ModEquipmentAssetKeys extends EquipmentAssetKeys {
    RegistryKey<EquipmentAsset> CONDOM = register("condom");

    static RegistryKey<EquipmentAsset> register(String name) {
        return RegistryKey.of(REGISTRY_KEY, Identifier.of(TheSexMod.MOD_ID, name));
    }
}
