package io.github.franiscoder.darkenchanting.api.events;

import io.github.franiscoder.darkenchanting.api.EnchDataContext;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.Map;

public class EnchantingHelperEvent {
    public static ArrayList<EnchDataContext> getDataList(ItemStack stack) {
        ArrayList<EnchDataContext> data = new ArrayList<>();
        Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(stack);
        map.forEach(
                ((enchantment, integer) -> {
                    data.add(new EnchDataContext(enchantment, integer));
                })
        );
        //Make sure an that if an Enchantment API ever gets made, to update this. 
        Registry.ENCHANTMENT.stream().forEach(
                (enchantment -> {
                    if (enchantment.isAcceptableItem(stack)) {
                        data.add(new EnchDataContext(enchantment, 0));
                    }
                })
        );
        return data;
    }
}
