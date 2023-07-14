package io.github.frqnny.darkenchanting.init;

import io.github.frqnny.darkenchanting.block.DarkConduitBlock;
import io.github.frqnny.darkenchanting.block.DarkEnchanterBlock;
import io.github.frqnny.darkenchanting.item.TableUpgradeItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Rarity;
import net.minecraft.util.math.Direction;

public class ModItems {
    public static ItemGroup DARK_ENCHANTING;

    public static final Item DARK_ENCHANTER = new BlockItem(ModBlocks.DARK_ENCHANTER, new FabricItemSettings());
    public static final Item DARK_TORCH = new VerticallyAttachableBlockItem(ModBlocks.DARK_TORCH, ModBlocks.DARK_TORCH_WALL, new FabricItemSettings(), Direction.DOWN);
    public static final Item TABLE_UPGRADE = new TableUpgradeItem(new FabricItemSettings().maxCount(1).rarity(Rarity.EPIC));

    public static void init() {
        Registry.register(Registries.ITEM, DarkEnchanterBlock.ID, DARK_ENCHANTER);
        Registry.register(Registries.ITEM, DarkConduitBlock.ID, DARK_TORCH);
        Registry.register(Registries.ITEM, TableUpgradeItem.ID, TABLE_UPGRADE);

        DARK_ENCHANTING = FabricItemGroup.builder()
                .icon(() -> new ItemStack(ModBlocks.DARK_ENCHANTER))
                .entries((ctx, entries) -> {
                    entries.add(DARK_ENCHANTER);
                    entries.add(DARK_TORCH);
                    entries.add(TABLE_UPGRADE);
                })
                .displayName(Text.translatable("itemGroup.dark-enchanting.dark_enchanting_group"))
                .build();
    }



}
