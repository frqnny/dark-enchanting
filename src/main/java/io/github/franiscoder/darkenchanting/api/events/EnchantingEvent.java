package io.github.franiscoder.darkenchanting.api.events;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.InfoEnchantment;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.registry.Registry;

import java.util.Iterator;
import java.util.Map;

//to be used in a single enchanting event, aka one use of WEnchantment
public class EnchantingEvent {
    public static ItemStack set(Map<Enchantment, Integer> map, ItemStack stack) {
        ListTag listTag = new ListTag();
        Iterator iterator = map.entrySet().iterator();

        while(iterator.hasNext()) {
            Map.Entry<Enchantment, Integer> mapEntry = (Map.Entry)iterator.next();
            Enchantment enchantment_1 = mapEntry.getKey();
            if (enchantment_1 != null) {
                int int_1 = mapEntry.getValue();
                CompoundTag compoundTag_1 = new CompoundTag();
                compoundTag_1.putString("id", String.valueOf(Registry.ENCHANTMENT.getId(enchantment_1)));
                compoundTag_1.putShort("lvl", (short)int_1);
                listTag.add(compoundTag_1);
                if (stack.getItem() == Items.ENCHANTED_BOOK) {
                    EnchantedBookItem.addEnchantment(stack, new InfoEnchantment(enchantment_1, int_1));
                }
            }
        }

        if (listTag.isEmpty()) {
            stack.removeSubTag("Enchantments");
        } else if (stack.getItem() != Items.ENCHANTED_BOOK) {
            stack.putSubTag("Enchantments", listTag);
        }
        return stack;
    }
}
