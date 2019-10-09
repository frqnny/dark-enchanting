package io.github.franiscoder.darkenchanting.api;

import net.minecraft.enchantment.Enchantment;

public class EnchDataContext {
    public int level;
    public Enchantment enchantment;

    public static EnchDataContext create(Enchantment enchantment, int level) {
        EnchDataContext d = new EnchDataContext();
        d.enchantment = enchantment;
        d.level = level;
        return d;
    }
}
