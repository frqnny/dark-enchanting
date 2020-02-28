package io.github.franiscoder.darkenchanting.init;

import io.github.franiscoder.darkenchanting.block.DarkEnchanter;
import io.github.franiscoder.darkenchanting.blockentity.DEBlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.registry.Registry;

import static io.github.franiscoder.darkenchanting.init.ModBlocks.DARK_ENCHANTER;

public class BlockEntities {
    public static BlockEntityType<DEBlockEntity> DE_BLOCK_ENTITY;

    public static void registerBlockEntities() {
        DE_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, DarkEnchanter.ID, BlockEntityType.Builder.create(DEBlockEntity::new, DARK_ENCHANTER).build(null));
    }
}
