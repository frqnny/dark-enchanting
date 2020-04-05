package io.github.franiscoder.darkenchanting;

import io.github.franiscoder.darkenchanting.block.DarkEnchanter;
import io.github.franiscoder.darkenchanting.client.gui.DarkEnchanterGUI;
import io.github.franiscoder.darkenchanting.client.gui.DarkEnchanterScreen;
import io.github.franiscoder.darkenchanting.client.renderer.DEBlockEntityRenderer;
import io.github.franiscoder.darkenchanting.init.ModBlocks;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.screen.ScreenProviderRegistry;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.container.BlockContext;
import net.minecraft.util.Identifier;

public class DarkEnchantingClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ScreenProviderRegistry.INSTANCE.registerFactory(DarkEnchanter.ID, (syncId, identifier, player, buf) -> new DarkEnchanterScreen(new DarkEnchanterGUI(syncId, player.inventory, BlockContext.create(player.world, buf.readBlockPos())), player));
        BlockEntityRendererRegistry.INSTANCE.register(ModBlocks.DE_BLOCK_ENTITY, DEBlockEntityRenderer::new);
        ClientSpriteRegistryCallback.event(SpriteAtlasTexture.BLOCK_ATLAS_TEX).register((atlas, registry) -> {
            registry.register(new Identifier("dark-enchanting:entity/book1"));
        });
    }
}