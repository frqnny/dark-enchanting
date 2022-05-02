package io.github.frqnny.darkenchanting;

import io.github.frqnny.darkenchanting.init.ModBlocks;
import io.github.frqnny.darkenchanting.init.ModGUIs;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.block.Block;
import net.minecraft.client.render.RenderLayer;

public class DarkEnchantingClient implements ClientModInitializer {

    public static void registerCutout(Block block) {
        BlockRenderLayerMap.INSTANCE.putBlock(block, RenderLayer.getCutout());
    }

    @Override
    public void onInitializeClient() {
        ModGUIs.clientInit();
        ModBlocks.clientInit();

        registerCutout(ModBlocks.DARK_TORCH);
        registerCutout(ModBlocks.DARK_TORCH_WALL);
    }
}
