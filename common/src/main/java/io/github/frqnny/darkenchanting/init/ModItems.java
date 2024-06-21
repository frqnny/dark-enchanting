package io.github.frqnny.darkenchanting.init;

import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrySupplier;
import io.github.frqnny.darkenchanting.DarkEnchanting;
import io.github.frqnny.darkenchanting.block.DarkConduitBlock;
import io.github.frqnny.darkenchanting.block.DarkEnchanterBlock;
import io.github.frqnny.darkenchanting.item.TableUpgradeItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.VerticallyAttachableBlockItem;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Rarity;
import net.minecraft.util.math.Direction;

public class ModItems {
    public static RegistrySupplier<ItemGroup> GROUP;
    public static RegistrySupplier<Item> DARK_ENCHANTER_BLOCK;

    public static void init() {
        Registrar<ItemGroup> itemGroups = DarkEnchanting.MANAGER.get().get(RegistryKeys.ITEM_GROUP);
        GROUP = itemGroups.register(DarkEnchanting.id("dark_enchanting"), () -> CreativeTabRegistry.create(
                Text.translatable("itemGroup.darkenchanting.dark_enchanting_group"),
                () -> DARK_ENCHANTER_BLOCK.get().getDefaultStack()
        ));
        Registrar<Item> items = DarkEnchanting.MANAGER.get().get(RegistryKeys.ITEM);
        DARK_ENCHANTER_BLOCK = items.register(DarkEnchanterBlock.ID, () -> new BlockItem(ModBlocks.DARK_ENCHANTER.get(), new Item.Settings().arch$tab(GROUP)));
        var darkTorch = items.register(DarkConduitBlock.ID, () -> new VerticallyAttachableBlockItem(ModBlocks.DARK_TORCH.get(), ModBlocks.DARK_TORCH_WALL.get(), new Item.Settings().arch$tab(GROUP), Direction.DOWN));
        var tableUpgrade = items.register(TableUpgradeItem.ID, () -> new TableUpgradeItem(new Item.Settings().arch$tab(GROUP).maxCount(1).rarity(Rarity.EPIC)));

    }
}
