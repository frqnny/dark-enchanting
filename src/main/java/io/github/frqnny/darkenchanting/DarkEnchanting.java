package io.github.frqnny.darkenchanting;

import draylar.omegaconfig.OmegaConfig;
import io.github.frqnny.darkenchanting.config.DarkEnchantingConfig;
import io.github.frqnny.darkenchanting.init.*;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;

public class DarkEnchanting implements ModInitializer {
    public static final String MODID = "darkenchanting";


    public static final DarkEnchantingConfig CONFIG = OmegaConfig.register(DarkEnchantingConfig.class);

    public static Identifier id(String namespace) {
        return Identifier.of(MODID, namespace);
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
