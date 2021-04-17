package io.github.frqnny.darkenchanting.init;

import io.github.frqnny.darkenchanting.DarkEnchanting;
import io.github.frqnny.darkenchanting.client.gui.DarkEnchanterGUI;
import io.github.frqnny.darkenchanting.util.XPUtil;
import it.unimi.dsi.fastutil.objects.Object2IntLinkedOpenHashMap;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.Map;

public class ModPackets {
    public static final Identifier APPLY_SINGLE_ENCHANTMENT = DarkEnchanting.id("apply_enchantments");

    public static void init() {
        ServerPlayNetworking.registerGlobalReceiver(APPLY_SINGLE_ENCHANTMENT, ((server, player, handler, buf, responseSender) -> {
            int size = buf.readInt();
            ServerPlayerEntity serverPlayer = handler.player;
            Object2IntLinkedOpenHashMap<Enchantment> enchantmentsToApply = new Object2IntLinkedOpenHashMap<>(size);

            for (int i = 0; i < size; i++) {
                Identifier id = buf.readIdentifier();
                Enchantment enchantment = Registry.ENCHANTMENT.get(id);
                int level = buf.readInt();
                enchantmentsToApply.put(enchantment, level);
            }

            server.execute(() -> {

                ScreenHandler screen = serverPlayer.currentScreenHandler;

                if (screen instanceof DarkEnchanterGUI) {
                    ItemStack stack = ((DarkEnchanterGUI) screen).inv.getStack(0);
                    Map<Enchantment, Integer> currentEnchantments = EnchantmentHelper.get(stack);
                    if (XPUtil.applyXp(serverPlayer, enchantmentsToApply, new Object2IntLinkedOpenHashMap<>(currentEnchantments))) {
                        EnchantmentHelper.set(enchantmentsToApply, stack);
                    }

                    player.closeHandledScreen();

                }
            });
        }));
    }

    public static void clientInit() {

    }
}
