package io.github.frqnny.darkenchanting.client.screen.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.frqnny.darkenchanting.util.TagUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.navigation.GuiNavigationType;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.screen.narration.NarrationPart;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.input.KeyCodes;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.glfw.GLFW;

import java.util.function.IntConsumer;

/*
 * Based on SliderWidget, this enchant slider instead focuses on:
 *  1. Integer-based slider system rather than an internal double
 *  2. Built in min/max values
 *  3. Hardcoded interoperability with Enchantments to insert more logic here
 */
public class EnchantSliderWidget extends ClickableWidget {
    private static final Identifier TEXTURE = Identifier.of("widget/slider");
    private static final Identifier HIGHLIGHTED_TEXTURE = Identifier.of("widget/slider_highlighted");
    private static final Identifier HANDLE_TEXTURE = Identifier.of("widget/slider_handle");
    private static final Identifier HANDLE_HIGHLIGHTED_TEXTURE = Identifier.of("widget/slider_handle_highlighted");
    private static final int MIN_ENCHANT_VALUE = 0;
    private final RegistryEntry<Enchantment> enchantment;
    private final int max;
    protected int level;
    private boolean sliderFocused;
    private IntConsumer callback = null;
    private boolean activated = true;

    public EnchantSliderWidget(RegistryEntry<Enchantment> enchantment, int level, int max) {
        super(0, 0, 120, 18, getLabel(enchantment, level, true));
        this.enchantment = enchantment;
        this.level = level;
        this.max = max;
    }

    public static Text getLabel(RegistryEntry<Enchantment> enchantment, int level, boolean activated) {
        MutableText mutableText = MutableText.of(enchantment.value().description().getContent());
        var world = MinecraftClient.getInstance().world;
        if (activated) {
            if (TagUtils.isEnchantmentCurse(world, enchantment.value())) {
                mutableText.formatted(Formatting.RED);
            } else if (TagUtils.isEnchantmentTreasure(world, enchantment.value())) {
                mutableText.formatted(Formatting.BLUE);
            } else {
                mutableText.formatted(Formatting.WHITE);
            }
        } else {
            mutableText.formatted(Formatting.DARK_GRAY);
        }

        if (level > 0) {
            mutableText.append(" ").append(Text.translatable("enchantment.level." + level));
        }

        return mutableText;
    }

    public boolean isActivated() {
        return this.activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
        if (!activated) {
            this.setValue(0);
        }
        updateMessage();

    }

    public Enchantment getEnchantment() {
        return this.enchantment.value();
    }

    private Identifier getTexture() {
        if (this.isFocused() && !this.sliderFocused) {
            return HIGHLIGHTED_TEXTURE;
        }
        return TEXTURE;
    }

    private Identifier getHandleTexture() {
        if (this.hovered || this.sliderFocused) {
            return HANDLE_HIGHLIGHTED_TEXTURE;
        }
        return HANDLE_TEXTURE;
    }

    @Override
    protected MutableText getNarrationMessage() {
        return Text.translatable("gui.narrate.slider", this.getMessage());
    }

    @Override
    public void appendClickableNarrations(NarrationMessageBuilder builder) {
        builder.put(NarrationPart.TITLE, this.getNarrationMessage());
        if (this.active) {
            if (this.isFocused()) {
                builder.put(NarrationPart.USAGE, Text.translatable("narration.slider.usage.focused"));
            } else {
                builder.put(NarrationPart.USAGE, Text.translatable("narration.slider.usage.hovered"));
            }
        }
    }

    @Override
    public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        context.setShaderColor(1.0f, 1.0f, 1.0f, this.alpha);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        context.drawGuiTexture(this.getTexture(), this.getX(), this.getY(), this.getWidth(), this.getHeight());
        if (activated) {
            double ratio = (double) this.level / this.max;
            int xOffset = (int) Math.round(ratio * (this.getWidth() - 8));
            context.drawGuiTexture(this.getHandleTexture(), this.getX() + xOffset, this.getY(), 8, this.getHeight());
        } else {
            //TODO implement a tooltip to explain why it got disabled?
        }
        context.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        int i = this.active ? 16777215 : 10526880;
        this.drawScrollableText(context, minecraftClient.textRenderer, 8, i | MathHelper.ceil(this.alpha * 255.0F) << 24);

    }


    @Override
    public void onClick(double mouseX, double mouseY) {
        this.setValueFromMouse(mouseX);
    }

    @Override
    public void setFocused(boolean focused) {
        super.setFocused(focused);
        if (!focused) {
            this.sliderFocused = false;
            return;
        }
        GuiNavigationType guiNavigationType = MinecraftClient.getInstance().getNavigationType();
        if (guiNavigationType == GuiNavigationType.MOUSE || guiNavigationType == GuiNavigationType.KEYBOARD_TAB) {
            this.sliderFocused = true;
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (KeyCodes.isToggle(keyCode)) {
            this.sliderFocused = !this.sliderFocused;
            return true;
        }
        if (this.sliderFocused) {
            int change = switch (keyCode) {
                case GLFW.GLFW_KEY_LEFT -> -1;
                case GLFW.GLFW_KEY_RIGHT -> 1;
                default -> 0;
            };
            this.setValue(this.level + change);
            return true;
        }
        return false;
    }

    private void setValueFromMouse(double mouseX) {
        double relativeX = mouseX - this.getX();
        double ratio = relativeX / width;
        int value = (int) Math.round(ratio * max);
        this.setValue(value);
    }

    private void setValue(int value) {
        if (isActivated()) {
            int d = this.level;
            this.level = MathHelper.clamp(value, MIN_ENCHANT_VALUE, max);
            if (d != this.level) {
                this.applyValue();
            }
            this.updateMessage();
        } else {
            this.level = 0;
        }
    }

    @Override
    protected void onDrag(double mouseX, double mouseY, double deltaX, double deltaY) {
        this.setValueFromMouse(mouseX);
        super.onDrag(mouseX, mouseY, deltaX, deltaY);
    }

    @Override
    public void playDownSound(SoundManager soundManager) {
    }

    @Override
    public void onRelease(double mouseX, double mouseY) {
        super.playDownSound(MinecraftClient.getInstance().getSoundManager());
    }

    protected void updateMessage() {
        this.setMessage(getLabel(this.enchantment, level, activated));
    }

    protected void applyValue() {
        if (this.callback != null) {
            callback.accept(this.level);
        }
    }

    public void setCallback(IntConsumer callback) {
        this.callback = callback;
    }
}
