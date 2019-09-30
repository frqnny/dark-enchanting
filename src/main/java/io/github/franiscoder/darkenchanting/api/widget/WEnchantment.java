package io.github.franiscoder.darkenchanting.api.widget;

import io.github.franiscoder.darkenchanting.blockentity.inventory.ImplementedInventory;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.ListTag;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class WEnchantment extends WLB {
    @Nullable
    private WEnchantment.LabelUpdater labelUpdater = null;
    @Nullable
    private Text enchantmentName;
    private Enchantment enchantment;
    private Inventory inv;
    public ItemStack stack;


    public WEnchantment(Enchantment enchantment, Inventory inv) {
        super(0, enchantment.getMaximumLevel());
        this.enchantmentName = new TranslatableText(enchantment.toString());
        this.enchantment = enchantment;
        this.inv = inv;
        this.stack = inv.getInvStack(0);

    }

    @Override
    protected void onValueChanged(int value) {
        super.onValueChanged(value);
        if (labelUpdater != null) {
            enchantmentName = labelUpdater.updateLabel(value);
        }
        ListTag lt = stack.getEnchantments();
        boolean hi = lt.contains(Enchantments.SHARPNESS);
        if (value != 0 && hi) {
            stack.addEnchantment(enchantment, value);
        }
    }
}