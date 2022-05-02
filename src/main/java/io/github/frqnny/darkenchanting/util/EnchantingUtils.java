package io.github.frqnny.darkenchanting.util;

import io.github.frqnny.darkenchanting.DarkEnchanting;
import it.unimi.dsi.fastutil.objects.Object2IntLinkedOpenHashMap;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtList;

import java.util.Map;

public class EnchantingUtils {
    public static boolean applyEnchantXP(PlayerEntity player, Object2IntLinkedOpenHashMap<Enchantment> enchantmentsToApply, Object2IntLinkedOpenHashMap<Enchantment> enchantmentsOnStack, double discount) {
        int totalExperience = PlayerUtils.syncAndGetTotalExperience(player);
        int xpCost = BookcaseUtils.applyDiscount(CostUtils.getXpCost(enchantmentsToApply, enchantmentsOnStack), discount);

        boolean canApplyXp = totalExperience >= xpCost || player.isCreative();
        if (canApplyXp) {
            PlayerUtils.modifyExperience(player, -xpCost);
        }

        return canApplyXp;
    }

    public static boolean applyRepairXP(PlayerEntity player, ItemStack stack, double discount) {
        int totalExperience = PlayerUtils.syncAndGetTotalExperience(player);
        int xpCost = BookcaseUtils.applyDiscount(CostUtils.getRepairCost(stack), discount);

        boolean canApplyXp = totalExperience >= xpCost || player.isCreative();
        if (canApplyXp) {
            PlayerUtils.modifyExperience(player, -xpCost);
        }

        return canApplyXp;
    }

    public static void set(Map<Enchantment, Integer> enchantments, ItemStack stack) {
        NbtList nbtList = new NbtList();

        for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
            Enchantment enchantment = entry.getKey();
            if (enchantment != null) {
                int i = entry.getValue();

                boolean shouldCheckMaxLevelEnch = DarkEnchanting.CONFIG.shouldRejectEnchantmentAttemptsAboveMaxValue;
                if (i > 0 && (i <= enchantment.getMaxLevel() || shouldCheckMaxLevelEnch)) {
                    nbtList.add(EnchantmentHelper.createNbt(EnchantmentHelper.getEnchantmentId(enchantment), i));
                }

            }
        }

        if (nbtList.isEmpty()) {
            stack.removeSubNbt("Enchantments");
        } else if (!stack.isOf(Items.ENCHANTED_BOOK)) {
            stack.setSubNbt("Enchantments", nbtList);
        }

    }
}

