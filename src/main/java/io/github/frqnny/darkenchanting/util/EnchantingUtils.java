package io.github.frqnny.darkenchanting.util;

import io.github.frqnny.darkenchanting.DarkEnchanting;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtList;

public class EnchantingUtils {
    public static boolean applyEnchantXP(PlayerEntity player, Object2IntMap<Enchantment> enchantmentsToApply, Object2IntMap<Enchantment> enchantmentsOnStack, double discount) {
        int totalExperience = PlayerUtils.syncAndGetTotalExperience(player);
        int xpCost = BookcaseUtils.applyDiscount(CostUtils.getExperienceCost(enchantmentsToApply, enchantmentsOnStack), discount);

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

    public static void set(Object2IntMap<Enchantment> enchantments, ItemStack stack) {
        NbtList nbtList = new NbtList();

        for (var entry : enchantments.object2IntEntrySet()) {
            Enchantment enchantment = entry.getKey();
            if (enchantment != null) {
                int i = entry.getIntValue();

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

