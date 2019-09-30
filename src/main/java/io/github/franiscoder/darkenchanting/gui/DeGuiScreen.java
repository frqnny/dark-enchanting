package io.github.franiscoder.darkenchanting.gui;

import io.github.cottonmc.cotton.gui.client.CottonInventoryScreen;
import net.minecraft.entity.player.PlayerEntity;

public class DEGuiScreen extends CottonInventoryScreen<DEGuiController> {
    public DEGuiScreen(DEGuiController container, PlayerEntity player) {
        super(container, player);
    }
}
