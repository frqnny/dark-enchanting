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
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.InventoryListener;
import net.minecraft.recipe.RecipeType;
import net.minecraft.text.LiteralText;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;


public class DEGuiController extends CottonCraftingController implements InventoryListener {
    private List<EnchDataContext> data = new ArrayList<>();
    private WListPanel listPanel;

    public DEGuiController(int syncId, PlayerInventory playerInventory, BlockContext context) {
        super(RecipeType.SMELTING, syncId, playerInventory, getBlockInventory(context), getBlockPropertyDelegate(context));
        WGridPanel rootPanel = (WGridPanel) getRootPanel();

        rootPanel.add(new WLabel(new LiteralText("Dark Enchanter"), WLabel.DEFAULT_TEXT_COLOR), 0, 0);

        WItemSlot slot = new WItemSlot(blockInventory, 0, 1,1,true, false);
        rootPanel.add(slot, 1, 1);

        rootPanel.add(this.createPlayerInventoryPanel(), 0, 5);

        BiConsumer<EnchDataContext, WEnchantment> configurator = (EnchDataContext ctx, WEnchantment widget) ->
            widget.set(ctx.getEnchantment(), ctx.getLevel(), context);

        data = EnchantingHelperEvent.getDataList(blockInventory.getInvStack(0));
        listPanel = new WListPanel(data, WEnchantment.class, new WEnchantment(Enchantments.SHARPNESS, context), configurator);
        listPanel.layout();
        rootPanel.add(listPanel, 3, 1, 6,0);
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

    @Override
    public void onInvChange(Inventory var1) {
        updateGUI();
    }
}