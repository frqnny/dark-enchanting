package io.github.frqnny.darkenchanting.init;

import io.github.frqnny.darkenchanting.block.DarkEnchanterBlock;
import io.github.frqnny.darkenchanting.blockentity.DarkEnchanterBlockEntity;
import io.github.frqnny.darkenchanting.client.renderer.DarkEnchanterBlockEntityRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.util.registry.Registry;

public class ModBlocks {
    public static final DarkEnchanterBlock DARK_ENCHANTER = new DarkEnchanterBlock(FabricBlockSettings.copyOf(Blocks.ENCHANTING_TABLE).breakByTool(FabricToolTags.PICKAXES));

    public static void init() {
        Registry.register(Registry.BLOCK, DarkEnchanterBlock.ID, DARK_ENCHANTER);
        Registry.register(Registry.BLOCK_ENTITY_TYPE, DarkEnchanterBlock.ID, DE_BLOCK_ENTITY);
    }    public static final BlockEntityType<DarkEnchanterBlockEntity> DE_BLOCK_ENTITY = FabricBlockEntityTypeBuilder.create(DarkEnchanterBlockEntity::new, DARK_ENCHANTER).build();

    public static void clientInit() {
        BlockEntityRendererRegistry.register(ModBlocks.DE_BLOCK_ENTITY, DarkEnchanterBlockEntityRenderer::new);
        ClientSpriteRegistryCallback.event(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE).register((atlas, registry) -> registry.register(DarkEnchanterBlockEntityRenderer.BOOK_ID));
    }


}
