package io.github.frqnny.darkenchanting.client;

import dev.architectury.platform.Platform;
import dev.architectury.registry.client.rendering.RenderTypeRegistry;
import io.github.frqnny.darkenchanting.init.ModBlocks;
import io.github.frqnny.darkenchanting.init.ModGUIs;
import net.minecraft.client.render.RenderLayer;

public class DarkEnchantingClient {


    public static void init() {
        ModBlocks.clientInit();
        ModGUIs.clientInit();
    }
}
