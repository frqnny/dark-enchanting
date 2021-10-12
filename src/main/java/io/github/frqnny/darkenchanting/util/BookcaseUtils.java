package io.github.frqnny.darkenchanting.util;

import io.github.frqnny.darkenchanting.DarkEnchanting;
import io.github.frqnny.darkenchanting.config.DarkEnchantingConfig;
import net.minecraft.block.Block;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class BookcaseUtils {
    public static int getBookshelfCount(World world, BlockPos blockPos) 
    {
        int bookshelves = 0;

        Tag<Block> bookshelvesTag = world.getTagManager().getTag(Registry.BLOCK_KEY, new Identifier("c", "bookshelves"), id -> new RuntimeException("Could not load tag: " + id.toString()));

        int j;
        for (j = -1; j <= 1; ++j) {
            for (int k = -1; k <= 1; ++k) {
                if ((j != 0 || k != 0) && world.isAir(blockPos.add(k, 0, j)) && world.isAir(blockPos.add(k, 1, j))) {
                    if (world.getBlockState(blockPos.add(k * 2, 0, j * 2)).isIn(bookshelvesTag)) {
                        ++bookshelves;
                    }

                    if (world.getBlockState(blockPos.add(k * 2, 1, j * 2)).isIn(bookshelvesTag)) {
                        ++bookshelves;
                    }

                    if (k != 0 && j != 0) {
                        if (world.getBlockState(blockPos.add(k * 2, 0, j)).isIn(bookshelvesTag)) {
                            ++bookshelves;
                        }

                        if (world.getBlockState(blockPos.add(k * 2, 1, j)).isIn(bookshelvesTag)) {
                            ++bookshelves;
                        }

                        if (world.getBlockState(blockPos.add(k, 0, j * 2)).isIn(bookshelvesTag)) {
                            ++bookshelves;
                        }

                        if (world.getBlockState(blockPos.add(k, 1, j * 2)).isIn(bookshelvesTag)) {
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
        DarkEnchantingConfig config = DarkEnchanting.CONFIG;
        return (getBookshelfCount(world, pos) / 15D * config.bookshelvesDiscount);
    }
}
