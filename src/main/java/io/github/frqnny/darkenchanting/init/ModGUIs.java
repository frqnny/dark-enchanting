package io.github.frqnny.darkenchanting.init;

import io.github.frqnny.darkenchanting.DarkEnchanting;
import io.github.frqnny.darkenchanting.client.screen.DarkEnchanterScreen;
import io.github.frqnny.darkenchanting.network.ScreenPacket;
import io.github.frqnny.darkenchanting.screen.DarkEnchanterScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;

public class ModGUIs {
    public static final ScreenHandlerType<DarkEnchanterScreenHandler> DARK_ENCHANTER = new ExtendedScreenHandlerType<>(
            (syncId, inventory, packet) -> new DarkEnchanterScreenHandler(syncId, inventory, ScreenHandlerContext.create(inventory.player.getEntityWorld(), packet.pos())),
            ScreenPacket.PACKET_CODEC
    );

    public static void init() {
        Registry.register(Registries.SCREEN_HANDLER, DarkEnchanting.id("dark_enchanter"), DARK_ENCHANTER);

    }

    @SuppressWarnings("all")
    public static void clientInit() {
        HandledScreens.<DarkEnchanterScreenHandler, DarkEnchanterScreen>register(DARK_ENCHANTER, DarkEnchanterScreen::new);

    }
}
