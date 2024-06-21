package io.github.frqnny.darkenchanting;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import dev.architectury.registry.registries.RegistrarManager;
import io.github.frqnny.darkenchanting.config.DarkEnchantingConfig;
import io.github.frqnny.darkenchanting.init.*;
import io.github.frqnny.omegaconfig.OmegaConfig;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class DarkEnchanting {
    public static final String MOD_ID = "darkenchanting";
    public static final DarkEnchantingConfig CONFIG = OmegaConfig.register(DarkEnchantingConfig.class);
    public static Supplier<RegistrarManager> MANAGER;
    public static final Logger LOGGER = LoggerFactory.getLogger("DarkEnchanting");

    public static Identifier id(String path) {
        return Identifier.of(MOD_ID, path);
    }
    public static void init() {
        LOGGER.info("Gathering the powers of the moon...");
        MANAGER = Suppliers.memoize(() -> RegistrarManager.get(MOD_ID));
        ModBlocks.init();
        ModItems.init();
        ModTags.init();
        ModPackets.init();
        ModGUIs.init();
        LOGGER.info("At your service, my dark one. :)");

    }
}
