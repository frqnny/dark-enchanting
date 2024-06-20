package io.github.frqnny.darkenchanting.screen;

import io.github.frqnny.darkenchanting.blockentity.inventory.DarkEnchanterInventory;
import io.github.frqnny.darkenchanting.init.ModBlocks;
import io.github.frqnny.darkenchanting.init.ModGUIs;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.math.BlockPos;

public class DarkEnchanterScreenHandler extends ScreenHandler {
    public final DarkEnchanterInventory inv = new DarkEnchanterInventory(this);
    private final ScreenHandlerContext context;
    protected PlayerInventory playerInventory;
    private boolean stackUpdate = false;

    public DarkEnchanterScreenHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(ModGUIs.DARK_ENCHANTER, syncId);
        this.context = context;
        this.playerInventory = playerInventory;

        //enchantment slot
        this.addSlot(new Slot(this.inv, 0, 15, 47) {
            @Override
            public int getMaxItemCount() {
                return 1;
            }
        });

        //player inventory
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 9; x++) {
                int index = x + y * 9 + 9;
                int xOffset = x * 18;
                int yOffset = y * 18;
                this.addSlot(new Slot(playerInventory, index, 8 + xOffset, 184 + yOffset));
            }
        }
        //hotbar
        for (int x = 0; x < 9; x++) {
            int xOffset = x * 18;
            this.addSlot(new Slot(playerInventory, x, 8 + xOffset, 242));
        }
    }


    @Override
    public ItemStack quickMove(PlayerEntity player, int slotIndex) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(slotIndex);
        if (slot.hasStack()) {
            ItemStack itemStack2 = slot.getStack();
            itemStack = itemStack2.copy();
            if (slotIndex == 0) {
                if (!this.insertItem(itemStack2, 1, 37, true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (this.slots.getFirst().hasStack() || !this.slots.getFirst().canInsert(itemStack2)) {
                    return ItemStack.EMPTY;
                }

                ItemStack itemStack3 = itemStack2.copyWithCount(1);
                itemStack2.decrement(1);
                this.slots.getFirst().setStack(itemStack3);
            }

            if (itemStack2.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }

            if (itemStack2.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTakeItem(player, itemStack2);
        }

        return itemStack;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return ScreenHandler.canUse(this.context, player, ModBlocks.DARK_ENCHANTER);
    }

    public BlockPos getPos() {
        var position = context.get((world, pos) -> pos);
        return position.orElse(BlockPos.ORIGIN);
    }


    public void onStackUpdate() {
        this.stackUpdate = true;
    }


    public ItemStack getActualStack() {
        return this.inv.getActualStack();
    }

    public boolean hasStackUpdate() {
        return this.stackUpdate;
    }

    public void handleStackUpdate() {
        this.stackUpdate = false;
    }

    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        this.context.run((world, pos) -> this.dropInventory(player, this.inv));
    }
}
