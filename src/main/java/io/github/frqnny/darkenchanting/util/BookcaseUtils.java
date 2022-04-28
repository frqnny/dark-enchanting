package io.github.frqnny.darkenchanting.util;


import io.github.frqnny.darkenchanting.DarkEnchanting;
import io.github.frqnny.darkenchanting.init.ModBlocks;
import io.github.frqnny.darkenchanting.init.ModTags;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BookcaseUtils {

    //Inner ring
    public static boolean getObsidianCount(World world, BlockPos blockPos) {
        int baseObsidian = 0;
        int z;
        for (z = -1; z <= 1; ++z) { // LOOP for Y level
            for (int x = -1; x <= 1; ++x) { // LOOP for Z level
                if ((z != 0 || x != 0) && world.isAir(blockPos.add(x, 0, z)) && world.isAir(blockPos.add(x, 1, z))) //check if block not center
                {
                    /*
                    check block on Coords:
                    relative to dark enchanter btw,
                    */
                    if (world.getBlockState(blockPos.add(x * 2, -1, z * 2)).isIn(ModTags.CRYING_OBSIDIAN)) {
                        ++baseObsidian;
                    }

                    if (x != 0 && z != 0) {
                        if (world.getBlockState(blockPos.add(x * 2, -1, z)).isIn(ModTags.CRYING_OBSIDIAN)) {
                            ++baseObsidian;
                        }

                        if (world.getBlockState(blockPos.add(x, -1, z * 2)).isIn(ModTags.CRYING_OBSIDIAN)) {
                            ++baseObsidian;
                        }
                    }
                }
            }
        }
        return baseObsidian == 16;
    }

    public static int getBookshelfCount(World world, BlockPos blockPos) {
        int bookshelves = 0;

        int z;
        int x;
        int y;
        if (getObsidianCount(world, blockPos)) {
            for (y = 0; y <= 5; ++y) {
                for (z = -1; z <= 1; ++z) { // LOOP for z level
                    for (x = -1; x <= 1; ++x) { // LOOP for x level
                        if ((z != 0 || x != 0) && world.isAir(blockPos.add(x, 0, z)) && world.isAir(blockPos.add(x, 1, z))) //check if block not center
                        {
                            // Y level is hardcoded, only check for y lvl 0,1
                            /*
                            check block on:
                            relative to dark enchanter btw,
                            everything that is labeled 1
                            [1][0][1][0][1]
                            [0]         [0]
                            [1]         [1]
                            [0]         [0]
                            [1][0][1][0][1]
                            */
                            if (world.getBlockState(blockPos.add(x * 2, y, z * 2)).isIn(ModTags.BOOKSHELVES)) {
                                ++bookshelves;
                            }
                            /*
                            check block on:
                            relative to dark enchanter btw,
                            everything that is labeled 0
                            [1][0][1][0][1]
                            [0]         [0]
                            [1]         [1]
                            [0]         [0]
                            [1][0][1][0][1]
                            */
                            if (x != 0 && z != 0) {
                                if (world.getBlockState(blockPos.add(x * 2, y, z)).isIn(ModTags.BOOKSHELVES)) {
                                    ++bookshelves;
                                }
                                if (world.getBlockState(blockPos.add(x, y, z * 2)).isIn(ModTags.BOOKSHELVES)) {
                                    ++bookshelves;
                                }
                            }
                        }
                    }
                }
            }
            return Math.min(15, bookshelves);

        } else {
            return 0;
        }
    }

    //outer ring
    public static boolean getObsidianCount2(World world, BlockPos blockPos) {
        int baseObsidian2 = 0;
        int z;
        int x;
        int y = -1;
        for (z = -4; z <= 4; ++z) { // LOOP for Z level
            if (z != 0) //check if block not center
            {
                /*
                check Corners

                 4 0 -3,
                 4 0 -2,
                 4 0 - 1
                 4 0 0,
                 4 0 1,
                 4 0 2,
                 4 0 3
                -4 0 -3,
                -4 0 -2,
                -4 0 0,
                -4 0 1,
                -4 0 2,
                -4 0 3
                */
                if (world.getBlockState(blockPos.add(4, y, z)).isIn(ModTags.CRYING_OBSIDIAN)) {
                    ++baseObsidian2;
                }
                if (z != 4) {
                    if (world.getBlockState(blockPos.add(-4, y, z)).isIn(ModTags.CRYING_OBSIDIAN)) {
                        ++baseObsidian2;
                    }
                }
            }
        }
        for (x = -4; x <= 4; ++x) { // LOOP for X level
            if (x != 0) //check if block not center
            {
                /*
                check Corners

                -4 0 -4                                                   4 0 4
                3 0 -4                                                    3 0 4
                2 0 -4                                                    2 0 4
                1 0 -4                                                    1 0 4
                0 0 -4                                                    0 0 4
                -1 0 -4                                                  -1 0 4
                -2 0 -4                                                  -2 0 4
                -3 0 -4                                                  -3 0 4
                -4 0 -4,                                                 -4 0 4
                */
                if (world.getBlockState(blockPos.add(x, y, 4)).isIn(ModTags.CRYING_OBSIDIAN)) {
                    ++baseObsidian2;
                }
                if (x != 4) {
                    if (world.getBlockState(blockPos.add(x, y, -4)).isIn(ModTags.CRYING_OBSIDIAN)) {
                        ++baseObsidian2;
                    }
                }
            }
        }
        return Math.min(30, baseObsidian2) == 30;

    }

    public static int getBookshelfCount2(World world, BlockPos blockPos) {
        int bookshelves2 = 0;

        int z;
        int x;
        int y;
        if (getObsidianCount2(world, blockPos)) {
            for (y = 0; y <= 5; ++y) { // Loop Y level
                for (z = -4; z <= 4; ++z) { // LOOP for Z level
                    if (z != 0) { //check if block not center
                        /*
                        check Corners

                         4 0 -3,
                         4 0 -2,
                         4 0 - 1
                         4 0 0,
                         4 0 1,
                         4 0 2,
                         4 0 3
                        -4 0 -3,
                        -4 0 -2,
                        -4 0 0,
                        -4 0 1,
                        -4 0 2,
                        -4 0 3
                        */
                        if (world.getBlockState(blockPos.add(4, y, z)).isIn(ModTags.BOOKSHELVES)) {
                            ++bookshelves2;
                        }
                        if (z != 4) {
                            if (world.getBlockState(blockPos.add(-4, y, z)).isIn(ModTags.BOOKSHELVES)) {
                                ++bookshelves2;
                            }
                        }
                    }
                }
                for (x = -4; x <= 4; ++x) { // LOOP for X level
                    if (x != 0) {//check if block not center

                        /*
                        check Corners

                        -4 0 -4                                                   4 0 4
                        3 0 -4                                                    3 0 4
                        2 0 -4                                                    2 0 4
                        1 0 -4                                                    1 0 4
                        0 0 -4                                                    0 0 4
                        -1 0 -4                                                  -1 0 4
                        -2 0 -4                                                  -2 0 4
                        -3 0 -4                                                  -3 0 4
                        -4 0 -4,                                                 -4 0 4
                        */
                        if (world.getBlockState(blockPos.add(x, y, 4)).isIn(ModTags.BOOKSHELVES)) {
                            ++bookshelves2;
                        }
                        if (x != 4) {
                            if (world.getBlockState(blockPos.add(x, y, -4)).isIn(ModTags.BOOKSHELVES)) {
                                ++bookshelves2;
                            }
                        }
                    }
                }
            }
            return Math.min(29, bookshelves2);
        } else {
            return 0;
        }
    }


    public static int applyDiscount(int originalCost, World world, BlockPos pos) {
        return applyDiscount(originalCost, getDiscount(world, pos));
    }

    public static int applyDiscount(int originalCost, double discount) {
        return (int) Math.ceil((originalCost * (1D - discount)));
    }

    public static double getDiscount(World world, BlockPos pos) {
        double halfDiscount = DarkEnchanting.CONFIG.bookshelvesDiscount / 2D;

        double bookcase1 = getBookshelfCount(world, pos) / 15D * halfDiscount;
        double bookcase2 = getBookshelfCount2(world, pos) / 29D * halfDiscount;
        return bookcase1 + bookcase2 + getConduitDiscount(world, pos);
    }

    public static double getConduitDiscount(World world, BlockPos pos) {
        if (getBookshelfCount(world, pos) == 15D && getBookshelfCount2(world, pos) == 29D && getConduits(world, pos)) {
            return DarkEnchanting.CONFIG.conduitDiscount;
        } else {
            return 0;
        }
    }

    //Conduit Counter
    public static boolean getConduits(World world, BlockPos blockPos) {
        int conduitCount = 0;
        int y = 5;

        if (world.getBlockState(blockPos.add(4, y, 4)).isOf(ModBlocks.DARK_TORCH)) {
            if (checkObsidianTower(world, blockPos, 4, 4)) {
                ++conduitCount;
            }
        }
        if (world.getBlockState(blockPos.add(-4, y, -4)).isOf(ModBlocks.DARK_TORCH)) {
            if (checkObsidianTower(world, blockPos, -4, -4)) {
                ++conduitCount;
            }
        }
        if (world.getBlockState(blockPos.add(-4, y, 4)).isOf(ModBlocks.DARK_TORCH)) {
            if (checkObsidianTower(world, blockPos, -4, 4)) {
                ++conduitCount;
            }
        }
        if (world.getBlockState(blockPos.add(4, y, -4)).isOf(ModBlocks.DARK_TORCH)) {
            if (checkObsidianTower(world, blockPos, 4, -4)) {
                ++conduitCount;
            }
        }

        return conduitCount == 4;


    }

    public static boolean checkObsidianTower(World world, BlockPos pos, int x, int z) {
        for (int i = 0; i < 5; i++) {
            if (!world.getBlockState(pos.add(x, i, z)).isOf(Blocks.OBSIDIAN)) {
                return false;
            }
        }

        return true;
    }
}
