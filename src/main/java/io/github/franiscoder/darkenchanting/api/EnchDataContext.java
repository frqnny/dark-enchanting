package io.github.franiscoder.darkenchanting.api;

import net.minecraft.enchantment.Enchantment;

public class EnchDataContext {


    public int level;


    public  Enchantment enchantment;

    public EnchDataContext(Enchantment enchantment, int level) {
        this.enchantment = enchantment;
        this.level = level;
    }
    public Enchantment getEnchantment() {
        return enchantment;
    }

    public int getLevel() {
        return level;
    }

}
