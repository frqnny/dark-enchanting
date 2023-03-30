package io.github.frqnny.darkenchanting;

import io.github.frqnny.darkenchanting.client.renderer.DarkEnchanterBlockEntityRenderer;
import io.github.frqnny.darkenchanting.init.ModBlocks;
import io.github.frqnny.darkenchanting.init.ModGUIs;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.minecraft.block.Block;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.screen.PlayerScreenHandler;

public class DarkEnchantingClient implements ClientModInitializer {

    public static void registerCutout(Block block) {
        BlockRenderLayerMap.INSTANCE.putBlock(block, RenderLayer.getCutout());
    }

    @Override
    public void onInitializeClient() {
        ModGUIs.clientInit();
        ModBlocks.clientInit();
        ClientSpriteRegistryCallback.event(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE).register((atlas, registry) -> registry.register(DarkEnchanterBlockEntityRenderer.BOOK_ID));

        registerCutout(ModBlocks.DARK_TORCH);
        registerCutout(ModBlocks.DARK_TORCH_WALL);
    }
}
