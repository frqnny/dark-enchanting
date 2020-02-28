package io.github.franiscoder.darkenchanting.gui;

import io.github.cottonmc.cotton.gui.CottonCraftingController;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WItemSlot;
import io.github.cottonmc.cotton.gui.widget.WLabeledSlider;
import io.github.cottonmc.cotton.gui.widget.WListPanel;
import io.github.franiscoder.darkenchanting.DarkEnchanting;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.container.BlockContext;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.InfoEnchantment;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeType;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.Map;
import java.util.function.BiConsumer;

public class DarkEnchanterGUI extends CottonCraftingController {
    private WListPanel<InfoEnchantment, WLabeledSlider> listPanel;
    private ArrayList<InfoEnchantment> data;

    public DarkEnchanterGUI(int syncId, PlayerInventory playerInventory, BlockContext context) {
        super(RecipeType.SMELTING, syncId, playerInventory, getBlockInventory(context), getBlockPropertyDelegate(context));

        WGridPanel root = new WGridPanel();
        setRootPanel(root);
        root.setSize(200, 200);

        WItemSlot item = WItemSlot.outputOf(blockInventory, 0);
        root.add(item, 5, 6);

        BiConsumer<InfoEnchantment, WLabeledSlider> configurator = (InfoEnchantment info, WLabeledSlider slider) -> {
            slider.setDraggingFinishedListener((level) ->
                    enchant(blockInventory.getInvStack(0), info.enchantment, level, context)
            );
            slider.setMinValue(0);
            slider.setMaxValue(info.enchantment.getMaximumLevel());
            slider.setLabel(new TranslatableText(info.enchantment.getTranslationKey()));
            slider.setValue(info.level);
        };
        data = getDataList(blockInventory.getInvStack(0));
        listPanel = new WListPanel<>(data, () -> new WLabeledSlider(0, 1), configurator);
        root.add(listPanel, 2, 1, 7, 4);

        root.add(this.createPlayerInventoryPanel(), 1, 8);
        root.validate(this);
    }

    public static ArrayList<InfoEnchantment> getDataList(ItemStack stack) {
        ArrayList<InfoEnchantment> data = new ArrayList<>();

        Registry.ENCHANTMENT.forEach(
                (enchantment -> {
                    if (enchantment.isAcceptableItem(stack)) {
                        data.add(new InfoEnchantment(enchantment, 0));
                    }
                }));

        EnchantmentHelper.getEnchantments(stack).forEach((enchantment, level) -> {
            data.removeIf(ench -> ench.enchantment == enchantment);
            data.add(new InfoEnchantment(enchantment, level));
        });

        return data;
    }

    public void enchant(ItemStack stack, Enchantment enchantment, int value, BlockContext context) {
        Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(stack);
        map.put(enchantment, value);
        EnchantmentHelper.set(map, stack);
        context.run(
                (world, pos) -> {
                    PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
                    passedData.writeBlockPos(pos);
                    passedData.writeIdentifier(Registry.ENCHANTMENT.getId(enchantment));
                    passedData.writeInt(value);
                    //System.out.println("Sending Packet...");
                    ClientSidePacketRegistry.INSTANCE.sendToServer(DarkEnchanting.ENCHANT_PACKET, passedData);
                    //System.out.println("Packet Sent!");
                }
        );
    }

    public void reloadListPanel() {
        data = getDataList(blockInventory.getInvStack(0));
        listPanel.layout();
    }

}
