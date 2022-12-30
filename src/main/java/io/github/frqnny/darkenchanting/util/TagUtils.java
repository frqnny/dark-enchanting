package io.github.frqnny.darkenchanting.util;

import io.github.frqnny.darkenchanting.init.ModTags;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.World;

public class TagUtils {

    public static boolean isEnchantmentDisabled(World world, Enchantment enchantment) {
        Registry<Enchantment> registry = world.getRegistryManager().get(RegistryKeys.ENCHANTMENT);
        RegistryEntry.Reference<Enchantment> regEntry = registry.entryOf(RegistryKey.of(RegistryKeys.ENCHANTMENT, Registries.ENCHANTMENT.getId(enchantment)));
        return regEntry.isIn(ModTags.DISABLED);
    }
}
