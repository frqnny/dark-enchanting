package io.github.frqnny.darkenchanting.init;

import dev.architectury.networking.NetworkManager;
import io.github.frqnny.darkenchanting.network.EnchantPacket;
import io.github.frqnny.darkenchanting.network.RepairPacket;
import io.github.frqnny.darkenchanting.screen.DarkEnchanterScreenHandler;
import io.github.frqnny.darkenchanting.util.BookcaseUtils;
import io.github.frqnny.darkenchanting.util.EnchantingUtils;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.math.BlockPos;

public class ModPackets {


    public static void init() {
        NetworkManager.registerReceiver(NetworkManager.Side.C2S, EnchantPacket.PACKET_ID, EnchantPacket.PACKET_CODEC, (payload, context) -> {
            BlockPos pos = payload.pos();
            var player = context.getPlayer();

            context.getPlayer().getServer().execute(() -> {

                ScreenHandler screen = player.currentScreenHandler;

                if (screen instanceof DarkEnchanterScreenHandler holder) {
                    ItemStack stack = holder.getActualStack();
                    Object2IntMap<RegistryEntry<Enchantment>> currentEnchantments = EnchantingUtils.getEnchantmentMap(stack);

                    if (EnchantingUtils.applyEnchantXP(player, payload.enchantments(), currentEnchantments, BookcaseUtils.getDiscount(player.getWorld(), pos))) {
                        EnchantingUtils.set(payload.enchantments(), stack);
                        player.incrementStat(Stats.ENCHANT_ITEM);
                        Criteria.ENCHANTED_ITEM.trigger((ServerPlayerEntity) player, stack, 1);
                    }

                    player.closeHandledScreen();
                    player.getServer().getOverworld().playSound(null, pos, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.BLOCKS, 1.0f, player.getServer().getOverworld().random.nextFloat() * 0.2f + 0.9f);

                }
            });
        });

        NetworkManager.registerReceiver(NetworkManager.Side.C2S, RepairPacket.PACKET_ID, RepairPacket.PACKET_CODEC, (payload, context) -> {
            BlockPos pos = payload.pos();
            var player = context.getPlayer();

            player.getServer().execute(() -> {
                ScreenHandler screen = player.currentScreenHandler;
                if (screen instanceof DarkEnchanterScreenHandler holder) {
                    ItemStack stack = holder.getActualStack();
                    if (EnchantingUtils.applyRepairXP(player, stack, BookcaseUtils.getDiscount(player.getEntityWorld(), pos))) {
                        stack.setDamage(0);
                    }
                }
                player.closeHandledScreen();
                player.getServer().getOverworld().playSound(null, pos, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.BLOCKS, 1.0f, player.getServer().getOverworld().random.nextFloat() * 0.2f + 0.9f);
            });
        });
    }
}
