package io.github.frqnny.darkenchanting.network;

import io.github.frqnny.darkenchanting.DarkEnchanting;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public record EnchantPacket(BlockPos pos,
                            Object2IntMap<RegistryEntry<Enchantment>> enchantments) implements CustomPayload {
    public static final Identifier APPLY_ENCHANTMENTS = DarkEnchanting.id("apply_enchantments");
    public static final CustomPayload.Id<EnchantPacket> PACKET_ID = new CustomPayload.Id<>(APPLY_ENCHANTMENTS);

    private static final PacketCodec<RegistryByteBuf, Object2IntMap<RegistryEntry<Enchantment>>> ENCHANTMENTS = PacketCodecs.map(Object2IntOpenHashMap::new, PacketCodecs.registryEntry(RegistryKeys.ENCHANTMENT), PacketCodecs.VAR_INT);
    public static final PacketCodec<RegistryByteBuf, EnchantPacket> PACKET_CODEC = PacketCodec.tuple(
            BlockPos.PACKET_CODEC, EnchantPacket::pos,
            ENCHANTMENTS, EnchantPacket::enchantments,
            EnchantPacket::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }
}
