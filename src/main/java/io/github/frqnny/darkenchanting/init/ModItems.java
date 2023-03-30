package io.github.frqnny.darkenchanting.init;

import io.github.frqnny.darkenchanting.DarkEnchanting;
import io.github.frqnny.darkenchanting.block.DarkConduitBlock;
import io.github.frqnny.darkenchanting.block.DarkEnchanterBlock;
import io.github.frqnny.darkenchanting.item.TableUpgradeItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.WallStandingBlockItem;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;

public class ModItems {

    public static final Item DARK_ENCHANTER = new BlockItem(ModBlocks.DARK_ENCHANTER, new FabricItemSettings().group(DarkEnchanting.DARK_ENCHANTING));
    public static final Item DARK_TORCH = new WallStandingBlockItem(ModBlocks.DARK_TORCH, ModBlocks.DARK_TORCH_WALL, new FabricItemSettings().group(DarkEnchanting.DARK_ENCHANTING));
    public static final Item TABLE_UPGRADE = new TableUpgradeItem(new FabricItemSettings().group(DarkEnchanting.DARK_ENCHANTING).maxCount(1).rarity(Rarity.EPIC));

    public static void init() {
        Registry.register(Registry.ITEM, DarkEnchanterBlock.ID, DARK_ENCHANTER);
        Registry.register(Registry.ITEM, DarkConduitBlock.ID, DARK_TORCH);
        Registry.register(Registry.ITEM, TableUpgradeItem.ID, TABLE_UPGRADE);
    }


}
