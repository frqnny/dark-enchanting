package io.github.frqnny.darkenchanting.neoforge;

import dev.architectury.platform.hooks.EventBusesHooks;
import io.github.frqnny.darkenchanting.DarkEnchanting;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;


@Mod(DarkEnchanting.MOD_ID)
public final class DarkEnchantingNeoForge {
    public DarkEnchantingNeoForge(IEventBus modBus) {
        // Run our common setup
        DarkEnchanting.init();
    }
}
