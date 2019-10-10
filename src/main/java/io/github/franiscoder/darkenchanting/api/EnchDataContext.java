package io.github.franiscoder.darkenchanting.api;

import net.minecraft.enchantment.Enchantment;

public class EnchDataContext {
    public static int level;
    public static Enchantment enchantment;

    public static EnchDataContext create(Enchantment enchantment, int level) {
        EnchDataContext d = new EnchDataContext();
        d.enchantment = enchantment;
        d.level = level;
        return d;
    }
}
