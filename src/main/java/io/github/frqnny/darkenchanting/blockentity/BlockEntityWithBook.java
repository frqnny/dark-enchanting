package io.github.frqnny.darkenchanting.blockentity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.Random;

public class BlockEntityWithBook extends BlockEntity {
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

    public BlockEntityWithBook(BlockEntityType<?> t, BlockPos pos, BlockState state) {
        super(t, pos, state);
    }

    public static void tick(World world, BlockPos pos, DarkEnchanterBlockEntity be) {
        be.pageTurningSpeed = be.nextPageTurningSpeed;
        be.bookRotationPrev = be.bookRotation;
        PlayerEntity player = world.getClosestPlayer((double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D, 3.0D, false);
        if (player != null) {
            double d = player.getX() - ((double) pos.getX() + 0.5D);
            double e = player.getZ() - ((double) pos.getZ() + 0.5D);
            be.offset = (float) MathHelper.atan2(e, d);
            be.nextPageTurningSpeed += 0.1F;
            if (be.nextPageTurningSpeed < 0.5F || RANDOM.nextInt(40) == 0) {
                float f = be.flipRandom;

                do {
                    be.flipRandom += (float) (RANDOM.nextInt(4) - RANDOM.nextInt(4));
                } while (f == be.flipRandom);
            }
        } else {
            be.offset += 0.02F;
            be.nextPageTurningSpeed -= 0.1F;
        }

        while (be.bookRotation >= 3.1415927F) {
            be.bookRotation -= 6.2831855F;
        }

        while (be.bookRotation < -3.1415927F) {
            be.bookRotation += 6.2831855F;
        }

        while (be.offset >= 3.1415927F) {
            be.offset -= 6.2831855F;
        }

        while (be.offset < -3.1415927F) {
            be.offset += 6.2831855F;
        }

        float g;

        for (g = be.offset - be.bookRotation; g >= 3.1415927F; ) {
            g -= 6.2831855F;
        }

        while (g < -3.1415927F) {
            g += 6.2831855F;
        }

        be.bookRotation += g * 0.4F;
        be.nextPageTurningSpeed = MathHelper.clamp(be.nextPageTurningSpeed, 0.0F, 1.0F);
        ++be.ticks;
        be.pageAngle = be.nextPageAngle;
        float h = (be.flipRandom - be.nextPageAngle) * 0.4F;
        h = MathHelper.clamp(h, -0.2F, 0.2F);
        be.flipTurn += (h - be.flipTurn) * 0.9F;
        be.nextPageAngle += be.flipTurn;
    }
}
