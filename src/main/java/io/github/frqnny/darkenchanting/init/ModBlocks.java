package io.github.frqnny.darkenchanting.init;

import io.github.frqnny.darkenchanting.block.DarkConduitBlock;
import io.github.frqnny.darkenchanting.block.DarkEnchanterBlock;
import io.github.frqnny.darkenchanting.block.WallDarkConduitBlock;
import io.github.frqnny.darkenchanting.blockentity.DarkEnchanterBlockEntity;
import io.github.frqnny.darkenchanting.client.renderer.DarkEnchanterBlockEntityRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.registry.Registry;

public class ModBlocks {

    public static final DarkEnchanterBlock DARK_ENCHANTER = new DarkEnchanterBlock(FabricBlockSettings.copyOf(Blocks.ENCHANTING_TABLE).requiresTool());
    public static final DarkConduitBlock DARK_TORCH = new DarkConduitBlock(FabricBlockSettings.copyOf(Blocks.TORCH), ParticleTypes.DRAGON_BREATH);
    public static final WallDarkConduitBlock DARK_TORCH_WALL = new WallDarkConduitBlock(FabricBlockSettings.copyOf(Blocks.WALL_TORCH), ParticleTypes.DRAGON_BREATH);

    public static void init() {
        Registry.register(Registry.BLOCK, DarkConduitBlock.ID, DARK_TORCH);
        Registry.register(Registry.BLOCK, WallDarkConduitBlock.ID, DARK_TORCH_WALL);
        Registry.register(Registry.BLOCK, DarkEnchanterBlock.ID, DARK_ENCHANTER);
        Registry.register(Registry.BLOCK_ENTITY_TYPE, DarkEnchanterBlock.ID, DE_BLOCK_ENTITY);
    }

    public static void clientInit() {
        BlockEntityRendererRegistry.register(ModBlocks.DE_BLOCK_ENTITY, DarkEnchanterBlockEntityRenderer::new);
        ClientSpriteRegistryCallback.event(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE).register((atlas, registry) -> registry.register(DarkEnchanterBlockEntityRenderer.BOOK_ID));
    }

    public static final BlockEntityType<DarkEnchanterBlockEntity> DE_BLOCK_ENTITY = FabricBlockEntityTypeBuilder.create(DarkEnchanterBlockEntity::new, DARK_ENCHANTER).build();
}




