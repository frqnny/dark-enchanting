package io.github.frqnny.darkenchanting.util;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;

public class EnchantingUtils {
    public static boolean applyEnchantXP(PlayerEntity player, Object2IntMap<RegistryEntry<Enchantment>> enchantmentsToApply, Object2IntMap<RegistryEntry<Enchantment>> enchantmentsOnStack, double discount) {
        int totalExperience = PlayerUtils.syncAndGetTotalExperience(player);
        int xpCost = BookcaseUtils.applyDiscount(CostUtils.getExperienceCost(player.getWorld(), enchantmentsToApply, enchantmentsOnStack), discount);

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

    public static void set(Object2IntMap<RegistryEntry<Enchantment>> enchantments, ItemStack stack) {
        stack.set(DataComponentTypes.ENCHANTMENTS, ItemEnchantmentsComponent.DEFAULT);

        for (var entry : enchantments.object2IntEntrySet()) {
            RegistryEntry<Enchantment> enchantment = entry.getKey();
            if (enchantment != null) {
                int level = entry.getIntValue();
                if (level <= 0 || level > enchantment.value().getMaxLevel()) {
                    continue;
                }
                stack.addEnchantment(enchantment, level);
            }
        }

    }

    public static Object2IntMap<RegistryEntry<Enchantment>> getEnchantmentMap(ItemStack stack) {
        Object2IntMap<RegistryEntry<Enchantment>> enchantments = new Object2IntOpenHashMap<>();
        var stackEnchantments = stack.getEnchantments().getEnchantmentEntries();
        for (var entry : stackEnchantments) {
            enchantments.put(entry.getKey(), entry.getIntValue());
        }
        return enchantments;
    }


}

