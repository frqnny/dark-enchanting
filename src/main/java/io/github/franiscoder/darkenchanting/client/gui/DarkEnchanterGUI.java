package io.github.franiscoder.darkenchanting.client.gui;

import com.google.common.collect.ImmutableList;
import io.github.cottonmc.cotton.gui.CottonCraftingController;
import io.github.cottonmc.cotton.gui.widget.*;
import io.github.franiscoder.darkenchanting.DarkEnchanting;
import io.github.franiscoder.darkenchanting.client.gui.widget.WItemScreen;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.container.BlockContext;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.InfoEnchantment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeType;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.function.BiConsumer;

public class DarkEnchanterGUI extends CottonCraftingController {
    private WListPanel<InfoEnchantment, WLabeledSlider> listPanel;
    private ArrayList<InfoEnchantment> data;
    private final BlockContext context;
    private final Inventory inventory;
    private Map<Enchantment, Integer> originalMap;
    private int levelCost = 0;
    private boolean enchanted = false;

    public DarkEnchanterGUI(int syncId, PlayerInventory playerInventory, BlockContext cxt) {
        super(RecipeType.SMELTING, syncId, playerInventory, getBlockInventory(cxt), getBlockPropertyDelegate(cxt));
        WGridPanel root = new WGridPanel();
        setRootPanel(root);
        root.setSize(200, 200);
        if (blockInventory.getInvStack(0) == ItemStack.EMPTY) {
            PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
            passedData.writeItemStack(playerInventory.getInvStack(0));
            cxt.run(
                    (world, pos) -> {
                        passedData.writeBlockPos(pos);
                    });
            ClientSidePacketRegistry.INSTANCE.sendToServer(DarkEnchanting.FIRST_PACKET, passedData);

            blockInventory.setInvStack(0, playerInventory.takeInvStack(playerInventory.selectedSlot, 1));
        }

        WLabel title = new WLabel("Dark Enchanter");
        root.add(title, 0, 0);

        context = cxt;
        inventory = blockInventory;
        originalMap = EnchantmentHelper.getEnchantments(blockInventory.getInvStack(0));
        Map<Enchantment, Integer> newMap = originalMap;

        WItemScreen item = new WItemScreen(blockInventory.getInvStack(0));
        item.addInformation(ImmutableList.of("Price:", levelCost + " levels"));
        root.add(item, 5, 6);

        WButton enchant = new WButton(new LiteralText("Enchant")).setOnClick(() -> {
            ItemStack stack = blockInventory.takeInvStack(0, 1);
            playerInventory.insertStack(playerInventory.getEmptySlot(), stack);

            PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
            cxt.run(
                    (world, pos) -> {
                        passedData.writeBlockPos(pos);
                    }
            );
            ClientSidePacketRegistry.INSTANCE.sendToServer(DarkEnchanting.FINAL_PACKET, passedData);
            playerInventory.player.experienceLevel -= levelCost;

            enchanted = true;
        });
        root.add(enchant, 1, 6, 3, 1);

        BiConsumer<InfoEnchantment, WLabeledSlider> configurator = (InfoEnchantment info, WLabeledSlider slider) -> {
            if (playerInventory.player.experienceLevel > 0 || playerInventory.player.isCreative()) {
                slider.setDraggingFinishedListener((level) -> {

                    enchant(blockInventory.getInvStack(0), info.enchantment, level, context);
                    data = getDataList(blockInventory.getInvStack(0));
                    listPanel.layout();

                    levelCost += (int) calculateCost(info.enchantment, info.level, level);
                    item.addInformation(ImmutableList.of("Price:", levelCost + " levels"));

                    if (levelCost > playerInventory.player.experienceLevel) {
                        enchant.setEnabled(false);
                    } else if (levelCost > playerInventory.player.experienceLevel){
                        enchant.setEnabled(true);
                    }
                });
            } else {
                slider.addInformation(Collections.singletonList("Not enough xp!"));
            }
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

    //TODO Cost: configController * enchantmentLevel * weight * discount
    public double calculateCost(Enchantment enchantment, int oldLevel, int newLevel) {
        int levelChange = newLevel - oldLevel;
        double cost = 0.3;
        cost *= 1.0; //configController
        cost *= levelChange;
        cost *= 1.0 / enchantment.getWeight().getWeight();

        return cost;
    }

    @Override
    public void close(PlayerEntity player) {
        if (!enchanted) {
            super.close(player);

            //resets the item since it should have been clicked on enchantment,
            ItemStack stack = getBlockInventory(context).getInvStack(0);
            EnchantmentHelper.set(originalMap, stack);
            getBlockInventory(context).setInvStack(0, stack);

            this.context.run((world, blockPos) -> {
                this.dropInventory(player, world, this.inventory);
            });
        }
    }
}
