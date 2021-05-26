package io.github.frqnny.darkenchanting.blockentity.inventory;

import io.github.frqnny.darkenchanting.client.gui.DarkEnchanterGUI;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;

import java.util.Map;

public class DarkEnchanterInventory implements Inventory {
    private final DefaultedList<ItemStack> stacks;
    private final DarkEnchanterGUI handler;

    public DarkEnchanterInventory(DarkEnchanterGUI h) {
        this.handler = h;
        stacks = DefaultedList.ofSize(1, ItemStack.EMPTY);
    }

    @Override
    public int size() {
        return stacks.size();
    }

    @Override
    public boolean isEmpty() {
        int size = size();
        for (int i = 0; i < size; i++) {
            ItemStack stack = getStack(i);
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public ItemStack getActualStack() {
        return getStack(0);
    }

    @Override
    public ItemStack getStack(int slot) {
        return stacks.get(slot);
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        ItemStack result = Inventories.splitStack(stacks, slot, amount);
        if (!result.isEmpty()) {
            markDirty();
        }
        return result;
    }

    @Override
    public ItemStack removeStack(int slot) {
        return Inventories.removeStack(stacks, slot);
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        stacks.set(slot, stack);
        if (stack.getCount() > getMaxCountPerStack()) {
            stack.setCount(getMaxCountPerStack());
        }
    }

    @Override
    public void markDirty() {
        handler.fillBox();
        handler.enchantmentsOnStack.clear();
        handler.enchantmentsToApply.clear();
        handler.removedEnchantments.clear();
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.get(this.getStack(0));
        enchantments.forEach((enchantment, level) -> {
            if (!handler.enchantmentsToApply.containsKey(enchantment)) {
                handler.enchantmentsToApply.put(enchantment, (int) level);
            }
            handler.enchantmentsOnStack.put(enchantment, (int) level);

        });
        handler.level = 0;
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return true;
    }

    @Override
    public void clear() {
        stacks.clear();
    }

    @Override
    public boolean isValid(int slot, ItemStack stack) {
        return stack.isEnchantable() || stack.hasEnchantments();
    }
}
