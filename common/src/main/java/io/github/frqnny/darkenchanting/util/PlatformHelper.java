package io.github.frqnny.darkenchanting.util;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;

public class PlatformHelper {

    @ExpectPlatform
    public static <T extends BlockEntity> BlockEntityType<T> createBlockEntity(BlockEntityType.BlockEntityFactory<T> factory, Block... blocks) {
        throw new AssertionError();
    }
}
