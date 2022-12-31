package io.github.frqnny.darkenchanting.init;

import io.github.frqnny.darkenchanting.DarkEnchanting;
import io.github.frqnny.darkenchanting.block.DarkConduitBlock;
import io.github.frqnny.darkenchanting.block.DarkEnchanterBlock;
import io.github.frqnny.darkenchanting.item.TableUpgradeItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.VerticallyAttachableBlockItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Rarity;
import net.minecraft.util.math.Direction;

public class ModItems {

    public static final Item DARK_ENCHANTER = new BlockItem(ModBlocks.DARK_ENCHANTER, new FabricItemSettings());
    public static final Item DARK_TORCH = new VerticallyAttachableBlockItem(ModBlocks.DARK_TORCH, ModBlocks.DARK_TORCH_WALL, new FabricItemSettings(), Direction.DOWN);
    public static final Item TABLE_UPGRADE = new TableUpgradeItem(new FabricItemSettings().maxCount(1).rarity(Rarity.EPIC));

    public static void init() {
        Registry.register(Registries.ITEM, DarkEnchanterBlock.ID, DARK_ENCHANTER);
        Registry.register(Registries.ITEM, DarkConduitBlock.ID, DARK_TORCH);
        Registry.register(Registries.ITEM, TableUpgradeItem.ID, TABLE_UPGRADE);
        ItemGroupEvents.modifyEntriesEvent(DarkEnchanting.DARK_ENCHANTING).register(ModItems::addDarkEnchantingBlocks);
        ItemGroupEvents.modifyEntriesEvent(DarkEnchanting.DARK_ENCHANTING).register(ModItems::addFunctionalBlocks);
        ItemGroupEvents.modifyEntriesEvent(DarkEnchanting.DARK_ENCHANTING).register(ModItems::addTools);
    }

    private static void addDarkEnchantingBlocks(FabricItemGroupEntries entries) {
        entries.add(DARK_ENCHANTER);
        entries.add(DARK_TORCH);
        entries.add(TABLE_UPGRADE);
    }

    private static void addFunctionalBlocks(FabricItemGroupEntries entries) {
        entries.addAfter(Items.SOUL_TORCH, DARK_TORCH);
        entries.addAfter(Items.ENCHANTING_TABLE, DARK_ENCHANTER);
    }

    private static void addTools(FabricItemGroupEntries entries) {
        entries.addBefore(Items.ENDER_EYE, TABLE_UPGRADE);
    }

}
