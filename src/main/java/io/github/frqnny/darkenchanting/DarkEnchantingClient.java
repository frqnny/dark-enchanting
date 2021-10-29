package io.github.frqnny.darkenchanting;

import io.github.frqnny.darkenchanting.init.ModBlocks;
import io.github.frqnny.darkenchanting.init.ModGUIs;
import io.github.frqnny.darkenchanting.init.ModItems;
import io.github.frqnny.darkenchanting.init.ModPackets;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.block.Block;
import net.minecraft.client.render.RenderLayer;

public class DarkEnchantingClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ModGUIs.clientInit();
        ModItems.clientInit();
        ModBlocks.clientInit();
        ModPackets.clientInit();

        registerCutout(ModBlocks.DARK_TORCH);
        registerCutout(ModBlocks.DARK_TORCH_WALL);
    }

    public static void registerCutout(Block block) {
        BlockRenderLayerMap.INSTANCE.putBlock(block, RenderLayer.getCutout());
    }
}