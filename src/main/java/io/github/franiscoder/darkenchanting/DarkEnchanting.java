package io.github.franiscoder.darkenchanting;

import io.github.franiscoder.darkenchanting.init.BlockEntities;
import io.github.franiscoder.darkenchanting.init.ModBlocks;
import io.github.franiscoder.darkenchanting.init.ModItems;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

public class DarkEnchanting implements ModInitializer {
    public static String MODID = "dark-enchanting";
    ModItems ItemRegistry = new ModItems();
    ModBlocks BlockRegistry = new ModBlocks();
    BlockEntities BlockEntities = new BlockEntities();

    public static final ItemGroup DARK_ENCHANTING = FabricItemGroupBuilder.create(
            new Identifier(MODID, "dark_enchanting_group"))
            .icon(() -> new ItemStack(ModBlocks.DARK_ENCHANTER))
            .appendItems(stacks ->
            {
                stacks.add(new ItemStack(ModItems.ENCHANTED_GEM));
                stacks.add(new ItemStack(ModBlocks.DARK_ENCHANTER));
            })
            .build();

    @Override
    public void onInitialize() {
        ItemRegistry.registerItems();
        BlockRegistry.registerBlocks();
        BlockEntities.registerBlockEntities();

    }
}
