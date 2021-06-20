package io.github.frqnny.darkenchanting.client.gui;

import io.github.cottonmc.cotton.gui.SyncedGuiDescription;
import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.data.Axis;
import io.github.frqnny.darkenchanting.blockentity.inventory.DarkEnchanterInventory;
import io.github.frqnny.darkenchanting.config.ConfigEnchantment;
import io.github.frqnny.darkenchanting.init.ModGUIs;
import io.github.frqnny.darkenchanting.init.ModPackets;
import io.github.frqnny.darkenchanting.util.BookcaseUtils;
import io.github.frqnny.darkenchanting.util.XPUtil;
import io.netty.buffer.Unpooled;
import it.unimi.dsi.fastutil.objects.Object2IntLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
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

import java.util.*;

public class DarkEnchanterGUI extends SyncedGuiDescription {
    public final DarkEnchanterInventory inv;
    public final WBox box;
    public final WGridPanel root;
    public final List<WLabeledSlider> enchantmentSliders;
    public final ScreenHandlerContext context;
    public final Object2IntLinkedOpenHashMap<Enchantment> enchantmentsToApply;
    public final Object2IntLinkedOpenHashMap<Enchantment> enchantmentsOnStack;
    public final List<Enchantment> removedEnchantments;
    public final WButton enchantButton;
    public final WButton repairButton;
    public int enchantCost = 0;
    public int repairCost = 0;
    public double bookshelfDiscount = 0.0F;

    public DarkEnchanterGUI(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(ModGUIs.DARK_ENCHANTER_GUI, syncId, playerInventory);
        //special inventory designed to be like a crafting table's inventory so multiple people can use it at a time
        //this special inventory gets this gui, so when markDirty is called, fillBox is called to update the box's widgets
        this.inv = new DarkEnchanterInventory(this);
        this.context = context;
        //empty list, updated when markUpdate is called
        enchantmentSliders = new ArrayList<>(15);
        //empty list, also updated when markUpdate is called
        enchantmentsToApply = new Object2IntLinkedOpenHashMap<>();
        enchantmentsOnStack = new Object2IntLinkedOpenHashMap<>();
        removedEnchantments = new ArrayList<>(5);

        //set to 1 so pixel so we have per-pixel panels
        root = new WGridPanel(1);
        this.setRootPanel(root);
        root.setSize(235, 250);

        //main enchanting slot
        WItemSlot slot = WItemSlot.of(inv, 0);
        slot.setFilter((stack) -> inv.isValid(0, stack));
        root.add(slot, 37, 17);

        //creates the box and scrollpanel
        box = new WBox(Axis.VERTICAL);
        WScrollPanel scrollPanel = new WScrollPanel(box);
        root.add(scrollPanel, 65, 17, 150, 135);

        enchantButton = new WButton();
        root.add(enchantButton, 35, 60, 20, 20);
        enchantButton.setLabel(new LiteralText("E"));
        enchantButton.setOnClick(this::enchant);


        repairButton = new WButton();
        root.add(repairButton, 35, 85, 20, 20);
        repairButton.setLabel(new LiteralText("R"));
        repairButton.setOnClick(this::repair);
        recalculateRepairCost();

        WWidget enchantCost = new WWidget() {
            @Override
            public void paint(MatrixStack matrices, int x, int y, int mouseX, int mouseY) {
                Screen screen = MinecraftClient.getInstance().currentScreen;
                String string;
                if (DarkEnchanterGUI.this.enchantCost > 0) {
                    string = "Pay: " + DarkEnchanterGUI.this.enchantCost + " levels";
                } else {
                    string = "Receive: " + -DarkEnchanterGUI.this.enchantCost + " levels";
                }

                screen.renderTooltip(matrices, Arrays.asList(new LiteralText("Enchant Cost:"), new LiteralText(string)), x, y);
            }
        };
        WWidget repairCost = new WWidget() {
            @Override
            public void paint(MatrixStack matrices, int x, int y, int mouseX, int mouseY) {
                Screen screen = MinecraftClient.getInstance().currentScreen;
                String string = "Pay: " + DarkEnchanterGUI.this.repairCost + " levels";
                screen.renderTooltip(matrices, Arrays.asList(new LiteralText("Repair Cost:"), new LiteralText(string)), x, y);
            }
        };
        WWidget bookshelfDiscount = new WWidget() {
            @Override
            public void paint(MatrixStack matrices, int x, int y, int mouseX, int mouseY) {
                Screen screen = MinecraftClient.getInstance().currentScreen;

                screen.renderTooltip(matrices, Arrays.asList(new LiteralText("Bookshelf Discount:"), new LiteralText(DarkEnchanterGUI.this.bookshelfDiscount + " %")), x, y);
            }
        };
        root.add(enchantCost, -120, 43);
        root.add(repairCost, -120, 80);
        root.add(bookshelfDiscount, -120, 117);
        //everything else
        root.add(this.createPlayerInventoryPanel(true), 36, 153);

        root.validate(this);
    }

    public static Text getLabel(Enchantment enchantment, int level) {
        MutableText mutableText = new TranslatableText(enchantment.getTranslationKey());
        if (enchantment.isCursed()) {
            mutableText.formatted(Formatting.RED);
        } else {
            mutableText.formatted(Formatting.GRAY);
        }

        if (level > 0) {
            mutableText.append(" ").append(new TranslatableText("enchantment.level." + level));
        }

        return mutableText;
    }

    //Called by DarkEnchanterInventory#markDirty
    public void fillBox() {
        //remove all sliders
        for (WLabeledSlider slider : enchantmentSliders) {
            box.remove(slider);
        }
        //repopulate the list of widgets
        populateList();
        //read new sliders
        for (WLabeledSlider slider : enchantmentSliders) {
            box.add(slider, 140, 18);
        }


        recalculateEnchantmentCost();
        recalculateRepairCost();
        root.layout();
    }

    public void populateList() {
        //finally get rid of old widgets
        enchantmentSliders.clear();
        //get the stack
        ItemStack stack = inv.getActualStack();
        //avoid iterating over the list if empty
        if (stack.isEmpty()) {
            return;
        }
        //its enchantments
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.get(stack);
        //This iterates though every enchantment in the game
        for (Enchantment enchantment : Registry.ENCHANTMENT) {
            Optional<ConfigEnchantment> configEnchantmentOptional = ConfigEnchantment.getConfigEnchantmentFor(enchantment);

            //Check if the Config has disabled it
            if (configEnchantmentOptional.isPresent()) {
                ConfigEnchantment configEnchantment = configEnchantmentOptional.get();
                if (!configEnchantment.activated) {
                    continue;
                }
            }

            //If enchantment can be put on stack, put it on the stack
            if (enchantment.isAcceptableItem(stack)) {
                WLabeledSlider enchantmentSlider;
                if (enchantmentsToApply.containsKey(enchantment)) {
                    enchantmentSlider = addNewWidgetToList(enchantmentsToApply.getInt(enchantment), enchantment);
                } else if (enchantments.containsKey(enchantment)) {
                    if (!removedEnchantments.contains(enchantment)) {
                        enchantmentSlider = addNewWidgetToList(enchantments.get(enchantment), enchantment);
                    } else {
                        enchantmentSlider = addNewWidgetToList(0, enchantment);
                    }
                } else {
                    enchantmentSlider = addNewWidgetToList(0, enchantment);

                }

                for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
                    Enchantment enchantmentOnStack = entry.getKey();
                    //enchantment is Smite
                    //enchantmentOnStack is Sharpness
                    //if sharpness will be removed, we do not remove smite
                    if (!removedEnchantments.contains(enchantmentOnStack)) {
                        if (!enchantmentOnStack.canCombine(enchantment) && !enchantmentOnStack.equals(enchantment)) {
                            enchantmentSliders.remove(enchantmentSlider);
                        }
                    }
                }

                for (Object2IntMap.Entry<Enchantment> entry : enchantmentsToApply.object2IntEntrySet()) {
                    Enchantment enchantmentOnStack = entry.getKey();

                    if (!enchantmentOnStack.canCombine(enchantment) && !enchantmentOnStack.equals(enchantment)) {
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
        slider.setValueChangeListener((power) -> changeInMap(enchantment, power));
        slider.setHost(this);
        enchantmentSliders.add(slider);
        return slider;
    }

    public void changeInMap(Enchantment enchantment, int level) {
        if (enchantmentsToApply.containsKey(enchantment)) {
            enchantmentsToApply.replace(enchantment, level);
        } else {
            enchantmentsToApply.put(enchantment, level);
        }
        if (level == 0) {
            enchantmentsToApply.removeInt(enchantment);
            removedEnchantments.add(enchantment);
        }

        fillBox();
        recalculateEnchantmentCost();
    }

    public void recalculateEnchantmentCost() {
        this.context.run((world, blockPos) -> {
            int playerLevel = playerInventory.player.experienceLevel;
            enchantCost = BookcaseUtils.applyDiscount(XPUtil.getLevelCostFromMap(enchantmentsToApply, enchantmentsOnStack), world, blockPos);
            bookshelfDiscount = BookcaseUtils.getDiscount(world, blockPos) * 100;

            boolean enchantmentsHaveChanged = true;

            for (Object2IntMap.Entry<Enchantment> entrySet : enchantmentsToApply.object2IntEntrySet()) {
                Enchantment enchantment = entrySet.getKey();
                int level = entrySet.getIntValue();

                if (!(enchantmentsOnStack.containsKey(enchantment)) || !(enchantmentsOnStack.getInt(enchantment) == level)) {
                    enchantmentsHaveChanged = false;
                }
            }

            //If false already, we do not need to check
            if (enchantmentsHaveChanged) {
                for (Object2IntMap.Entry<Enchantment> entrySet : enchantmentsOnStack.object2IntEntrySet()) {
                    Enchantment enchantment = entrySet.getKey();
                    int level = entrySet.getIntValue();

                    if (!(enchantmentsToApply.containsKey(enchantment)) || !(enchantmentsToApply.getInt(enchantment) == level)) {
                        enchantmentsHaveChanged = false;
                    }
                }
            }


            enchantButton.setEnabled((playerLevel >= enchantCost && !enchantmentsHaveChanged) || playerInventory.player.isCreative());
        });

    }

    public void recalculateRepairCost() {
        repairCost = XPUtil.getRepairCostFromItemStack(inv.getActualStack());
        repairButton.setEnabled(inv.getActualStack().isDamaged() || playerInventory.player.isCreative());
    }

    public void enchant() {
        this.context.run((world1, blockPos) -> {
            PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
            buf.writeBlockPos(blockPos);
            buf.writeInt(enchantmentsToApply.size());
            enchantmentsToApply.forEach((enchantment, level) -> {
                buf.writeIdentifier(Registry.ENCHANTMENT.getId(enchantment));
                buf.writeInt(level);
            });
            this.getPacketSender().sendPacket(ModPackets.APPLY_ENCHANTMENTS, buf);
            this.inv.markDirty();
        });
    }

    public void repair() {
        this.context.run((world, blockPos) -> {
            PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
            buf.writeBlockPos(blockPos);
            this.getPacketSender().sendPacket(ModPackets.APPLY_REPAIR, buf);
        });
    }

    @Override
    public void close(PlayerEntity player) {
        super.close(player);
        this.context.run((world, blockPos) -> this.dropInventory(player, this.inv));
    }
}
