package io.github.frqnny.darkenchanting.client.screen.widget;

import io.github.frqnny.darkenchanting.client.screen.DarkEnchanterScreen;
import io.github.frqnny.darkenchanting.config.ConfigEnchantment;
import io.github.frqnny.darkenchanting.util.TagUtils;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;

import java.util.List;
import java.util.Optional;

@Environment(EnvType.CLIENT)
public class EnchantSlidersListWidget extends ElementListWidget<EnchantSlidersListWidget.WidgetEntry> {
    private final DarkEnchanterScreen screen;

    public EnchantSlidersListWidget(DarkEnchanterScreen darkEnchanterScreen, int x, int y, int width, int height) {
        super(darkEnchanterScreen.getClient(), width, height, 0, 20);
        this.screen = darkEnchanterScreen;
        this.setX(x);
        this.setY(y);
    }

    public static EnchantSliderWidget getSlider(RegistryEntry<Enchantment> enchantment, Object2IntMap<RegistryEntry<Enchantment>> enchantmentsToApply, Object2IntMap<RegistryEntry<Enchantment>> enchantments) {
        int levelForSlider = 0;
        if (enchantmentsToApply.containsKey(enchantment)) {
            levelForSlider = enchantmentsToApply.getInt(enchantment);
        } else if (enchantments.containsKey(enchantment)) {
            if (!isEnchantmentRemoved(enchantment.value(), enchantmentsToApply)) {
                levelForSlider = enchantments.getInt(enchantment);
            }
        }

        return new EnchantSliderWidget(enchantment, levelForSlider, enchantment.value().getMaxLevel());
    }

    public static boolean isEnchantmentRemoved(Enchantment enchantment, Object2IntMap<RegistryEntry<Enchantment>> enchantmentsToApply) {
        return enchantmentsToApply.getInt(enchantment) <= 0;
    }

    @Override
    public int getRowWidth() {
        return this.getWidth();
    }

    @Override
    protected int getScrollbarX() {
        return super.getScrollbarX() - 16;
    }

    public void populateSliders() {
        this.clearEntries();
        ItemStack stack = screen.getScreenHandler().inv.getEnchantStack();
        if (stack.isEmpty()) {
            return;
        }

        var enchantmentRegistry = screen.getClient().world.getRegistryManager().getOrThrow(RegistryKeys.ENCHANTMENT);
        for (Enchantment enchantment : enchantmentRegistry) {
            var registryEntry = enchantmentRegistry.getEntry(enchantment);
            Optional<ConfigEnchantment> configEnchantmentOptional = ConfigEnchantment.getConfigEnchantmentFor(screen.getClient().world, enchantment);

            if (configEnchantmentOptional.isPresent()) {
                ConfigEnchantment configEnchantment = configEnchantmentOptional.get();
                if (!configEnchantment.activated) {
                    continue;
                }
            }

            if (TagUtils.isEnchantmentDisabled(this.screen.getClient().world, enchantment) || enchantment.getMaxLevel() < 1) {
                continue;
            }

            if (enchantment.isAcceptableItem(stack)) {
                EnchantSliderWidget slider = getSlider(registryEntry, screen.enchantmentsToApply, screen.enchantmentsOnStack);
                slider.setCallback(level -> screen.onSliderValueChange(registryEntry, level));
                WidgetEntry entry = WidgetEntry.create(slider);
                this.addEntry(entry);
            }
        }

        checkIncompabilities();
    }

    public void checkIncompabilities() {
        var registry = this.client.world.getRegistryManager().getOrThrow(RegistryKeys.ENCHANTMENT);

        for (WidgetEntry entry : this.children()) {
            Enchantment enchantment = entry.getEnchantment();
            RegistryEntry<Enchantment> enchantmentRegistryEntry = registry.getEntry(enchantment);

            boolean activated = true;
            for (RegistryEntry<Enchantment> enchantmentOnStack : screen.enchantmentsOnStack.keySet()) {
                if (!isEnchantmentRemoved(enchantmentOnStack.value(), screen.enchantmentsToApply)) {

                    if (enchantmentOnStack.value().exclusiveSet().contains(enchantmentRegistryEntry) && !enchantmentOnStack.equals(enchantmentRegistryEntry)) {
                        activated = false;
                    }
                }
            }

            for (var enchantmentEntry : screen.enchantmentsToApply.object2IntEntrySet()) {
                Enchantment enchantmentOnStack = enchantmentEntry.getKey().value();

                if (enchantmentOnStack.exclusiveSet().contains(enchantmentRegistryEntry) && !enchantmentOnStack.equals(enchantment) && enchantmentEntry.getIntValue() > 0) {
                    activated = false;
                }
            }

            entry.slider.setActivated(activated);
        }
    }

    @Override
    protected void drawHeaderAndFooterSeparators(DrawContext context) {

    }

    @Override
    protected void drawMenuListBackground(DrawContext context) {

    }

    @Environment(value = EnvType.CLIENT)
    protected static class WidgetEntry extends ElementListWidget.Entry<WidgetEntry> {
        private final EnchantSliderWidget slider;
        private final List<EnchantSliderWidget> cachedChildrenList;

        private WidgetEntry(EnchantSliderWidget slider) {
            this.slider = slider;
            cachedChildrenList = List.of(slider);
        }

        public static WidgetEntry create(EnchantSliderWidget widget) {
            return new WidgetEntry(widget);
        }

        @Override
        public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            slider.setPosition(x, y);
            slider.render(context, mouseX, mouseY, tickDelta);
        }

        @Override
        public List<? extends Element> children() {
            return this.cachedChildrenList;
        }

        @Override
        public List<? extends Selectable> selectableChildren() {
            return this.cachedChildrenList;
        }

        public Enchantment getEnchantment() {
            return slider.getEnchantment();
        }
    }
}
