package io.github.frqnny.darkenchanting.client.gui;

import io.github.cottonmc.cotton.gui.widget.WWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.List;
import java.util.function.Supplier;

public class WDynamicTooltipLabel extends WWidget {
    private final Supplier<List<Text>> supplier;

    public WDynamicTooltipLabel(Supplier<List<Text>> supplier) {
        this.supplier = supplier;
    }

    @Override
    public void paint(MatrixStack matrices, int x, int y, int mouseX, int mouseY) {
        MinecraftClient.getInstance().currentScreen.renderTooltip(matrices, supplier.get(), x, y);
    }
}
