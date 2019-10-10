package io.github.franiscoder.darkenchanting.api.events;

import io.github.franiscoder.darkenchanting.api.EnchDataContext;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EnchantingHelperEvent {
    public static List<EnchDataContext> getDataList(ItemStack stack) {
        List<EnchDataContext> data = new ArrayList<>();
        Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(stack);
        map.forEach(
                ((enchantment, integer) -> {
                    data.add(EnchDataContext.create(enchantment, integer));
                })
        );
        //Make sure an that if an Enchantment API ever gets made, to update this. 
        Registry.ENCHANTMENT.forEach(
                (enchantment -> {
                    if (enchantment.isAcceptableItem(stack)) {
                        data.add(EnchDataContext.create(enchantment, 0));
                    }
                })
        );
        return data;
    }
}
