package io.github.frqnny.darkenchanting.util;

import io.github.frqnny.darkenchanting.init.ModTags;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.EnchantmentTags;
import net.minecraft.world.World;

public class TagUtils {

    public static boolean isEnchantmentDisabled(World world, Enchantment enchantment) {
        Registry<Enchantment> registry = world.getRegistryManager().get(RegistryKeys.ENCHANTMENT);
        var regEntry = registry.getEntry(enchantment);
        return regEntry.isIn(ModTags.DISABLED);
    }

    public static boolean isEnchantmentTreasure(World world, Enchantment enchantment) {
        Registry<Enchantment> registry = world.getRegistryManager().get(RegistryKeys.ENCHANTMENT);
        var regEntry = registry.getEntry(enchantment);
        return regEntry.isIn(EnchantmentTags.TREASURE);
    }

    public static boolean isEnchantmentCurse(World world, Enchantment enchantment) {
        Registry<Enchantment> registry = world.getRegistryManager().get(RegistryKeys.ENCHANTMENT);
        var regEntry = registry.getEntry(enchantment);
        return regEntry.isIn(EnchantmentTags.CURSE);
    }
}
