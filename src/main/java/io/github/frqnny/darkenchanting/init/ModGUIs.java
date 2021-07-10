package io.github.frqnny.darkenchanting.init;

import io.github.cottonmc.cotton.gui.client.CottonInventoryScreen;
import io.github.frqnny.darkenchanting.DarkEnchanting;
import io.github.frqnny.darkenchanting.client.gui.DarkEnchanterGUI;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;

public class ModGUIs {
    public static ScreenHandlerType<DarkEnchanterGUI> DARK_ENCHANTER_GUI;

    public static void init() {
        DARK_ENCHANTER_GUI = ScreenHandlerRegistry.registerExtended(DarkEnchanting.id("dark_enchanter_gui"), (syncId, inventory, buf) -> new DarkEnchanterGUI(syncId, inventory, ScreenHandlerContext.create(inventory.player.world, buf.readBlockPos())));

    }

    @SuppressWarnings("all") // IDEA warns me about unneeded cast but that's not true.
    public static void clientInit() {
        ScreenRegistry.<DarkEnchanterGUI, CottonInventoryScreen<DarkEnchanterGUI>>register(DARK_ENCHANTER_GUI, (gui, inventory, title) -> new CottonInventoryScreen<>(gui, inventory.player, title));
    }
}
