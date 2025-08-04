package com.we1rdoyt.component;

import java.util.UUID;
import java.util.function.UnaryOperator;

import com.we1rdoyt.TheSexMod;

import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Uuids;

public class ModDataComponentTypes {
    public static final ComponentType<UUID> TARGET_ENTITY = register("target_entity", builer -> builer.codec(Uuids.CODEC));

    public static void init() {
    }

    private static <T> ComponentType<T> register(String id, UnaryOperator<ComponentType.Builder<T>> builderOperator) {
        return Registry.register(Registries.DATA_COMPONENT_TYPE, Identifier.of(TheSexMod.MOD_ID, id),
                ((ComponentType.Builder<T>) builderOperator.apply(ComponentType.builder())).build());
    }
}
