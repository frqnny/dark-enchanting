package io.github.frqnny.darkenchanting.init;

import io.github.frqnny.darkenchanting.block.DarkConduitBlock;
import io.github.frqnny.darkenchanting.block.DarkEnchanterBlock;
import io.github.frqnny.darkenchanting.block.WallDarkConduitBlock;
import io.github.frqnny.darkenchanting.blockentity.DarkEnchanterBlockEntity;
import io.github.frqnny.darkenchanting.client.renderer.DarkEnchanterBlockEntityRenderer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

import static io.github.frqnny.darkenchanting.DarkEnchanting.DarkEnchantingGroup;

public class ModBlocks {

    public static final DarkEnchanterBlock DARK_ENCHANTER = new DarkEnchanterBlock(FabricBlockSettings.copyOf(Blocks.ENCHANTING_TABLE).requiresTool());
    public static final DarkConduitBlock DARK_TORCH = new DarkConduitBlock(FabricBlockSettings.copyOf(Blocks.TORCH), ParticleTypes.DRAGON_BREATH);
    public static final WallDarkConduitBlock DARK_TORCH_WALL = new WallDarkConduitBlock(FabricBlockSettings.copyOf(Blocks.WALL_TORCH), ParticleTypes.DRAGON_BREATH);
    public static final BlockEntityType<DarkEnchanterBlockEntity> DE_BLOCK_ENTITY = FabricBlockEntityTypeBuilder.create(DarkEnchanterBlockEntity::new, DARK_ENCHANTER).build();

    public static void init() {
        Registry.register(Registries.BLOCK, DarkConduitBlock.ID, DARK_TORCH);
        Registry.register(Registries.BLOCK, WallDarkConduitBlock.ID, DARK_TORCH_WALL);
        Registry.register(Registries.BLOCK, DarkEnchanterBlock.ID, DARK_ENCHANTER);
        Registry.register(Registries.BLOCK_ENTITY_TYPE, DarkEnchanterBlock.ID, DE_BLOCK_ENTITY);

    }
    public static void clientInit() {
        BlockEntityRendererFactories.register(DE_BLOCK_ENTITY, DarkEnchanterBlockEntityRenderer::new);
    }
}
