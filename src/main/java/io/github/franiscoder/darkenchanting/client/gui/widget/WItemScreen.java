package io.github.franiscoder.darkenchanting.client.gui.widget;

import io.github.cottonmc.cotton.gui.widget.WWidget;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.ItemStack;

import java.util.List;

public class WItemScreen extends WWidget {
    private ItemStack stack;
    private List<String> information;

    public WItemScreen(ItemStack stack) {
        this.stack = stack;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void paintBackground(int x, int y) {
        MinecraftClient.getInstance().getItemRenderer().renderGuiItem(stack, x, y);
    }

    @Override
    public void addInformation(List<String> information) {
        this.information = information;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void renderTooltip(int x, int y, int tX, int tY) {
        if (this.information.size() == 0)
            return;

        Screen screen = MinecraftClient.getInstance().currentScreen;
        screen.renderTooltip(information, tX + x, tY + y);
    }
}
