package io.github.frqnny.darkenchanting.init;

import io.github.frqnny.darkenchanting.DarkEnchanting;
import io.github.frqnny.darkenchanting.client.gui.DarkEnchanterGUI;
import io.github.frqnny.darkenchanting.util.BookcaseUtils;
import io.github.frqnny.darkenchanting.util.EnchantingUtils;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;

public class ModPackets {
    public static final Identifier APPLY_ENCHANTMENTS = DarkEnchanting.id("apply_enchantments");
    public static final Identifier APPLY_REPAIR = DarkEnchanting.id("apply_repair");

    public static void init() {
        ServerPlayNetworking.registerGlobalReceiver(APPLY_ENCHANTMENTS, (server, player, handler, buf, responseSender) -> {
            BlockPos pos = buf.readBlockPos();
            int size = buf.readVarInt();
            ServerPlayerEntity serverPlayer = handler.player;
            Object2IntMap<Enchantment> enchantmentsToApply = new Object2IntOpenHashMap<>(size);

            for (int i = 0; i < size; i++) {
                Identifier id = buf.readIdentifier();
                Enchantment enchantment = Registry.ENCHANTMENT.get(id);
                int level = buf.readVarInt();
                enchantmentsToApply.put(enchantment, level);
            }

            server.execute(() -> {
                ScreenHandler screen = serverPlayer.currentScreenHandler;

                if (screen instanceof DarkEnchanterGUI) {
                    ItemStack stack = ((DarkEnchanterGUI) screen).inv.getActualStack();
                    Object2IntMap<Enchantment> currentEnchantments = new Object2IntOpenHashMap<>(EnchantmentHelper.get(stack));

                    if (EnchantingUtils.applyEnchantXP(serverPlayer, enchantmentsToApply, currentEnchantments, BookcaseUtils.getDiscount(player.world, pos))) {
                        EnchantingUtils.set(enchantmentsToApply, stack);
                        player.incrementStat(Stats.ENCHANT_ITEM);
                        Criteria.ENCHANTED_ITEM.trigger(player, stack, 1);
                    }

                    player.closeHandledScreen();
                    server.getOverworld().playSound(null, pos, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.BLOCKS, 1.0f, server.getOverworld().random.nextFloat() * 0.2f + 0.9f);

                }
            });
        });

        ServerPlayNetworking.registerGlobalReceiver(APPLY_REPAIR, (server, player, handler, buf, responseSender) -> {
            BlockPos pos = buf.readBlockPos();

            server.execute(() -> {
                ScreenHandler screen = player.currentScreenHandler;
                if (screen instanceof DarkEnchanterGUI) {
                    ItemStack stack = ((DarkEnchanterGUI) screen).inv.getActualStack();
                    if (EnchantingUtils.applyRepairXP(player, stack, BookcaseUtils.getDiscount(player.world, pos))) {
                        stack.setDamage(0);
                    }
                }
                player.closeHandledScreen();
                server.getOverworld().playSound(null, pos, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.BLOCKS, 1.0f, server.getOverworld().random.nextFloat() * 0.2f + 0.9f);
            });
        });
    }
}
