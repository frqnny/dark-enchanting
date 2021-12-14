package io.github.frqnny.darkenchanting.util;

import io.github.frqnny.darkenchanting.DarkEnchanting;
import io.github.frqnny.darkenchanting.config.ConfigEnchantment;
import io.github.frqnny.darkenchanting.config.DarkEnchantingConfig;
import it.unimi.dsi.fastutil.objects.Object2IntLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ToolItem;
import net.minecraft.nbt.NbtList;

import java.util.Map;
import java.util.Optional;

public class EnchHelp {

    public static int getXpCost(Object2IntMap<Enchantment> map, Object2IntMap<Enchantment> stackEnchantments) {
        int cost = getLevelCost(map, stackEnchantments);
        cost *= 17; // turn it into XP; 17 xp in a level
        return cost;
    }

    public static int getLevelCost(Object2IntMap<Enchantment> enchantmentsToApply, Object2IntMap<Enchantment> stackEnchantments) {
        float level = 0;

        for (Object2IntMap.Entry<Enchantment> entry : enchantmentsToApply.object2IntEntrySet()) {
            Enchantment enchantment = entry.getKey();
            int power = entry.getIntValue();
            float cost = 0F;
            boolean takingOff = false;
            if (stackEnchantments.containsKey(enchantment)) {
                int powerOnStack = stackEnchantments.getInt(enchantment);
                int powerToApply = power - powerOnStack; //positive if putting on, neg if taking off some/all, 0 if the ench wasn't touched (and should behave as such)
                if (powerToApply > 0) { //putting on more, then powerToApply to get some of that tasty discount in there
                    cost = getEnchantmentCost(enchantment, powerToApply, takingOff);
                } else if (powerToApply < 0) { //taking off some/all.
                    takingOff = true;
                    cost = getEnchantmentCost(enchantment, Math.absExact(powerToApply), takingOff);
                }
            } else {
                cost = getEnchantmentCost(enchantment, power, takingOff);
            }

            if (cost != -1000) {
                cost += (cost * cost) / 10; // increase the value as more cost, look up the equation  0.1x^2  for help. this is needed because level xp also increases
                if (takingOff) {
                    cost *= DarkEnchanting.CONFIG.receiveFactor;
                    level -= cost;
                } else {
                    level += cost;
                }
            }

        }

        return Math.round(level);
    }

    //in levels
    public static float getEnchantmentCost(Enchantment enchantment, int power, boolean takingOff) {
        DarkEnchantingConfig config = DarkEnchanting.CONFIG;
        float cost = config.baseCost;

        cost *= Math.max((11.0F - enchantment.getRarity().getWeight()) / config.weightDivisor, 1F);

        cost *= power;

        cost *= config.costFactor;

        if (enchantment.isCursed()) {

            cost *= config.curseFactor;
        } else if (enchantment.isTreasure()) {

            cost *= config.treasureFactor;
        }

        Optional<ConfigEnchantment> configEnchantmentOptional = ConfigEnchantment.getConfigEnchantmentFor(enchantment);
        if (configEnchantmentOptional.isPresent()) {
            ConfigEnchantment configEnchantment = configEnchantmentOptional.get();
            if (!configEnchantment.activated) {
                return -1000;
            }

            cost *= configEnchantment.personalFactor;
            if (takingOff) {
                cost *= configEnchantment.personalReceiveFactor;
            }

        }

        return cost;
    }

    public static int getRepairCost(ItemStack stack) {
        float cost = 0;
        if (stack.isDamaged()) {
            //Cost is initially XP amount
            cost += stack.getDamage();

            //tools don't scale up well, compared to armor
            if (stack.getItem() instanceof ToolItem) {
                cost *= 0.7F;
            }

            cost *= DarkEnchanting.CONFIG.repairFactor;
            return (int) Math.ceil(Math.max(1D, cost));
        }
        return 0;
    }

    public static boolean applyEnchantXP(PlayerEntity player, Object2IntLinkedOpenHashMap<Enchantment> enchantmentsToApply, Object2IntLinkedOpenHashMap<Enchantment> enchantmentsOnStack, double discount) {
        int totalExperience = player.totalExperience;
        int xp = BookcaseUtils.applyDiscount(getXpCost(enchantmentsToApply, enchantmentsOnStack), discount);

        boolean canApplyXp = totalExperience >= xp || player.isCreative();
        if (canApplyXp) {
            player.addExperience(-xp);

        }
        return canApplyXp;
    }

    public static boolean applyRepairXP(PlayerEntity player, ItemStack stack, double discount) {
        int totalExperience = player.totalExperience;
        int xpCost = BookcaseUtils.applyDiscount(getRepairCost(stack), discount);
        boolean canApplyXp = totalExperience >= xpCost || player.isCreative();
        if (canApplyXp) {
            player.addExperience(-xpCost);

        }

        return canApplyXp;
    }

    public static void set(Map<Enchantment, Integer> enchantments, ItemStack stack) {
        NbtList nbtList = new NbtList();

        for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
            Enchantment enchantment = entry.getKey();
            if (enchantment != null) {
                int i = entry.getValue();

                if (i > 0 && i <= enchantment.getMaxLevel()) {
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

