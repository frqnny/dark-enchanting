package io.github.frqnny.darkenchanting.blockentity;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.MathHelper;

import java.util.Random;

public class BlockEntityWithBook extends BlockEntity implements Tickable {
    private static final Random RANDOM = new Random();
    public int ticks;
    public float nextPageAngle;
    public float pageAngle;
    public float flipRandom;
    public float flipTurn;
    public float nextPageTurningSpeed;
    public float pageTurningSpeed;
    public float bookRotation;
    public float bookRotationPrev;
    public float offset;

    public BlockEntityWithBook(BlockEntityType<?> type) {
        super(type);
    }

    @Override
    public void tick() {
        this.pageTurningSpeed = this.nextPageTurningSpeed;
        this.bookRotationPrev = this.bookRotation;
        PlayerEntity playerEntity = this.world.getClosestPlayer((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D, (double) this.pos.getZ() + 0.5D, 3.0D, false);
        if (playerEntity != null) {
            double d = playerEntity.getX() - ((double) this.pos.getX() + 0.5D);
            double e = playerEntity.getZ() - ((double) this.pos.getZ() + 0.5D);
            this.offset = (float) MathHelper.atan2(e, d);
            this.nextPageTurningSpeed += 0.1F;
            if (this.nextPageTurningSpeed < 0.5F || RANDOM.nextInt(40) == 0) {
                float f = this.flipRandom;

                do {
                    this.flipRandom += (float) (RANDOM.nextInt(4) - RANDOM.nextInt(4));
                } while (f == this.flipRandom);
            }
        } else {
            this.offset += 0.02F;
            this.nextPageTurningSpeed -= 0.1F;
        }

        while (this.bookRotation >= 3.1415927F) {
            this.bookRotation -= 6.2831855F;
        }

        while (this.bookRotation < -3.1415927F) {
            this.bookRotation += 6.2831855F;
        }

        while (this.offset >= 3.1415927F) {
            this.offset -= 6.2831855F;
        }

        while (this.offset < -3.1415927F) {
            this.offset += 6.2831855F;
        }

        float g;

        for (g = this.offset - this.bookRotation; g >= 3.1415927F; ) {
            g -= 6.2831855F;
        }

        while (g < -3.1415927F) {
            g += 6.2831855F;
        }

        this.bookRotation += g * 0.4F;
        this.nextPageTurningSpeed = MathHelper.clamp(this.nextPageTurningSpeed, 0.0F, 1.0F);
        ++this.ticks;
        this.pageAngle = this.nextPageAngle;
        float h = (this.flipRandom - this.nextPageAngle) * 0.4F;
        float i = 0.2F;
        h = MathHelper.clamp(h, -0.2F, 0.2F);
        this.flipTurn += (h - this.flipTurn) * 0.9F;
        this.nextPageAngle += this.flipTurn;
    }
}
