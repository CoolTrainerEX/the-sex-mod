package com.we1rdoyt;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record RidingPlayerJump() implements CustomPayload {
    public static final CustomPayload.Id<RidingPlayerJump> ID = new CustomPayload.Id<>(
            Identifier.of(TheSexMod.MOD_ID, "riding_player_jump"));
    public static final PacketCodec<RegistryByteBuf, RidingPlayerJump> CODEC = PacketCodec.unit(new RidingPlayerJump());

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
