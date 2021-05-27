package io.github.frqnny.darkenchanting.client.gui;

import io.github.cottonmc.cotton.gui.client.CottonInventoryScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

public class DarkEnchanterScreen extends CottonInventoryScreen<DarkEnchanterGUI> {
    public DarkEnchanterScreen(DarkEnchanterGUI c, PlayerEntity p, Text t) {
        super(c, p, t);
    }
}
