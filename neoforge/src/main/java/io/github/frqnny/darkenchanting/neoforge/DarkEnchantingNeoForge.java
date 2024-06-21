package io.github.frqnny.darkenchanting.neoforge;

import io.github.frqnny.darkenchanting.DarkEnchanting;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;


@Mod(DarkEnchanting.MOD_ID)
public final class DarkEnchantingNeoForge {
    public DarkEnchantingNeoForge(IEventBus modBus) {
        // Run our common setup
        DarkEnchanting.init();
    }
}
