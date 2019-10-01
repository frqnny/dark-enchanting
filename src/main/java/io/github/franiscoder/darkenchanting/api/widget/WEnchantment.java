package io.github.franiscoder.darkenchanting.api.widget;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import javax.annotation.Nullable;
import java.util.Map;

public class WEnchantment extends WLB {
    @Nullable
    private WEnchantment.LabelUpdater labelUpdater = null;
    @Nullable
    public Text enchantmentName;
    private Enchantment enchantment;
    private Inventory inv;


    public WEnchantment(Enchantment enchantment, Inventory inv) {
        super(0, enchantment.getMaximumLevel());
        this.enchantmentName = new TranslatableText(enchantment.getTranslationKey());
        this.enchantment = enchantment;
        this.inv = inv;

    }

    private ItemStack stack = inv.getInvStack(0);

    @Override
    protected void onValueChanged(int value) {
        super.onValueChanged(value);
        if (labelUpdater != null) {
            enchantmentName = labelUpdater.updateLabel(value);
        }
        Map<Enchantment, Integer> map = EnchantmentHelper.method_22445(stack.getEnchantments());
        if (value != 0 && !map.containsKey(enchantment)) {
            stack.addEnchantment(enchantment, value);
        } else if (value != 0) {
            map.remove(enchantment);
            map.put(enchantment, value);
            EnchantmentHelper.set(map, stack);
        }
    }
}