package io.github.frqnny.darkenchanting;

import io.github.frqnny.darkenchanting.init.ModBlocks;
import io.github.frqnny.darkenchanting.init.ModGUIs;
import io.github.frqnny.darkenchanting.init.ModItems;
import io.github.frqnny.darkenchanting.init.ModPackets;
import net.fabricmc.api.ClientModInitializer;

public class DarkEnchantingClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ModGUIs.clientInit();
        ModItems.clientInit();
        ModBlocks.clientInit();
        ModPackets.clientInit();
    }
}