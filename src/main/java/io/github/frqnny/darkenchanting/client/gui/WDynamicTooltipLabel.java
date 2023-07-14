package io.github.frqnny.darkenchanting.client.gui;

import io.github.cottonmc.cotton.gui.widget.WWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

import java.util.List;
import java.util.function.Supplier;

public class WDynamicTooltipLabel extends WWidget {
    private final Supplier<List<Text>> supplier;

    public WDynamicTooltipLabel(Supplier<List<Text>> supplier) {
        this.supplier = supplier;
    }

    @Override
    public void paint(DrawContext context, int x, int y, int mouseX, int mouseY) {
        context.drawTooltip(MinecraftClient.getInstance().textRenderer, supplier.get(), x, y);
    }
}
