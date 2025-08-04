package com.we1rdoyt;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record PlayerJump() implements CustomPayload {
    public static final CustomPayload.Id<PlayerJump> ID = new CustomPayload.Id<>(
            Identifier.of(TheSexMod.MOD_ID, "player_jump"));
    public static final PacketCodec<RegistryByteBuf, PlayerJump> CODEC = PacketCodec.unit(new PlayerJump());

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
