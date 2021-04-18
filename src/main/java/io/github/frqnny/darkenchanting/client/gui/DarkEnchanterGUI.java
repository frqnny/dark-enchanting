package io.github.frqnny.darkenchanting.client.gui;

import io.github.cottonmc.cotton.gui.SyncedGuiDescription;
import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.data.Axis;
import io.github.frqnny.darkenchanting.blockentity.inventory.DarkEnchanterInventory;
import io.github.frqnny.darkenchanting.init.ModGUIs;
import io.github.frqnny.darkenchanting.init.ModPackets;
import io.github.frqnny.darkenchanting.util.XPUtil;
import io.netty.buffer.Unpooled;
import it.unimi.dsi.fastutil.objects.Object2IntLinkedOpenHashMap;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DarkEnchanterGUI extends SyncedGuiDescription {
    public final DarkEnchanterInventory inv;
    public final WBox box;
    public final WScrollPanel scrollPanel;
    public final WGridPanel root;
    public final List<WLabeledSlider> enchantmentSliders;
    public final ScreenHandlerContext context;
    public final Object2IntLinkedOpenHashMap<Enchantment> enchantmentsToApply;
    public final Object2IntLinkedOpenHashMap<Enchantment> enchantmentsOnStack;
    public final List<Enchantment> removedEnchantments;
    public final WButton enchantButton;
    public int level = 0;

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
        root.setSize(235, 182);

        //main enchanting slot
        WItemSlot slot = WItemSlot.of(inv, 0);
        slot.setFilter((stack) -> inv.isValid(0, stack));
        root.add(slot, 37, 17);

        //creates the box and scrollpanel
        box = new WBox(Axis.VERTICAL);
        scrollPanel = new WScrollPanel(box);
        root.add(scrollPanel, 65, 12, 150, 75);

        enchantButton = new WButton();
        root.add(enchantButton, 35, 60, 20, 20);
        enchantButton.setLabel(new LiteralText("E"));
        enchantButton.setOnClick(this::enchant);

        WWidget label = new WWidget() {
            @Override
            public void paint(MatrixStack matrices, int x, int y, int mouseX, int mouseY) {
                Screen screen = MinecraftClient.getInstance().currentScreen;
                String string;
                if (level > 0) {
                    string = "Pay: " + level + " levels";
                } else {
                    string = "Receive: " + -level + " levels";
                }
                screen.renderTooltip(matrices, new LiteralText(string), x, y);
            }
        };
        root.add(label, -120, 43);
        //everything else
        root.add(this.createPlayerInventoryPanel(true), 36, 91);

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
        root.layout();
        root.setHost(this);
        scrollPanel.setHost(this);
        box.setHost(this);
    }

    public void populateList() {
        //finally get rid of old widgets
        enchantmentSliders.clear();
        //get the stack
        ItemStack stack = inv.getActualStack();
        //its enchantments
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.get(stack);
        //This iterates though every enchantment in the game
        for (Enchantment enchantment : Registry.ENCHANTMENT) {
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


                enchantments.forEach((enchantmentOnStack, level) -> {
                    //enchantment is Smite
                    //enchantmentOnStack is Sharpness
                    //if sharpness will be removed, we do not remove smite
                    if (!removedEnchantments.contains(enchantmentOnStack)) {
                        if (!enchantmentOnStack.canCombine(enchantment) && !enchantmentOnStack.equals(enchantment)) {
                            enchantmentSliders.remove(enchantmentSlider);
                        }
                    }

                });


                enchantmentsToApply.forEach((enchantmentOnStack, level) -> {
                    if (!enchantmentOnStack.canCombine(enchantment) && !enchantmentOnStack.equals(enchantment)) {
                        enchantmentSliders.remove(enchantmentSlider);
                    }
                });

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
        level = XPUtil.getLevelCostFromMap(enchantmentsToApply, enchantmentsOnStack);
        this.context.run((world, blockPos) -> {
            int playerLevel = playerInventory.player.experienceLevel;


            enchantButton.setEnabled((playerLevel >= level && level != 0) || playerInventory.player.isCreative());
        });

    }

    public void enchant() {
        this.context.run(((world1, blockPos) -> {
            ItemStack stack = inv.getStack(0);
            EnchantmentHelper.set(enchantmentsToApply, stack);

            PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
            buf.writeInt(enchantmentsToApply.size());
            enchantmentsToApply.forEach((enchantment, level) -> {
                buf.writeIdentifier(Registry.ENCHANTMENT.getId(enchantment));
                buf.writeInt(level);
            });
            this.getPacketSender().sendPacket(ModPackets.APPLY_SINGLE_ENCHANTMENT, buf);
            XPUtil.applyXp(playerInventory.player, enchantmentsToApply, enchantmentsOnStack);
            this.inv.markDirty();
        }));

        enchantmentsToApply.clear();
        fillBox();
    }

    @Override
    public void close(PlayerEntity player) {
        super.close(player);
        this.context.run((world, blockPos) -> {
            this.dropInventory(player, player.world, this.inv);
        });
    }
}
