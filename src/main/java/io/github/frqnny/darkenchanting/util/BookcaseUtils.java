package io.github.frqnny.darkenchanting.util;

import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BookcaseUtils {
    public static int getBookshelfCount(World world, BlockPos blockPos) {
        int bookshelves = 0;

        int j;
        for (j = -1; j <= 1; ++j) {
            for (int k = -1; k <= 1; ++k) {
                if ((j != 0 || k != 0) && world.isAir(blockPos.add(k, 0, j)) && world.isAir(blockPos.add(k, 1, j))) {
                    if (world.getBlockState(blockPos.add(k * 2, 0, j * 2)).isOf(Blocks.BOOKSHELF)) {
                        ++bookshelves;
                    }

                    if (world.getBlockState(blockPos.add(k * 2, 1, j * 2)).isOf(Blocks.BOOKSHELF)) {
                        ++bookshelves;
                    }

                    if (k != 0 && j != 0) {
                        if (world.getBlockState(blockPos.add(k * 2, 0, j)).isOf(Blocks.BOOKSHELF)) {
                            ++bookshelves;
                        }

                        if (world.getBlockState(blockPos.add(k * 2, 1, j)).isOf(Blocks.BOOKSHELF)) {
                            ++bookshelves;
                        }

                        if (world.getBlockState(blockPos.add(k, 0, j * 2)).isOf(Blocks.BOOKSHELF)) {
                            ++bookshelves;
                        }

                        if (world.getBlockState(blockPos.add(k, 1, j * 2)).isOf(Blocks.BOOKSHELF)) {
                            ++bookshelves;
                        }
                    }
                }
            }
        }

        return Math.min(15, bookshelves);
    }

    public static int applyDiscount(int originalCost, World world, BlockPos pos) {
        return applyDiscount(originalCost, getDiscount(world, pos));
    }

    public static int applyDiscount(int originalCost, double discount) {
        return (int) (originalCost * (1D - discount));
    }

    public static double getDiscount(World world, BlockPos pos) {
        return (getBookshelfCount(world, pos) / 15D * 0.4);
    }
}
