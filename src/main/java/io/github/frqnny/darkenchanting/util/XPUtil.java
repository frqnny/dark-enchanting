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

    public static int getLevelCostFromMap(Object2IntLinkedOpenHashMap<Enchantment> map, Object2IntLinkedOpenHashMap<Enchantment> stackEnchantments) {
        float level = 0;

        DarkEnchantingConfig config = DarkEnchanting.CONFIG;
        for (Object2IntMap.Entry<Enchantment> entry : map.object2IntEntrySet()) {
            Enchantment enchantment = entry.getKey();
            int power = entry.getIntValue();

            if (stackEnchantments.containsKey(enchantment) && stackEnchantments.getInt(enchantment) >= power) {
                continue;
            }
            float cost = getLevelCostFromEnchantment(enchantment, power, true);
            if (cost != -1000) {
                level += cost;
            }
        }

        //this checks for taking off enchantments
        for (Object2IntMap.Entry<Enchantment> entry : stackEnchantments.object2IntEntrySet()) {
            Enchantment enchantment = entry.getKey();
            int power = entry.getIntValue();

            if (!map.containsKey(enchantment)) {
                float cost = getLevelCostFromEnchantment(enchantment, power, false);
                if (cost != -1000) {
                    cost *= Math.min(1, config.discountFactor);
                    level -= cost;
                }
            } else if (map.getInt(enchantment) < power) {
                int powerOnApply = map.getInt(enchantment);
                int powerToApply = power - powerOnApply;
                float cost = getLevelCostFromEnchantment(enchantment, powerToApply, false);
                if (cost != -1000) {
                    cost *= Math.min(1, config.discountFactor);
                    level -= cost;
                }
            }
        }

        return Math.round(level);

    }

    public static int getRepairCostFromItemStack(ItemStack stack) {
        float cost = 0;
        if (stack.isDamaged()) {
            //Cost is initially XP amount
            cost += stack.getDamage();

            //TODO cost += stack.getEnchantments().size();
            //turn them into levels
            cost /= 17;


            //tools don't scale up well, compared to armor
            if (stack.getItem() instanceof ToolItem) {
                cost *= 0.4;
            }

            cost *= DarkEnchanting.CONFIG.repairFactor;
            return (int) Math.max(1, cost);

        }
        return (int) cost;
    }

    public static boolean applyEnchantXP(PlayerEntity player, Object2IntLinkedOpenHashMap<Enchantment> enchantmentsToApply, Object2IntLinkedOpenHashMap<Enchantment> enchantmentsOnStack, double discount) {
        int currentPlayerLevel = player.experienceLevel;
        int level = BookcaseUtils.applyDiscount(getLevelCostFromMap(enchantmentsToApply, enchantmentsOnStack), discount);


        boolean canApplyXp = currentPlayerLevel >= level || player.isCreative();
        if (canApplyXp) {
            player.addExperienceLevels(-level);

        }
        return canApplyXp;
    }

    public static boolean applyRepairXP(PlayerEntity player, ItemStack stack, double discount) {
        int currentPlayerLevel = player.experienceLevel;
        int cost = BookcaseUtils.applyDiscount(getRepairCostFromItemStack(stack), discount);
        boolean canApplyXp = currentPlayerLevel >= cost || player.isCreative();
        if (canApplyXp) {
            player.addExperienceLevels(-cost);

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

            cost *= isCost? configEnchantment.costFactorEnchantment : configEnchantment.discountFactorEnchantment;

        }

        return cost;
    }
}

