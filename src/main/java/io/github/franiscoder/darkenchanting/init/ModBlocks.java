package io.github.franiscoder.darkenchanting.init;

import io.github.franiscoder.darkenchanting.DarkEnchanting;
import io.github.franiscoder.darkenchanting.block.DarkEnchanter;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;

public class ModBlocks {
    public static final DarkEnchanter DARK_ENCHANTER = new DarkEnchanter(FabricBlockSettings.of(Material.METAL).build());

    public void registerBlocks() {
        Registry.register(Registry.BLOCK, DarkEnchanter.ID, DARK_ENCHANTER);
        registerBlockItems();
    }

    private void registerBlockItems() {
        Registry.register(Registry.ITEM, DarkEnchanter.ID, new BlockItem(DARK_ENCHANTER, new Item.Settings().group(DarkEnchanting.DARK_ENCHANTING)));
    }
}
