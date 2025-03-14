package io.github.frqnny.darkenchanting.util.neoforge;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;

public class PlatformHelperImpl {

    public static <T extends BlockEntity> BlockEntityType<T> createBlockEntity(BlockEntityType.BlockEntityFactory<T> factory, Block... blocks) {
        return new BlockEntityType<>(factory, blocks);
    }
}
