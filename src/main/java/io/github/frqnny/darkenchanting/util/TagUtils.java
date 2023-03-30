package io.github.frqnny.darkenchanting.util;

import io.github.frqnny.darkenchanting.init.ModTags;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

public class TagUtils {

    public static boolean isEnchantmentDisabled(World world, Enchantment enchantment) {
        Registry<Enchantment> registry = world.getRegistryManager().get(Registry.ENCHANTMENT_KEY);
        RegistryEntry<Enchantment> regEntry = registry.entryOf(RegistryKey.of(Registry.ENCHANTMENT_KEY, Registry.ENCHANTMENT.getId(enchantment)));
        return regEntry.isIn(ModTags.DISABLED);
    }
}
