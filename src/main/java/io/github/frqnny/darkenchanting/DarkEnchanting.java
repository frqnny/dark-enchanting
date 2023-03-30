package io.github.frqnny.darkenchanting;

import draylar.omegaconfig.OmegaConfig;
import io.github.frqnny.darkenchanting.config.DarkEnchantingConfig;
import io.github.frqnny.darkenchanting.init.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class DarkEnchanting implements ModInitializer {
    public static final String MODID = "dark-enchanting";

    public static final ItemGroup DARK_ENCHANTING = FabricItemGroupBuilder.create(new Identifier(MODID, "dark_enchanting_group"))
            .icon(() -> new ItemStack(ModBlocks.DARK_ENCHANTER))
            .build();

    public static final DarkEnchantingConfig CONFIG = OmegaConfig.register(DarkEnchantingConfig.class);

    public static Identifier id(String namespace) {
        return new Identifier(MODID, namespace);
    }

    @Override
    public void onInitialize() {
        ModItems.init();
        ModBlocks.init();
        ModGUIs.init();
        ModTags.init();
        ModPackets.init();
    }
}
