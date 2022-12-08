package io.github.frqnny.darkenchanting.init;

import io.github.cottonmc.cotton.gui.client.CottonInventoryScreen;
import io.github.frqnny.darkenchanting.DarkEnchanting;
import io.github.frqnny.darkenchanting.client.gui.DarkEnchanterGUI;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.registry.Registries;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.registry.Registry;

public class ModGUIs {
    public static final ScreenHandlerType<DarkEnchanterGUI> DARK_ENCHANTER_GUI = new ExtendedScreenHandlerType<>((syncId, inventory, buf) -> new DarkEnchanterGUI(syncId, inventory, ScreenHandlerContext.create(inventory.player.world, buf.readBlockPos())));

    public static void init() {
        Registry.register(Registries.SCREEN_HANDLER, DarkEnchanting.id("dark_enchanter_gui"), DARK_ENCHANTER_GUI);
    }

    @SuppressWarnings("all")
    public static void clientInit() {
        HandledScreens.<DarkEnchanterGUI, CottonInventoryScreen<DarkEnchanterGUI>>register(DARK_ENCHANTER_GUI, CottonInventoryScreen::new);
    }
}
