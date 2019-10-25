package io.github.franiscoder.darkenchanting.api.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.cottonmc.cotton.gui.client.ScreenDrawing;
import io.github.cottonmc.cotton.gui.widget.WLabeledSlider;
import io.github.cottonmc.cotton.gui.widget.WSlider;
import io.github.cottonmc.cotton.gui.widget.data.Alignment;
import io.github.cottonmc.cotton.gui.widget.data.Axis;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.text.Text;

import javax.annotation.Nullable;

public class WLB extends WAS {
    //Since like, a lot of stuff is protected and stuff I'm just including this so I can do my work.
    @Nullable private Text label = null;
    @Nullable private WLabeledSlider.LabelUpdater labelUpdater = null;
    private Alignment labelAlignment = Alignment.CENTER;

    public WLB(int min, int max) {
        this(min, max, Axis.HORIZONTAL);
    }

    public WLB(int min, int max, Axis axis) {
        super(min, max, axis);
    }

    public WLB(int min, int max, Axis axis, @Nullable Text label) {
        this(min, max, axis);
        this.label = label;
    }

    public WLB(int min, int max, @Nullable Text label) {
        this(min, max);
        this.label = label;
    }

    @Override
    public void setSize(int x, int y) {
        if (axis == Axis.HORIZONTAL) {
            super.setSize(x, 20);
        } else {
            super.setSize(20, y);
        }
    }

    @Nullable
    public Text getLabel() {
        return label;
    }

    public void setLabel(@Nullable Text label) {
        this.label = label;
    }

    @Override
    protected void onValueChanged(int value) {
        super.onValueChanged(value);
        if (labelUpdater != null) {
            label = labelUpdater.updateLabel(value);
        }
    }

    public Alignment getLabelAlignment() {
        return labelAlignment;
    }

    public void setLabelAlignment(Alignment labelAlignment) {
        this.labelAlignment = labelAlignment;
    }

    @Nullable
    public WLabeledSlider.LabelUpdater getLabelUpdater() {
        return labelUpdater;
    }

    public void setLabelUpdater(@Nullable WLabeledSlider.LabelUpdater labelUpdater) {
        this.labelUpdater = labelUpdater;
    }

    @Override
    protected int getThumbWidth() {
        return 8;
    }

    @Override
    protected boolean isMouseInsideBounds(int x, int y) {
        return x >= 0 && x <= width && y >= 0 && y <= height;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void paintBackground(int x, int y, int mouseX, int mouseY) {
        int aWidth = axis == Axis.HORIZONTAL ? width : height;
        int aHeight = axis == Axis.HORIZONTAL ? height : width;
        int rotMouseX = axis == Axis.HORIZONTAL ? mouseX : (height - mouseY);
        int rotMouseY = axis == Axis.HORIZONTAL ? mouseY : mouseX;

        RenderSystem.pushMatrix();
        RenderSystem.translatef(x, y, 0);
        if (axis == Axis.VERTICAL) {
            RenderSystem.translatef(0, height, 0);
            RenderSystem.rotatef(270, 0, 0, 1);
        }
        drawButton(0, 0, 0, aWidth);

        // 1: regular, 2: hovered, 0: disabled/dragging
        int thumbX = Math.round(coordToValueRatio * (value - min));
        int thumbY = 0;
        int thumbWidth = getThumbWidth();
        int thumbHeight = aHeight;
        boolean hovering = rotMouseX >= thumbX && rotMouseX <= thumbX + thumbWidth && rotMouseY >= thumbY && rotMouseY <= thumbY + thumbHeight;
        int thumbState = dragging || hovering ? 2 : 1;

        drawButton(thumbX, thumbY, thumbState, thumbWidth);

        if (thumbState == 1 && isFocused()) {
            float px = 1 / 32f;
            ScreenDrawing.texturedRect(thumbX, thumbY, thumbWidth, thumbHeight, WSlider.LIGHT_TEXTURE, 24*px, 0*px, 32*px, 20*px, 0xFFFFFFFF);
        }

        if (label != null) {
            int color = isMouseInsideBounds(mouseX, mouseY) ? 0xFFFFA0 : 0xE0E0E0;
            ScreenDrawing.drawStringWithShadow(label.asFormattedString(), labelAlignment, 2, aHeight / 2 - 4, aWidth - 4, color);
        }
        RenderSystem.popMatrix();
    }

    // state = 1: regular, 2: hovered, 0: disabled/dragging
    @Environment(EnvType.CLIENT)
    private void drawButton(int x, int y, int state, int width) {
        float px = 1 / 256f;
        float buttonLeft = 0 * px;
        float buttonTop = (46 + (state * 20)) * px;
        int halfWidth = width / 2;
        if (halfWidth > 198) halfWidth = 198;
        float buttonWidth = halfWidth * px;
        float buttonHeight = 20 * px;
        float buttonEndLeft = (200 - halfWidth) * px;

        ScreenDrawing.texturedRect(x, y, halfWidth, 20, AbstractButtonWidget.WIDGETS_LOCATION, buttonLeft, buttonTop, buttonLeft + buttonWidth, buttonTop + buttonHeight, 0xFFFFFFFF);
        ScreenDrawing.texturedRect(x + halfWidth, y, halfWidth, 20, AbstractButtonWidget.WIDGETS_LOCATION, buttonEndLeft, buttonTop, 200 * px, buttonTop + buttonHeight, 0xFFFFFFFF);
    }

    @FunctionalInterface
    public interface LabelUpdater {
        Text updateLabel(int value);
    }
}