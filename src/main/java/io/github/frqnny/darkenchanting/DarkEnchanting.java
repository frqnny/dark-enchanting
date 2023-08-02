package io.github.frqnny.darkenchanting;

import draylar.omegaconfig.OmegaConfig;
import io.github.frqnny.darkenchanting.config.DarkEnchantingConfig;
import io.github.frqnny.darkenchanting.init.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import static io.github.frqnny.darkenchanting.init.ModBlocks.DARK_TORCH;
import static io.github.frqnny.darkenchanting.init.ModItems.DARK_ENCHANTER;
import static io.github.frqnny.darkenchanting.init.ModItems.TABLE_UPGRADE;

public class DarkEnchanting implements ModInitializer {
    public static final String MODID = "dark-enchanting";


    public static final DarkEnchantingConfig CONFIG = OmegaConfig.register(DarkEnchantingConfig.class);

    public static Identifier id(String namespace) {
        return new Identifier(MODID, namespace);
    }

    public static final RegistryKey<ItemGroup> DarkEnchantingGroup = RegistryKey.of(RegistryKeys.ITEM_GROUP, new Identifier("dark-enchanting", "dark_enchanting_group"));

    @Override
    public void onInitialize() {
        ModItems.init();
        ModBlocks.init();
        ModGUIs.init();
        ModTags.init();
        ModPackets.init();

        Registry.register(Registries.ITEM_GROUP, DarkEnchantingGroup, FabricItemGroup.builder()
                .icon(() -> new ItemStack(DARK_ENCHANTER))
                .displayName(Text.translatable("itemGroup.dark-enchanting.dark_enchanting_group"))
                .build());

        ItemGroupEvents.modifyEntriesEvent(DarkEnchantingGroup).register(content -> {
            content.add(DARK_ENCHANTER);
            content.add(DARK_TORCH);
            content.add(TABLE_UPGRADE);
        });

    }
}
