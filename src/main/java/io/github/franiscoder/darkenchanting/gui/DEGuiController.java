package io.github.franiscoder.darkenchanting.gui;

import io.github.cottonmc.cotton.gui.CottonCraftingController;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WItemSlot;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.franiscoder.darkenchanting.api.widget.WEnchantment;
import io.github.franiscoder.darkenchanting.blockentity.inventory.ImplementedInventory;
import net.minecraft.container.BlockContext;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.recipe.RecipeType;
import net.minecraft.text.LiteralText;

public class DEGuiController extends CottonCraftingController {
    public DEGuiController(int syncId, PlayerInventory playerInventory, BlockContext context) {
        super(RecipeType.SMELTING, syncId, playerInventory, getBlockInventory(context), getBlockPropertyDelegate(context));

        WGridPanel rootPanel = (WGridPanel) getRootPanel();

        rootPanel.add(new WLabel(new LiteralText("Dark Enchanter"), WLabel.DEFAULT_TEXT_COLOR), 0, 0);

        WItemSlot inputSlot1 = WItemSlot.of(blockInventory, 0);
        rootPanel.add(inputSlot1, 1, 1);


        //WLabeledSlider slider = new WLabeledSlider(0, 4, Axis.HORIZONTAL, new LiteralText("hi"));
        //rootPanel.add(slider, 3, 1, 5, 1)
        WEnchantment enchantment = new WEnchantment(Enchantments.SHARPNESS, blockInventory);
        rootPanel.add(enchantment, 3,1, 5,1);




        rootPanel.add(this.createPlayerInventoryPanel(), 0, 5);

        rootPanel.validate(this);
    }
    @Override
    public int getCraftingResultSlotIndex() {
        return -1; //There's no real result slot
    }
}