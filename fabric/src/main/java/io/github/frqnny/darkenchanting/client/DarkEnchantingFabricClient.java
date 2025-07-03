package io.github.frqnny.darkenchanting.client;

import dev.architectury.registry.client.rendering.RenderTypeRegistry;
import io.github.frqnny.darkenchanting.init.ModBlocks;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.render.BlockRenderLayer;

public final class DarkEnchantingFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        DarkEnchantingClient.init();
        RenderTypeRegistry.register(BlockRenderLayer.CUTOUT, ModBlocks.DARK_TORCH_WALL.get(), ModBlocks.DARK_TORCH.get());
    }
}
