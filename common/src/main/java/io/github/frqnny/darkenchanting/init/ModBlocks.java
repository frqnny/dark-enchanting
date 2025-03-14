package io.github.frqnny.darkenchanting.init;

import dev.architectury.platform.Platform;
import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrySupplier;
import io.github.frqnny.darkenchanting.DarkEnchanting;
import io.github.frqnny.darkenchanting.block.DarkConduitBlock;
import io.github.frqnny.darkenchanting.block.DarkEnchanterBlock;
import io.github.frqnny.darkenchanting.block.WallDarkConduitBlock;
import io.github.frqnny.darkenchanting.blockentity.DarkEnchanterBlockEntity;
import io.github.frqnny.darkenchanting.client.renderer.DarkEnchanterBlockEntityRenderer;
import io.github.frqnny.darkenchanting.util.PlatformHelper;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

public class ModBlocks {
    public static RegistrySupplier<DarkEnchanterBlock> DARK_ENCHANTER;
    public static RegistrySupplier<DarkConduitBlock> DARK_TORCH;
    public static RegistrySupplier<WallDarkConduitBlock> DARK_TORCH_WALL;
    public static RegistrySupplier<BlockEntityType<DarkEnchanterBlockEntity>> DE_BLOCK_ENTITY;


    public static void init() {
        Registrar<Block> blocks = DarkEnchanting.MANAGER.get().get(RegistryKeys.BLOCK);
        DARK_TORCH = blocks.register(DarkConduitBlock.ID, () ->
                new DarkConduitBlock(ParticleTypes.DRAGON_BREATH, AbstractBlock.Settings.copy(Blocks.TORCH).registryKey(RegistryKey.of(RegistryKeys.BLOCK, DarkConduitBlock.ID))));
        DARK_TORCH_WALL = blocks.register(WallDarkConduitBlock.ID, () ->
                new WallDarkConduitBlock(ParticleTypes.DRAGON_BREATH, AbstractBlock.Settings.copy(Blocks.WALL_TORCH).registryKey(RegistryKey.of(RegistryKeys.BLOCK, WallDarkConduitBlock.ID))));
        DARK_ENCHANTER = blocks.register(DarkEnchanterBlock.ID, () ->
                new DarkEnchanterBlock(AbstractBlock.Settings.copy(Blocks.ENCHANTING_TABLE).requiresTool().registryKey(RegistryKey.of(RegistryKeys.BLOCK, DarkEnchanterBlock.ID))));
        Registrar<BlockEntityType<?>> blockEntities = DarkEnchanting.MANAGER.get().get(RegistryKeys.BLOCK_ENTITY_TYPE);
        DE_BLOCK_ENTITY = blockEntities.register(DarkEnchanterBlock.ID, () -> PlatformHelper.createBlockEntity(DarkEnchanterBlockEntity::new, DARK_ENCHANTER.get()));
    }

    public static void clientInit() {
        if (Platform.isFabric()) {
            BlockEntityRendererRegistry.register(DE_BLOCK_ENTITY.get(), DarkEnchanterBlockEntityRenderer::new);
        }
    }
}
