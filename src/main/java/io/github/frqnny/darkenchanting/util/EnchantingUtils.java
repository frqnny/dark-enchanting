package io.github.frqnny.darkenchanting.util;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;

import java.util.Set;

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
        stack.set(DataComponentTypes.ENCHANTMENTS, ItemEnchantmentsComponent.DEFAULT);

        for (var entry : enchantments.object2IntEntrySet()) {
            Enchantment enchantment = entry.getKey();
            if (enchantment != null) {
                int level = entry.getIntValue();
                if (level <= 0 || level > enchantment.getMaxLevel()) {
                    continue;
                }
                stack.addEnchantment(enchantment, level);
            }
        }

    }

    public static Object2IntMap<Enchantment> getEnchantmentMap(ItemStack stack) {
        return convert(stack.getEnchantments().getEnchantmentsMap());
    }

    public static Object2IntMap<Enchantment> convert(Set<Object2IntMap.Entry<RegistryEntry<Enchantment>>> map) {
        Object2IntMap<Enchantment> enchantments = new Object2IntOpenHashMap<>();

        for (var entry : map) {
            int level = entry.getIntValue();

            var enchantmentEntry = entry.getKey();
            Enchantment enchantment = enchantmentEntry.value();

            enchantments.put(enchantment, level);
        }

        return enchantments;
    }

    public static Object2IntMap<RegistryEntry<Enchantment>> unconvert(Object2IntMap<Enchantment> map) {
        Object2IntMap<RegistryEntry<Enchantment>> enchantments = new Object2IntOpenHashMap<>();

        for (var entry : map.object2IntEntrySet()) {
            RegistryEntry<Enchantment> enchantmentEntry = entry.getKey().getRegistryEntry();
            int level = entry.getIntValue();

            enchantments.put(enchantmentEntry, level);
        }

        return enchantments;
    }
}

