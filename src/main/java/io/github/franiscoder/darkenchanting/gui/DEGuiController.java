package io.github.franiscoder.darkenchanting.gui;

import io.github.cottonmc.cotton.gui.CottonCraftingController;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WItemSlot;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WListPanel;
import io.github.franiscoder.darkenchanting.api.EnchDataContext;
import io.github.franiscoder.darkenchanting.api.events.EnchantingHelperEvent;
import io.github.franiscoder.darkenchanting.api.widget.WEnchantment;
import net.minecraft.container.BlockContext;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.recipe.RecipeType;
import net.minecraft.text.LiteralText;

import java.util.List;
import java.util.function.BiConsumer;


public class DEGuiController extends CottonCraftingController {
    private static List<EnchDataContext> data;
    private static WListPanel<EnchDataContext, WEnchantment> listPanel;

    public DEGuiController(int syncId, PlayerInventory playerInventory, BlockContext context) {
        super(RecipeType.SMELTING, syncId, playerInventory, getBlockInventory(context), getBlockPropertyDelegate(context));
        WGridPanel rootPanel = new WGridPanel(27);
        setRootPanel(rootPanel);

        rootPanel.add(new WLabel(new LiteralText("Dark Enchanter"), WLabel.DEFAULT_TEXT_COLOR), 0, 0);

        WItemSlot slot = WItemSlot.outputOf(blockInventory, 0);
        rootPanel.add(slot, 1, 1);

        //WButton loadEnchantments = new WButton();
        //loadEnchantments.setOnClick(this::updateGUI);
        //rootPanel.add(loadEnchantments, 1, 3);

        BiConsumer<EnchDataContext, WEnchantment> configurator = (EnchDataContext ctx, WEnchantment widget) ->
                widget.set(ctx.getEnchantment(), ctx.getLevel(), context);

        data = EnchantingHelperEvent.getDataList(blockInventory.getInvStack(0));
        listPanel = new WListPanel<EnchDataContext, WEnchantment>(data, () -> new WEnchantment(Enchantments.SHARPNESS, context), configurator);
        rootPanel.add(listPanel, 2, 1, 6, 3);
        listPanel.layout();

        listPanel.layout();
        rootPanel.add(this.createPlayerInventoryPanel(), 0, 5);
        rootPanel.layout();
        rootPanel.validate(this);

    }

    @Override
    public int getCraftingResultSlotIndex() {
        return -1; //There's no real result slot
    }

    private void updateGUI() {
        data = EnchantingHelperEvent.getDataList(blockInventory.getInvStack(0));
        listPanel.layout();
    }
}