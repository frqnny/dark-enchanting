package io.github.franiscoder.darkenchanting.client.gui;

import io.github.cottonmc.cotton.gui.client.CottonInventoryScreen;
import net.minecraft.entity.player.PlayerEntity;

public class DarkEnchanterScreen extends CottonInventoryScreen<DarkEnchanterGUI> {
    public DarkEnchanterScreen(DarkEnchanterGUI container, PlayerEntity player) {
        super(container, player);
    }
}
