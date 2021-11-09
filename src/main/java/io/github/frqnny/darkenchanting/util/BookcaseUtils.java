package io.github.frqnny.darkenchanting.util;


import io.github.frqnny.darkenchanting.DarkEnchanting;
import io.github.frqnny.darkenchanting.config.DarkEnchantingConfig;
import io.github.frqnny.darkenchanting.init.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class BookcaseUtils 
{

    //Inner ring
    public static boolean getObsidianCount(World world, BlockPos blockPos) 
    {
        int base_obsidian = 0;
        int z;
        for (z = -1; z <= 1; ++z) { // LOOP for Y level
            for (int x = -1; x <= 1; ++x) { // LOOP for Z level
                if ((z != 0 || x != 0) && world.isAir(blockPos.add(x, 0, z)) && world.isAir(blockPos.add(x, 1, z))) //check if block not center
                {
                    /* 
                    check block on Coords:
                    relative to dark enchanter btw,
                    */
                    if (world.getBlockState(blockPos.add(x * 2, -1, z * 2)).isOf(Blocks.CRYING_OBSIDIAN)) 
                    {
                        ++base_obsidian;
                    }

                    if (x != 0 && z != 0)
                     {
                        if (world.getBlockState(blockPos.add(x * 2, -1, z)).isOf(Blocks.CRYING_OBSIDIAN))
                        {
                            ++base_obsidian;
                        }

                        if (world.getBlockState(blockPos.add(x, -1, z * 2)).isOf(Blocks.CRYING_OBSIDIAN)) 
                        {
                            ++base_obsidian;
                        }
                    }
                }
            }
        }
        if (base_obsidian == 16)
        {
            return true;
        }
        else
        {
            base_obsidian = 0; 
            return false;
        }
    }
    
    public static int getBookshelfCount(World world, BlockPos blockPos) 
    {
        int bookshelves = 0;
        Tag<Block> bookshelvesTag = world.getTagManager().getTag(Registry.BLOCK_KEY, new Identifier("c", "bookshelves"), id -> new RuntimeException("Could not load tag: " + id.toString()));

        int z;
        int x;
        int y;
        if (getObsidianCount(world,blockPos))
        {
            for (y = 0; y <= 5; ++y)
            {
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
                            if (world.getBlockState(blockPos.add(x * 2, y, z * 2)).isIn(bookshelvesTag)) 
                            {
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
                            if (x != 0 && z != 0)
                            {
                                if (world.getBlockState(blockPos.add(x * 2, y, z)).isIn(bookshelvesTag)) 
                                {
                                    ++bookshelves;
                                }
                                if (world.getBlockState(blockPos.add(x, y, z * 2)).isIn(bookshelvesTag)) 
                                {
                                    ++bookshelves;
                                }
                            }
                        }
                    }
                }
            }
            return Math.min(15, bookshelves);

        }
        else
        {
            return 0;
        }
    }

    //outer ring
    public static boolean getObsidianCount_2(World world, BlockPos blockPos) 
    {
        int base_obsidian_2 = 0;
        int z;
        int x;
        int y = -1;
        for (z = -4; z <= 4; ++z) 
        { // LOOP for Z level
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
                if (world.getBlockState(blockPos.add(4, y, z)).isOf(Blocks.ANCIENT_DEBRIS)) 
                {
                    //System.out.println("obsidian found at :" + 4 +","+y+","+z);
                    ++base_obsidian_2;
                }
                if (z != 4)
                {
                    if (world.getBlockState(blockPos.add(-4, y, z)).isOf(Blocks.ANCIENT_DEBRIS)) 
                    {
                        //System.out.println("obsidian found at :" + -4 +","+y+","+ z);
                        ++base_obsidian_2;
                    }
                }
            } 
        }
        for (x = -4; x <= 4; ++x)
        { // LOOP for X level
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
                if (world.getBlockState(blockPos.add(x, y, 4)).isOf(Blocks.ANCIENT_DEBRIS)) 
                {
                    //System.out.println("obsidian found at :" + x +","+y+","+4);
                    ++base_obsidian_2;
                }
                if (x != 4)
                {
                    if (world.getBlockState(blockPos.add(x, y, -4)).isOf(Blocks.ANCIENT_DEBRIS)) 
                    {
                        //System.out.println("obsidian found at :" + x +","+y+","+-4);
                        ++base_obsidian_2;
                    }
                }
            } 
        }
        //System.out.println("Dark Enchanter Obsidian Ring Count :" + base_obsidian_2);
        if (Math.min(30, base_obsidian_2) == 30)
        {
            
            return true;
        }
        else
        {
            base_obsidian_2 = 0;
            return false;

        }
        
    }
    
    public static int getBookshelfCount_2(World world, BlockPos blockPos) 
    {
        int bookshelves_2 = 0;
        Tag<Block> bookshelvesTag = world.getTagManager().getTag(Registry.BLOCK_KEY, new Identifier("c", "bookshelves"), id -> new RuntimeException("Could not load tag: " + id.toString()));

        int z;
        int x;
        int y;
        if (getObsidianCount_2(world,blockPos))
        {
            for (y = 0; y <= 5; ++y) 
            { // Loop Y level
                for (z = -4; z <= 4; ++z) 
                { // LOOP for Z level
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
                        if (world.getBlockState(blockPos.add(4, y, z)).isIn(bookshelvesTag)) 
                        {
                            //System.out.println("obsidian found at :" + 4 +","+y+","+z);
                            ++bookshelves_2;
                        }
                        if (z != 4)
                        {
                            if (world.getBlockState(blockPos.add(-4, y, z)).isIn(bookshelvesTag)) 
                            {
                                //System.out.println("obsidian found at :" + -4 +","+y+","+ z);
                                ++bookshelves_2;
                            }
                        }
                    } 
                }
                for (x = -4; x <= 4; ++x)
                { // LOOP for X level
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
                        if (world.getBlockState(blockPos.add(x, y, 4)).isIn(bookshelvesTag)) 
                        {
                            //System.out.println("obsidian found at :" + x +","+y+","+4);
                            ++bookshelves_2;
                        }
                        if (x != 4)
                        {
                            if (world.getBlockState(blockPos.add(x, y, -4)).isIn(bookshelvesTag)) 
                            {
                               // System.out.println("obsidian found at :" + x +","+y+","+-4);
                                ++bookshelves_2;
                            }
                        }
                    } 
                }
            }
            return Math.min(29, bookshelves_2);
        }
        else
        {
            return 0;
        }
    }

    
    public static int applyDiscount(int originalCost, World world, BlockPos pos) 
    {
        return applyDiscount(originalCost, getDiscount(world, pos));
    }

    public static int applyDiscount(int originalCost, double discount) {
        return (int) Math.ceil((originalCost * (1D - discount)));
    }

    public static double getDiscount(World world, BlockPos pos) 
    {
        DarkEnchantingConfig config = DarkEnchanting.CONFIG;
        double ConfiguredMaxDiscount = config.bookshelvesDiscount;
        double HalfDiscount = ConfiguredMaxDiscount / 2D ;

        double bookcase_1 = getBookshelfCount(world, pos) / 15D * HalfDiscount;
        double bookcase_2 = getBookshelfCount_2(world, pos) / 29D * HalfDiscount;
        return (bookcase_1 + bookcase_2 + getconduitdiscount(world,pos)); 
    }

    public static double getconduitdiscount(World world, BlockPos pos)  {
        if (getBookshelfCount(world, pos) == 15D && getBookshelfCount_2(world, pos) == 29D && getconduits(world,pos))
        {
            return 0.1;
        }
        else
        {
            return 0;
        }
    }

    //Conduit Counter
    public static boolean getconduits(World world, BlockPos blockPos) 
    {
        int conduit_count = 0;
        int y = 6;
        
        if (world.getBlockState(blockPos.add(4, y, 4)).isOf(ModBlocks.DARK_TORCH)) 
        {
            //System.out.println("obsidian found at :" + 4 +","+y+","+z);
            ++conduit_count;
        }
        if (world.getBlockState(blockPos.add(-4, y, -4)).isOf(ModBlocks.DARK_TORCH)) 
        {
            //System.out.println("obsidian found at :" + -4 +","+y+","+ z);
            ++conduit_count;
        }
        if (world.getBlockState(blockPos.add(-4, y, 4)).isOf(ModBlocks.DARK_TORCH)) 
        {
            //System.out.println("obsidian found at :" + 4 +","+y+","+z);
            ++conduit_count;
        }
        if (world.getBlockState(blockPos.add(4, y, -4)).isOf(ModBlocks.DARK_TORCH)) 
        {
            //System.out.println("obsidian found at :" + -4 +","+y+","+ z);
            ++conduit_count;
        }            
        
        //System.out.println("Dark Enchanter Obsidian Ring Count :" + base_obsidian_2);
        if (Math.min(4, conduit_count) == 4)
        {
            
            return true;
        }
        else
        {
            conduit_count = 0;
            return false;

        }
        
    

    
}
