package io.github.frqnny.darkenchanting.neoforge.client;

import io.github.frqnny.darkenchanting.DarkEnchanting;
import io.github.frqnny.darkenchanting.blockentity.DarkEnchanterBlockEntity;
import io.github.frqnny.darkenchanting.client.DarkEnchantingClient;
import io.github.frqnny.darkenchanting.client.renderer.DarkEnchanterBlockEntityRenderer;
import io.github.frqnny.darkenchanting.init.ModBlocks;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@Mod(value = DarkEnchanting.MOD_ID, dist = Dist.CLIENT)
public final class DarkEnchantingNeoForgeClient {
    public DarkEnchantingNeoForgeClient(IEventBus modBus) {
        DarkEnchantingClient.init();
        modBus.register(this);
    }

    @SubscribeEvent
    public void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlocks.DE_BLOCK_ENTITY.get(), DarkEnchanterBlockEntityRenderer::new);

    }
}
