package io.github.frqnny.darkenchanting.network;

import io.github.frqnny.darkenchanting.DarkEnchanting;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public record RepairPacket(BlockPos pos) implements CustomPayload {
    public static final Identifier APPLY_REPAIR = DarkEnchanting.id("apply_repair");
    public static final PacketCodec<RegistryByteBuf, RepairPacket> PACKET_CODEC = BlockPos.PACKET_CODEC.xmap(RepairPacket::new, RepairPacket::pos).cast();
    public static final CustomPayload.Id<RepairPacket> PACKET_ID = new CustomPayload.Id<>(APPLY_REPAIR);

    @Override
    public Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }
}
