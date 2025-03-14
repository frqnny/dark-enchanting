package io.github.frqnny.darkenchanting;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;


@Mod(DarkEnchanting.MOD_ID)
public final class DarkEnchantingNeoForge {
    public DarkEnchantingNeoForge(IEventBus modBus) {
        // Run our common setup
        DarkEnchanting.init();
    }
}
