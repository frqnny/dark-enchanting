package io.github.frqnny.darkenchanting.util;

import io.github.frqnny.darkenchanting.DarkEnchanting;
import io.github.frqnny.darkenchanting.config.ConfigEnchantment;
import io.github.frqnny.darkenchanting.config.DarkEnchantingConfig;
import it.unimi.dsi.fastutil.objects.Object2IntLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;

import java.util.Optional;

public class XPUtil {

    public static int getXpCostFromMap(Object2IntLinkedOpenHashMap<Enchantment> map, Object2IntLinkedOpenHashMap<Enchantment> stackEnchantments) {
        int cost = getLevelCostFromMap(map, stackEnchantments);
        cost *= 17; // turn it into XP; 17 xp in a level
        return cost;
    }

    public static int getLevelCostFromMap(Object2IntLinkedOpenHashMap<Enchantment> enchantmentsToApply, Object2IntLinkedOpenHashMap<Enchantment> stackEnchantments) {
        float level = 0;

        DarkEnchantingConfig config = DarkEnchanting.CONFIG;
        for (Object2IntMap.Entry<Enchantment> entry : enchantmentsToApply.object2IntEntrySet()) {
            Enchantment enchantment = entry.getKey();
            int power = entry.getIntValue();

            if (stackEnchantments.containsKey(enchantment) && stackEnchantments.getInt(enchantment) >= power) {
                continue;
            }
            float cost = getLevelCostFromEnchantment(enchantment, power, true);
            if (cost != -1000) {
                cost += (cost * cost) / 10; // increase the value as more cost, look up the equation  0.0005x^2  for help. this is needed because level xp also increases
                level += cost;
            }
        }

        //this checks for taking off enchantments
        for (Object2IntMap.Entry<Enchantment> entry : stackEnchantments.object2IntEntrySet()) {
            Enchantment enchantment = entry.getKey();
            int power = entry.getIntValue();

            if (!enchantmentsToApply.containsKey(enchantment)) {


                float cost = getLevelDiscounting(enchantment, power, false);
                if (cost != -1000) {
                    cost *= Math.min(1F, config.discountFactor);
                    level -= cost;
                }
            } else if (enchantmentsToApply.getInt(enchantment) < power) {
                int powerOnApply = enchantmentsToApply.getInt(enchantment);
                int powerToApply = power - powerOnApply;
                float cost = getLevelDiscounting(enchantment, powerToApply, false);
                if (cost != -1000) {
                    cost *= Math.min(1F, config.discountFactor);
                    level -= cost;
                }
            }
        }

        return Math.round(level);

    }

    public static int getRepairXpFromStack(ItemStack stack) {
        float cost = 0;
        if (stack.isDamaged()) {
            //Cost is initially XP amount
            cost += stack.getDamage();

            //TODO cost += stack.getEnchantments().size();
            //turn them into levels

            //tools don't scale up well, compared to armor
            if (stack.getItem() instanceof ToolItem) {
                cost *= 0.7F;
            }

            cost *= DarkEnchanting.CONFIG.repairFactor;
            return (int) Math.max(1, cost);

        }
        return (int) cost;
    }

    public static boolean applyEnchantXP(PlayerEntity player, Object2IntLinkedOpenHashMap<Enchantment> enchantmentsToApply, Object2IntLinkedOpenHashMap<Enchantment> enchantmentsOnStack, double discount) {
        int totalExperience = player.totalExperience;
        int xp = BookcaseUtils.applyDiscount(getXpCostFromMap(enchantmentsToApply, enchantmentsOnStack), discount);


        boolean canApplyXp = totalExperience >= xp || player.isCreative();
        if (canApplyXp) {
            player.addExperience(-xp);

        }
        return canApplyXp;
    }

    public static boolean applyRepairXP(PlayerEntity player, ItemStack stack, double discount) {
        int totalExperience = player.totalExperience;
        int xpCost = BookcaseUtils.applyDiscount(getRepairXpFromStack(stack), discount);
        boolean canApplyXp = totalExperience >= xpCost || player.isCreative();
        if (canApplyXp) {
            player.addExperience(-xpCost);

        }

        return canApplyXp;
    }


    public static float getLevelCostFromEnchantment(Enchantment enchantment, int power, boolean isCost) {
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

            cost *= isCost ? configEnchantment.costFactorEnchantment : configEnchantment.discountFactorEnchantment;

        }

        return cost;
    }

    public static float getLevelDiscounting(Enchantment enchantment, int power, boolean isCost) {
        float cost;
        if (enchantment.isTreasure()) {
            cost = 3F;
        } else if (enchantment.isCursed()) {
            cost = 2F;
        } else {
            cost = 1F;
        }

        float percentage = ((float) power) / enchantment.getMaxLevel();
        cost *= Math.min(1.0F, percentage);

        Optional<ConfigEnchantment> configEnchantmentOptional = ConfigEnchantment.getConfigEnchantmentFor(enchantment);
        if (configEnchantmentOptional.isPresent()) {
            ConfigEnchantment configEnchantment = configEnchantmentOptional.get();
            if (!configEnchantment.activated) {
                return -1000F;
            }

            cost *= isCost ? configEnchantment.costFactorEnchantment : configEnchantment.discountFactorEnchantment;

        }

        cost *= 17; // turn it into xp
        cost /= 2; // it can still be way too high so to avoid loops
        return cost;
    }
}

