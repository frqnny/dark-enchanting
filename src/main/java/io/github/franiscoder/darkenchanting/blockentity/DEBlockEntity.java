package io.github.franiscoder.darkenchanting.blockentity;


import io.github.franiscoder.darkenchanting.blockentity.inventory.ImplementedInventory;
import io.github.franiscoder.darkenchanting.init.BlockEntities;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.MathHelper;

import java.util.Random;

public class DEBlockEntity extends BlockEntity implements ImplementedInventory, Tickable {
    private final DefaultedList<ItemStack> items = DefaultedList.ofSize(1, ItemStack.EMPTY);


    public DEBlockEntity() {
        super(BlockEntities.DE_BLOCK_ENTITY);
    }

    // Things from Mojang's Page turner, so that's why it has field_xxxxx
    //It's hard to implement the rendered of the book without it being mapped and you knowing what to do.
    public int ticks;
    public float nextPageAngle;
    public float pageAngle;
    private float field_11969;
    private float field_11967;
    public float nextPageTurningSpeed;
    public float pageTurningSpeed;
    public float field_11964;
    public float field_11963;
    private float field_11962;
    private static final Random RANDOM = new Random();

    @Override
    public DefaultedList<ItemStack> getItems() {
        return items;
    }

    @Override
    public boolean canPlayerUseInv(PlayerEntity player) {
        return pos.isWithinDistance(player.getBlockPos(), 5.5D);
    }

    @Override
    public void fromTag(CompoundTag tag) {
        super.fromTag(tag);
        Inventories.fromTag(tag, items);
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        Inventories.toTag(tag, items);
        return super.toTag(tag);
    }

    @Override
    public void tick() {
        this.pageTurningSpeed = this.nextPageTurningSpeed;
        this.field_11963 = this.field_11964;
        PlayerEntity playerEntity_1 = this.world.getClosestPlayer((float) this.pos.getX() + 0.5F, (float) this.pos.getY() + 0.5F, (float) this.pos.getZ() + 0.5F, 3.0D, false);
        if (playerEntity_1 != null) {
            double double_1 = playerEntity_1.x - (double) ((float) this.pos.getX() + 0.5F);
            double double_2 = playerEntity_1.z - (double) ((float) this.pos.getZ() + 0.5F);
            this.field_11962 = (float) MathHelper.atan2(double_2, double_1);
            this.nextPageTurningSpeed += 0.1F;
            if (this.nextPageTurningSpeed < 0.5F || RANDOM.nextInt(40) == 0) {
                float float_1 = this.field_11969;

                do {
                    this.field_11969 += (float) (RANDOM.nextInt(4) - RANDOM.nextInt(4));
                } while (float_1 == this.field_11969);
            }
        } else {
            this.field_11962 += 0.02F;
            this.nextPageTurningSpeed -= 0.1F;
        }

        while (this.field_11964 >= 3.1415927F) {
            this.field_11964 -= 6.2831855F;
        }

        while (this.field_11964 < -3.1415927F) {
            this.field_11964 += 6.2831855F;
        }

        while (this.field_11962 >= 3.1415927F) {
            this.field_11962 -= 6.2831855F;
        }

        while (this.field_11962 < -3.1415927F) {
            this.field_11962 += 6.2831855F;
        }

        float float_2 = this.field_11962 - this.field_11964;
        while (float_2 >= 3.1415927F) {
            float_2 -= 6.2831855F;
        }

        while (float_2 < -3.1415927F) {
            float_2 += 6.2831855F;
        }

        this.field_11964 += float_2 * 0.4F;
        this.nextPageTurningSpeed = MathHelper.clamp(this.nextPageTurningSpeed, 0.0F, 1.0F);
        ++this.ticks;
        this.pageAngle = this.nextPageAngle;
        float float_3 = (this.field_11969 - this.nextPageAngle) * 0.4F;
        //float float_4 = 0.2F;
        float_3 = MathHelper.clamp(float_3, -0.2F, 0.2F);
        this.field_11967 += (float_3 - this.field_11967) * 0.9F;
        this.nextPageAngle += this.field_11967;
    }
}
