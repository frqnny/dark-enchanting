package io.github.frqnny.darkenchanting.init;

import dev.architectury.registry.registries.Registrar;
import io.github.frqnny.darkenchanting.DarkEnchanting;
import io.github.frqnny.darkenchanting.block.DarkConduitBlock;
import io.github.frqnny.darkenchanting.block.DarkEnchanterBlock;
import io.github.frqnny.darkenchanting.item.TableUpgradeItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.VerticallyAttachableBlockItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Rarity;
import net.minecraft.util.math.Direction;

public class ModItems {
    public static ItemGroup DARK_ENCHANTING;


    public static void init() {
        Registrar<Item> items = DarkEnchanting.MANAGER.get().get(RegistryKeys.ITEM);
        items.register(DarkEnchanterBlock.ID, () -> new BlockItem(ModBlocks.DARK_ENCHANTER.get(), new Item.Settings()));
        items.register(DarkConduitBlock.ID, () -> new VerticallyAttachableBlockItem(ModBlocks.DARK_TORCH.get(), ModBlocks.DARK_TORCH_WALL.get(), new Item.Settings(), Direction.DOWN));
        items.register(TableUpgradeItem.ID, () -> new TableUpgradeItem(new Item.Settings().maxCount(1).rarity(Rarity.EPIC)));
    }
}
