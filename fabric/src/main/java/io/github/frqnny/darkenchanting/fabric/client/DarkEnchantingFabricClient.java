package io.github.frqnny.darkenchanting.fabric.client;

import dev.architectury.registry.client.rendering.RenderTypeRegistry;
import io.github.frqnny.darkenchanting.client.DarkEnchantingClient;
import io.github.frqnny.darkenchanting.init.ModBlocks;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.render.RenderLayer;

public final class DarkEnchantingFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        DarkEnchantingClient.init();
        RenderTypeRegistry.register(RenderLayer.getCutout(), ModBlocks.DARK_TORCH_WALL.get(), ModBlocks.DARK_TORCH.get());
    }
}
