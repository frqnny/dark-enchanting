package io.github.frqnny.darkenchanting.util;

import io.github.frqnny.darkenchanting.DarkEnchanting;
import io.github.frqnny.darkenchanting.config.DarkEnchantingConfig;
import it.unimi.dsi.fastutil.objects.Object2IntLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.PlayerEntity;

public class XPUtil {

    public static int getLevelCostFromMap(Object2IntLinkedOpenHashMap<Enchantment> map, Object2IntLinkedOpenHashMap<Enchantment> stackEnchantments) {
        float level = 0;

        DarkEnchantingConfig config = DarkEnchanting.config;
        for (Object2IntMap.Entry<Enchantment> entry : map.object2IntEntrySet()) {
            Enchantment enchantment = entry.getKey();
            int power = entry.getIntValue();

            if (stackEnchantments.containsKey(enchantment) && stackEnchantments.getInt(enchantment) >= power) {
                continue;
            }
            float cost = config.baseCost;


            cost *= Math.max((11.0F - enchantment.getRarity().getWeight())/config.weightDivisor, 1F);

            cost *= power;


            cost *= config.costFactor;

            if (enchantment.isCursed()) {

                cost *= config.curseFactor;
            } else if (enchantment.isTreasure()) {

                cost *= config.treasureFactor;
            }

            level += cost;
        }

        //this checks for taking off enchantments
        for (Object2IntMap.Entry<Enchantment> entry : stackEnchantments.object2IntEntrySet()) {
            Enchantment enchantment = entry.getKey();
            int power = entry.getIntValue();

            if (!map.containsKey(enchantment)) {
                float cost = config.baseCost;

                cost *= Math.max((11 - enchantment.getRarity().getWeight())/config.weightDivisor, 1);

                cost *= power;

                cost *= config.costFactor;

                if (enchantment.isCursed()) {

                    cost *= config.curseFactor;
                } else if (enchantment.isTreasure()) {

                    cost *= config.treasureFactor;
                }


                level -= cost;
            } else if (map.getInt(enchantment) < power) {
                int powerOnApply = map.getInt(enchantment);
                int powerToApply =  power - powerOnApply;
                float cost = config.baseCost;

                cost *= Math.max((11 - enchantment.getRarity().getWeight())/config.weightDivisor, 1);

                cost *= powerToApply;

                cost *= config.costFactor;

                if (enchantment.isCursed()) {

                    cost *= config.curseFactor;
                } else if (enchantment.isTreasure()) {

                    cost *= config.treasureFactor;
                }

                level -= cost;
            }
        }

        return Math.round(level);

    }

    public static boolean applyXp(PlayerEntity player, Object2IntLinkedOpenHashMap<Enchantment> enchantmentsToApply, Object2IntLinkedOpenHashMap<Enchantment> enchantmentsOnStack) {
        int currentPlayerLevel = player.experienceLevel;
        int level = getLevelCostFromMap(enchantmentsToApply, enchantmentsOnStack);

        boolean canApplyXp = currentPlayerLevel >= level || player.isCreative();
        if (canApplyXp) {
            player.addExperienceLevels(-level);

        }
        return canApplyXp;
    }
}
