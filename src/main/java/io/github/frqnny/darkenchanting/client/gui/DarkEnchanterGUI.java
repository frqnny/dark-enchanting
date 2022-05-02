package io.github.frqnny.darkenchanting.client.gui;

import com.google.common.collect.ImmutableList;
import io.github.cottonmc.cotton.gui.SyncedGuiDescription;
import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.data.Axis;
import io.github.frqnny.darkenchanting.blockentity.inventory.DarkEnchanterInventory;
import io.github.frqnny.darkenchanting.config.ConfigEnchantment;
import io.github.frqnny.darkenchanting.init.ModGUIs;
import io.github.frqnny.darkenchanting.init.ModPackets;
import io.github.frqnny.darkenchanting.util.BookcaseUtils;
import io.github.frqnny.darkenchanting.util.CostUtils;
import io.github.frqnny.darkenchanting.util.PlayerUtils;
import io.github.frqnny.darkenchanting.util.TagUtils;
import io.netty.buffer.Unpooled;
import it.unimi.dsi.fastutil.objects.Object2IntLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class DarkEnchanterGUI extends SyncedGuiDescription {
    public final DarkEnchanterInventory inv = new DarkEnchanterInventory(this);
    public final WBox box = new WBox(Axis.VERTICAL);
    public final List<WLabeledSlider> enchantmentSliders  = new ArrayList<>(15);
    public final ScreenHandlerContext context;
    public final Object2IntLinkedOpenHashMap<Enchantment> enchantmentsToApply = new Object2IntLinkedOpenHashMap<>(15);
    public final Object2IntLinkedOpenHashMap<Enchantment> enchantmentsOnStack = new Object2IntLinkedOpenHashMap<>(15);
    public final WButton enchantButton = new WButton();
    public final WButton repairButton = new WButton();
    public int enchantCost = 0;
    public int repairCost = 0;
    public int bookshelfDiscount = 0;
    public String bookcaseStats1;
    public String bookcaseStats2;
    public String bookcaseStats3;

    public DarkEnchanterGUI(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(ModGUIs.DARK_ENCHANTER_GUI, syncId, playerInventory);
        PlayerUtils.syncExperience(playerInventory.player);
        this.blockInventory = inv;
        this.context = context;

        WPlainPanel root = new WPlainPanel();
        this.setRootPanel(root);
        root.setSize(235, 250);

        WItemSlot slot = WItemSlot.of(inv, 0);
        slot.setFilter((stack) -> inv.isValid(0, stack));
        root.add(slot, 35, 17);

        WScrollPanel scrollPanel = new WScrollPanel(box);
        root.add(scrollPanel, 65, 17, 150, 135);

        root.add(enchantButton, 35, 60, 20, 20);
        enchantButton.setLabel(new LiteralText("E"));
        enchantButton.setOnClick(this::enchant);

        root.add(repairButton, 35, 85, 20, 20);
        repairButton.setLabel(new LiteralText("R"));
        repairButton.setOnClick(this::repair);

        WDynamicTooltipLabel tooltip = new WDynamicTooltipLabel(this::getTooltip);
        root.add(tooltip, -120, 43);

        root.add(this.createPlayerInventoryPanel(true), 36, 153);
        root.validate(this);
    }

    public Text getLabel(Enchantment enchantment, int level) {
        MutableText mutableText = new TranslatableText(enchantment.getTranslationKey());
        if (enchantment.isCursed()) {
            mutableText.formatted(Formatting.RED);
        } else if (enchantment.isTreasure()) {
            mutableText.formatted(Formatting.BLUE);
        } else {
            mutableText.formatted(Formatting.GRAY);
        }

        if (level > 0) {
            mutableText.append(" ").append(new TranslatableText("enchantment.level." + level));
        }

        return mutableText;
    }

    public void fillBox() {
        for (WLabeledSlider slider : enchantmentSliders) {
            box.remove(slider);
        }
        populateList();
        for (WLabeledSlider slider : enchantmentSliders) {
            box.add(slider, 140, 18);
        }
        this.getRootPanel().layout();
    }

    public void populateList() {
        enchantmentSliders.clear();
        ItemStack stack = inv.getActualStack();
        if (stack.isEmpty()) {
            return;
        }
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.get(stack);
        for (Enchantment enchantment : Registry.ENCHANTMENT) {
            Optional<ConfigEnchantment> configEnchantmentOptional = ConfigEnchantment.getConfigEnchantmentFor(enchantment);

            if (configEnchantmentOptional.isPresent()) {
                ConfigEnchantment configEnchantment = configEnchantmentOptional.get();
                if (!configEnchantment.activated) {
                    continue;
                }

            }

            if (this.context.get((world, pos) -> TagUtils.isEnchantmentDisabled(world, enchantment), false) || enchantment.getMaxLevel() < 1) {
                continue;
            }

            if (enchantment.isAcceptableItem(stack)) {
                WLabeledSlider enchantmentSlider;
                if (enchantmentsToApply.containsKey(enchantment)) {
                    enchantmentSlider = addNewWidgetToList(enchantmentsToApply.getInt(enchantment), enchantment);
                } else if (enchantments.containsKey(enchantment)) {
                    if (this.isEnchantmentRemoved(enchantment)) {
                        enchantmentSlider = addNewWidgetToList(0, enchantment);
                    } else {
                        enchantmentSlider = addNewWidgetToList(enchantments.get(enchantment), enchantment);
                    }

                } else {
                    enchantmentSlider = addNewWidgetToList(0, enchantment);

                }

                for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
                    Enchantment enchantmentOnStack = entry.getKey();
                    if (!this.isEnchantmentRemoved(enchantmentOnStack)) {
                        if (!enchantmentOnStack.canCombine(enchantment) && !enchantmentOnStack.equals(enchantment)) {
                            enchantmentSliders.remove(enchantmentSlider);
                        }
                    }
                }

                for (Object2IntMap.Entry<Enchantment> entry : enchantmentsToApply.object2IntEntrySet()) {
                    Enchantment enchantmentOnStack = entry.getKey();

                    if (!enchantmentOnStack.canCombine(enchantment) && !enchantmentOnStack.equals(enchantment) && entry.getIntValue() > 0) {
                        enchantmentSliders.remove(enchantmentSlider);
                    }
                }

            }
        }
    }

    public WLabeledSlider addNewWidgetToList(int value, Enchantment enchantment) {
        WLabeledSlider slider = new WLabeledSlider(0, enchantment.getMaxLevel());
        slider.setLabel(getLabel(enchantment, value));
        slider.setLabelUpdater((power) -> getLabel(enchantment, power));
        slider.setValue(value);
        slider.setValueChangeListener((power) -> onSliderValueChange(enchantment, power));
        slider.setHost(this);
        enchantmentSliders.add(slider);
        return slider;
    }

    public void onSliderValueChange(Enchantment enchantment, int level) {
        if (enchantmentsToApply.containsKey(enchantment)) {
            enchantmentsToApply.replace(enchantment, level);
        } else {
            enchantmentsToApply.put(enchantment, level);
        }

        fillBox();
        recalculateEnchantmentCost();
    }

    public void recalculateEnchantmentCost() {
        this.context.run((world, blockPos) -> {
            int totalExperience = PlayerUtils.getTotalExperience(playerInventory.player);
            enchantCost = BookcaseUtils.applyDiscount(CostUtils.getXpCost(enchantmentsToApply, enchantmentsOnStack), world, blockPos);

            if (BookcaseUtils.getObsidianCount(world, blockPos)) {
                bookcaseStats1 = "☆";
            } else {
                bookcaseStats1 = "";
            }

            if (BookcaseUtils.getObsidianCount2(world, blockPos)) {
                bookcaseStats2 = "☆";
            } else {
                bookcaseStats2 = "";
            }

            if (BookcaseUtils.getConduits(world, blockPos)) {
                bookcaseStats3 = "☆";
            } else {
                bookcaseStats3 = "";
            }

            bookshelfDiscount = (int) (BookcaseUtils.getDiscount(world, blockPos) * 100);

            boolean enchantmentsHaveChanged = true;

            for (Object2IntMap.Entry<Enchantment> entrySet : enchantmentsToApply.object2IntEntrySet()) {
                Enchantment enchantment = entrySet.getKey();
                int level = entrySet.getIntValue();

                if (!(enchantmentsOnStack.containsKey(enchantment)) || !(enchantmentsOnStack.getInt(enchantment) == level)) {
                    enchantmentsHaveChanged = false;
                }
            }

            if (enchantmentsHaveChanged) {
                for (Object2IntMap.Entry<Enchantment> entrySet : enchantmentsOnStack.object2IntEntrySet()) {
                    Enchantment enchantment = entrySet.getKey();
                    int level = entrySet.getIntValue();
                    if (!(enchantmentsToApply.containsKey(enchantment)) || !(enchantmentsToApply.getInt(enchantment) == level)) {
                        enchantmentsHaveChanged = false;
                    }
                }
            }


            enchantButton.setEnabled((totalExperience >= enchantCost && !enchantmentsHaveChanged) || playerInventory.player.isCreative());
        });

    }

    public void recalculateRepairCost() {
        this.context.run((world, pos) -> repairCost = BookcaseUtils.applyDiscount(CostUtils.getRepairCost(inv.getActualStack()), world, pos));
        repairButton.setEnabled(inv.getActualStack().isDamaged() || playerInventory.player.isCreative());
    }

    public void enchant() {
        this.context.run((world, pos) -> {
            PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
            buf.writeBlockPos(pos);
            buf.writeVarInt(enchantmentsToApply.size());
            for (Object2IntMap.Entry<Enchantment> entry : enchantmentsToApply.object2IntEntrySet()) {
                buf.writeIdentifier(Registry.ENCHANTMENT.getId(entry.getKey()));
                buf.writeVarInt(entry.getIntValue());
            }

            this.getPacketSender().sendPacket(ModPackets.APPLY_ENCHANTMENTS, buf);
            this.inv.markDirty();
        });
    }

    public void repair() {
        this.context.run((world, pos) -> {
            PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
            buf.writeBlockPos(pos);
            this.getPacketSender().sendPacket(ModPackets.APPLY_REPAIR, buf);
        });
    }

    public List<Text> getTooltip() {
        String string;
        if (DarkEnchanterGUI.this.enchantCost > 0) {
            string = "Pay: " + DarkEnchanterGUI.this.enchantCost + " XP";
        } else {
            string = "Receive: " + -DarkEnchanterGUI.this.enchantCost + " XP";
        }

        return ImmutableList.of(
                new LiteralText("Enchant Cost:").formatted(Formatting.DARK_GREEN),
                new LiteralText(string),
                new LiteralText(""),
                new LiteralText(""),
                new LiteralText("Repair Cost:").formatted(Formatting.BLUE),
                new LiteralText("Pay: " + DarkEnchanterGUI.this.repairCost + " XP"),
                new LiteralText(""),
                new LiteralText(""),
                new LiteralText("Bookshelf Discount:").formatted(Formatting.DARK_PURPLE),
                new LiteralText(DarkEnchanterGUI.this.bookcaseStats1 + DarkEnchanterGUI.this.bookcaseStats2 + DarkEnchanterGUI.this.bookcaseStats3 + " " + DarkEnchanterGUI.this.bookshelfDiscount + " %"),
                new LiteralText(""),
                new LiteralText(""),
                new LiteralText("You have: " + PlayerUtils.getTotalExperience(DarkEnchanterGUI.this.playerInventory.player) + " XP").formatted(Formatting.GOLD),
                new LiteralText(""),
                new LiteralText(""));
    }

    public void onStackUpdate(ItemStack stack) {
        fillBox();

        enchantmentsOnStack.clear();
        enchantmentsToApply.clear();
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.get(stack);
        enchantments.forEach((enchantment, level) -> {
            enchantmentsToApply.putIfAbsent(enchantment, (int) level);
            enchantmentsOnStack.put(enchantment, (int) level);
        });
        recalculateEnchantmentCost();
        recalculateRepairCost();
    }

    @Override
    public void close(PlayerEntity player) {
        super.close(player);
        this.context.run((world, pos) -> this.dropInventory(player, this.inv));
    }

    public boolean isEnchantmentRemoved(Enchantment enchantment) {
        return this.enchantmentsToApply.getInt(enchantment) <= 0;
    }
}
