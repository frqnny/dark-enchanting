package io.github.frqnny.darkenchanting.client;

import io.github.frqnny.darkenchanting.init.ModBlocks;
import io.github.frqnny.darkenchanting.init.ModGUIs;

public class DarkEnchantingClient {


    public static void init() {
        ModBlocks.clientInit();
        ModGUIs.clientInit();
    }
}
