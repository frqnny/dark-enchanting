package io.github.frqnny.darkenchanting.init;

import io.github.cottonmc.cotton.gui.client.CottonInventoryScreen;
import io.github.frqnny.darkenchanting.DarkEnchanting;
import io.github.frqnny.darkenchanting.client.gui.DarkEnchanterGUI;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.registry.Registry;

public class ModGUIs {
    public static final ScreenHandlerType<DarkEnchanterGUI> DARK_ENCHANTER_GUI = new ExtendedScreenHandlerType<>((syncId, inventory, buf) -> new DarkEnchanterGUI(syncId, inventory, ScreenHandlerContext.create(inventory.player.world, buf.readBlockPos())));

    public static void init() {
        Registry.register(Registry.SCREEN_HANDLER, DarkEnchanting.id("dark_enchanter_gui"), DARK_ENCHANTER_GUI);
    }

    public static void clientInit() {
        HandledScreens.register(DARK_ENCHANTER_GUI, (gui, inventory, title) -> new CottonInventoryScreen<>(gui, inventory.player, title));
    }
}
