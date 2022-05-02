package io.github.frqnny.darkenchanting.util;

import io.github.frqnny.darkenchanting.DarkEnchanting;
import io.github.frqnny.darkenchanting.config.ConfigEnchantment;
import io.github.frqnny.darkenchanting.config.DarkEnchantingConfig;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;

import java.util.Optional;

public class CostUtils {

    public static int getExperienceCost(Object2IntMap<Enchantment> enchantmentsToApply, Object2IntMap<Enchantment> stackEnchantments) {
        int totalCost = 0;

        for (var entry : enchantmentsToApply.object2IntEntrySet()) {
            Enchantment enchantment = entry.getKey();
            int power = entry.getIntValue();
            int individualCost = 0;
            boolean takingOff = false;
            if (stackEnchantments.containsKey(enchantment)) {
                int powerOnStack = stackEnchantments.getInt(enchantment);
                int powerToApply = power - powerOnStack; //positive if putting on, neg if taking off some/all, 0 if the ench wasn't touched (and should behave as such)
                if (powerToApply > 0) { //putting on more, then powerToApply to get some of that tasty discount in there
                    individualCost = getEnchantmentCost(enchantment, powerToApply, false);
                } else if (powerToApply < 0) { //taking off some/all.
                    takingOff = true;
                    individualCost = getEnchantmentCost(enchantment, Math.absExact(powerToApply), true);
                }
            } else {
                individualCost = getEnchantmentCost(enchantment, power, false);
            }

            if (individualCost != -1000) {
                if (takingOff) {
                    individualCost *= DarkEnchanting.CONFIG.receiveFactor;
                    totalCost -= individualCost;
                } else {
                    totalCost += individualCost;
                }
            }
        }

        return totalCost;
    }

    public static int getEnchantmentCost(Enchantment enchantment, int power, boolean takingOff) {
        DarkEnchantingConfig config = DarkEnchanting.CONFIG;

        int cost = config.baseExperienceCost;
        cost *= Math.max((11.0F - enchantment.getRarity().getWeight()) * config.weightFactor, 1F);

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
}
