package io.github.frqnny.darkenchanting.init;

import io.github.frqnny.darkenchanting.block.DarkConduitBlock;
import io.github.frqnny.darkenchanting.block.DarkEnchanterBlock;
import io.github.frqnny.darkenchanting.block.WallDarkConduitBlock;
import io.github.frqnny.darkenchanting.blockentity.DarkEnchanterBlockEntity;
import io.github.frqnny.darkenchanting.client.renderer.DarkEnchanterBlockEntityRenderer;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModBlocks {

    public static final DarkEnchanterBlock DARK_ENCHANTER = new DarkEnchanterBlock(AbstractBlock.Settings.copy(Blocks.ENCHANTING_TABLE).requiresTool());
    public static final DarkConduitBlock DARK_TORCH = new DarkConduitBlock(ParticleTypes.DRAGON_BREATH, AbstractBlock.Settings.copy(Blocks.TORCH));
    public static final WallDarkConduitBlock DARK_TORCH_WALL = new WallDarkConduitBlock(ParticleTypes.DRAGON_BREATH, AbstractBlock.Settings.copy(Blocks.WALL_TORCH));

    public static void init() {
        Registry.register(Registries.BLOCK, DarkConduitBlock.ID, DARK_TORCH);
        Registry.register(Registries.BLOCK, WallDarkConduitBlock.ID, DARK_TORCH_WALL);
        Registry.register(Registries.BLOCK, DarkEnchanterBlock.ID, DARK_ENCHANTER);
        Registry.register(Registries.BLOCK_ENTITY_TYPE, DarkEnchanterBlock.ID, DE_BLOCK_ENTITY);
    }

    public static void clientInit() {
        BlockEntityRendererFactories.register(DE_BLOCK_ENTITY, DarkEnchanterBlockEntityRenderer::new);
    }

    public static final BlockEntityType<DarkEnchanterBlockEntity> DE_BLOCK_ENTITY = BlockEntityType.Builder.create(DarkEnchanterBlockEntity::new, DARK_ENCHANTER).build(null);


}
