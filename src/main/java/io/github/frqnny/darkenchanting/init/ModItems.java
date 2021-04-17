package io.github.frqnny.darkenchanting.init;

import io.github.frqnny.darkenchanting.DarkEnchanting;
import io.github.frqnny.darkenchanting.block.DarkEnchanterBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;

public class ModItems {
    public static final Item DARK_ENCHANTER = new BlockItem(ModBlocks.DARK_ENCHANTER, new Item.Settings().group(DarkEnchanting.DARK_ENCHANTING));

    public static void init() {
        Registry.register(Registry.ITEM, DarkEnchanterBlock.ID, DARK_ENCHANTER);

    }

    public static void clientInit() {

    }
}
