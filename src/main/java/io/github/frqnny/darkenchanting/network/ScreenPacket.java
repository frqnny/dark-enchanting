package io.github.frqnny.darkenchanting.network;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.util.math.BlockPos;

public record ScreenPacket(BlockPos pos) {
    public static final PacketCodec<RegistryByteBuf, ScreenPacket> PACKET_CODEC = BlockPos.PACKET_CODEC.xmap(ScreenPacket::new, ScreenPacket::pos).cast();

}
