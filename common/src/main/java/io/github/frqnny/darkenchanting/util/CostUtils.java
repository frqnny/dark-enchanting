package io.github.frqnny.darkenchanting.util;

import io.github.frqnny.darkenchanting.DarkEnchanting;
import io.github.frqnny.darkenchanting.config.ConfigEnchantment;
import io.github.frqnny.darkenchanting.config.DarkEnchantingConfig;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.World;

import java.util.Optional;

public class CostUtils {

    public static int getExperienceCost(World world, Object2IntMap<RegistryEntry<Enchantment>> enchantmentsToApply, Object2IntMap<RegistryEntry<Enchantment>> stackEnchantments) {
        float totalCost = 0;

        for (var entry : enchantmentsToApply.object2IntEntrySet()) {
            RegistryEntry<Enchantment> registryEntry = entry.getKey();
            Enchantment enchantment = registryEntry.value();
            int power = entry.getIntValue();
            int powerOnStack = stackEnchantments.getInt(registryEntry);

            int netLevelChange = power - powerOnStack; //positive if putting on levels, neg if taking off levels, 0 if same (no effect)
            if (netLevelChange == 0) {
                continue;
            }
            boolean takingOff = netLevelChange < 0; // the enchantment's levels are being reduced
            float individualCost = getEnchantmentCost(world, enchantment, Math.absExact(netLevelChange), takingOff);

            if (individualCost != Integer.MIN_VALUE) {

                if (takingOff) {
                    individualCost *= DarkEnchanting.CONFIG.receiveFactor;
                    if (DarkEnchanting.CONFIG.takingOffLevelsCostsXP) {
                        totalCost += individualCost; // implement the cost when taking off levels
                    } else {
                        totalCost -= individualCost; // allow to receive xp if you turn it off
                    }
                } else {
                    totalCost += individualCost;

                }

            }
        }

        return Math.round(totalCost);
    }

    public static float getEnchantmentCost(World world, Enchantment enchantment, int power, boolean takingOff) {
        DarkEnchantingConfig config = DarkEnchanting.CONFIG;

        float cost = config.baseExperienceCost;
        cost *= Math.max((11.0F - enchantment.getWeight()) * config.weightFactor, 1F);

        cost *= power;

        cost *= config.costFactor;

        if (TagUtils.isEnchantmentCurse(world, enchantment)) {
            if (config.curseEnchantmentsHaveSpecialHandling && takingOff) {
                cost /= config.receiveFactor;
            }
            cost *= config.curseFactor;

        } else if (TagUtils.isEnchantmentTreasure(world, enchantment)) {

            cost *= config.treasureFactor;
        }

        Optional<ConfigEnchantment> configEnchantmentOptional = ConfigEnchantment.getConfigEnchantmentFor(world, enchantment);
        if (configEnchantmentOptional.isPresent()) {
            ConfigEnchantment configEnchantment = configEnchantmentOptional.get();
            if (!configEnchantment.activated) {
                return Integer.MIN_VALUE;
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
        boolean damaged = stack.isDamaged();
        if (damaged) {
            //Cost is initially XP amount
            cost += stack.getDamage();
            cost *= DarkEnchanting.CONFIG.repairFactor;
        }
        return damaged ? Math.round(Math.max(1.0F, cost)) : 0;
    }
}
