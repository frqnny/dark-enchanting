package io.github.frqnny.darkenchanting.init;

import io.github.frqnny.darkenchanting.DarkEnchanting;
import io.github.frqnny.darkenchanting.client.gui.DarkEnchanterGUI;
import io.github.frqnny.darkenchanting.client.gui.DarkEnchanterScreen;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;

public class ModGUIs {

    public static ScreenHandlerType<DarkEnchanterGUI> DARK_ENCHANTER_GUI;

    public static void init() {
        DARK_ENCHANTER_GUI = ScreenHandlerRegistry.registerExtended(DarkEnchanting.id("dark_enchanter_gui"), (syncId, inventory, buf) -> new DarkEnchanterGUI(syncId, inventory, ScreenHandlerContext.create(inventory.player.world, buf.readBlockPos())));

    }

    public static void clientInit() {
        ScreenRegistry.Factory<DarkEnchanterGUI, DarkEnchanterScreen> DARK_ENCHANTING_SCREEN = (gui, inventory, title) -> new DarkEnchanterScreen(gui, inventory.player, title);
        ScreenRegistry.register(DARK_ENCHANTER_GUI, DARK_ENCHANTING_SCREEN);
    }
}
