package io.github.frqnny.darkenchanting.client;

import io.github.frqnny.darkenchanting.init.ModBlocks;
import io.github.frqnny.darkenchanting.init.ModGUIs;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class DarkEnchantingClient {

    public static void init() {
        ModBlocks.clientInit();
        ModGUIs.clientInit();
    }
}
