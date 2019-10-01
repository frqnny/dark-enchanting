package io.github.franiscoder.darkenchanting.init;

import io.github.franiscoder.darkenchanting.DarkEnchanting;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModItems {
    public static final Item ENCHANTED_GEM = new Item(new Item.Settings().group(DarkEnchanting.DARK_ENCHANTING));

    public void registerItems() {
        Registry.register(Registry.ITEM, new Identifier("dark-enchanting:enchanted_gem"), ENCHANTED_GEM);
    }
}
