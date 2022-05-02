package io.github.frqnny.darkenchanting.block;

import io.github.frqnny.darkenchanting.DarkEnchanting;
import io.github.frqnny.darkenchanting.blockentity.BlockEntityWithBook;
import io.github.frqnny.darkenchanting.blockentity.DarkEnchanterBlockEntity;
import io.github.frqnny.darkenchanting.init.ModBlocks;
import io.github.frqnny.darkenchanting.util.PlayerUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class DarkEnchanterBlock extends BlockWithEntity {
    public static final Identifier ID = new Identifier(DarkEnchanting.MODID, "dark_enchanter");
    public static final EnumProperty<BookType> BOOK_TYPE = EnumProperty.of("book_type", BookType.class);
    private static final VoxelShape SHAPE = Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D);

    public DarkEnchanterBlock(Settings s) {
        super(s);
        this.setDefaultState(this.getStateManager().getDefaultState().with(BOOK_TYPE, BookType.DEFAULT));
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new DarkEnchanterBlockEntity(pos, state);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult blockHitResult) {
        PlayerUtils.syncExperience(player);
        if (world.isClient) {
            return ActionResult.SUCCESS;
        } else {
            player.openHandledScreen(state.createScreenHandlerFactory(world, pos));
            return ActionResult.CONSUME;
        }
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public BlockRenderType getRenderType(BlockState blockState_1) {
        return BlockRenderType.MODEL;
    }

    @Override
    public boolean hasSidedTransparency(BlockState blockState_1) {
        return true;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos blockPos, Random random) {
        super.randomDisplayTick(state, world, blockPos, random);

        //inner circle
        for (int x = -2; x <= 2; ++x) {
            for (int z = -2; z <= 2; ++z) {

                if (x > -2 && x < 2 && z == -1) {
                    z = 2;
                }

                if (random.nextInt(16) == 0) {
                    for (int y = 0; y <= 1; ++y) {
                        BlockPos blockPos_2 = blockPos.add(x, y, z);
                        if (world.getBlockState(blockPos_2).getBlock() == Blocks.BOOKSHELF) {
                            if (!world.isAir(blockPos.add(x / 2, 0, z / 2))) {
                                break;
                            }

                            world.addParticle(ParticleTypes.ENCHANT, (double) blockPos.getX() + 0.5D, (double) blockPos.getY() + 2.0D, (double) blockPos.getZ() + 0.5D, (double) ((float) x + random.nextFloat()) - 0.5D, (float) y - random.nextFloat() - 1.5F, (double) ((float) z + random.nextFloat()) - 0.5D);
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(BOOK_TYPE);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, ModBlocks.DE_BLOCK_ENTITY, (world1, pos, state1, be) -> BlockEntityWithBook.tick(world1, pos, be));
    }

    @Override
    public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
        return false;
    }

    //TODO make it render based on the ID on here
    public enum BookType implements StringIdentifiable {
        DEFAULT(DarkEnchanting.id("entity/book1"), "default"),
        HELL(DarkEnchanting.id("entity/book2"), "hell");

        final Identifier id;
        final String string;

        BookType(Identifier id, String string) {
            this.id = id;
            this.string = string;
        }

        @Override
        public String asString() {
            return string;
        }
    }
}

