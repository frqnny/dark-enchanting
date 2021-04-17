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

            if (stackEnchantments.containsKey(enchantment) && stackEnchantments.getInt(enchantment) == power) {
                continue;
            }
            // Base cost is equal to roughly 2.5 levels of EXP.
            float cost = config.baseCost;

            // Cost is multiplied up to 10, based on rarity of the enchant.
            // Rarer the enchant, higher the cost.
            cost *= Math.max(11 - enchantment.getRarity().getWeight(), 1);

            // Linear cost increase based on level.
            cost *= power;

            // The cost factor is applied. Default is 1.5.

            cost *= config.costFactor;

            // Curses cost even more to apply
            if (enchantment.isCursed()) {

                cost *= config.curseFactor;
            } else if (enchantment.isTreasure()) {// Treasures cost more to apply

                cost *= config.treasureFactor;
            }

            level += cost;
        }

        //this checks for taking off enchantments
        for (Object2IntMap.Entry<Enchantment> entry : stackEnchantments.object2IntEntrySet()) {
            Enchantment enchantment = entry.getKey();
            int power = entry.getIntValue();

            if (!map.containsKey(enchantment)) {
                // Base cost is equal to roughly 2.5 levels of EXP.
                float cost = config.baseCost;

                // Cost is multiplied up to 10, based on rarity of the enchant.
                // Rarer the enchant, higher the cost.
                cost *= Math.max(11 - enchantment.getRarity().getWeight(), 1);

                // Linear cost increase based on level.
                cost *= power;

                // The cost factor is applied. Default is 1.5.

                cost *= config.costFactor;

                // Curses cost even more to apply
                if (enchantment.isCursed()) {

                    cost *= config.curseFactor;
                } else if (enchantment.isTreasure()) {  // Treasures cost more to apply

                    cost *= config.treasureFactor;
                }

                if (cost > 2) {
                    cost--;
                }
                level -= cost;
            } else if (map.getInt(enchantment) < power) {
                int powerOnApply = map.getInt(enchantment);
                int powerToApply =  power - powerOnApply;
                // Base cost is equal to roughly 2.5 levels of EXP.
                float cost = config.baseCost;

                // Cost is multiplied up to 10, based on rarity of the enchant.
                // Rarer the enchant, higher the cost.
                cost *= Math.max(11 - enchantment.getRarity().getWeight(), 1);

                // Linear cost increase based on level.
                cost *= powerToApply;

                // The cost factor is applied. Default is 1.5.

                cost *= config.costFactor;

                // Curses cost even more to apply
                if (enchantment.isCursed()) {

                    cost *= config.curseFactor;
                } else if (enchantment.isTreasure()) {  // Treasures cost more to apply

                    cost *= config.treasureFactor;
                }

                if (cost > 2) {
                    cost--;
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
