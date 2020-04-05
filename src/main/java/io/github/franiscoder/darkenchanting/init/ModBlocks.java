package io.github.franiscoder.darkenchanting.init;

import io.github.franiscoder.darkenchanting.DarkEnchanting;
import io.github.franiscoder.darkenchanting.block.DarkEnchanter;
import io.github.franiscoder.darkenchanting.blockentity.DEBlockEntity;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;

public class ModBlocks {
    public static final DarkEnchanter DARK_ENCHANTER = new DarkEnchanter(FabricBlockSettings.of(Material.METAL).build());

    public static BlockEntityType<DEBlockEntity> DE_BLOCK_ENTITY;

    public static void registerBlocks() {
        Registry.register(Registry.BLOCK, DarkEnchanter.ID, DARK_ENCHANTER);
        registerBlockItems();
        DE_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, DarkEnchanter.ID, BlockEntityType.Builder.create(DEBlockEntity::new, DARK_ENCHANTER).build(null));

    }

    private static void registerBlockItems() {
        Registry.register(Registry.ITEM, DarkEnchanter.ID, new BlockItem(DARK_ENCHANTER, new Item.Settings().group(DarkEnchanting.DARK_ENCHANTING)));
    }
}
