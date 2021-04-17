package io.github.frqnny.darkenchanting.client.renderer;

import io.github.frqnny.darkenchanting.DarkEnchanting;
import io.github.frqnny.darkenchanting.blockentity.DarkEnchanterBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.entity.model.BookModel;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

@Environment(EnvType.CLIENT)
public class DarkEnchanterBlockEntityRenderer extends BlockEntityRenderer<DarkEnchanterBlockEntity> {
    public static final Identifier BOOK_ID = DarkEnchanting.id("entity/book1");
    private static final SpriteIdentifier BOOK_TEX = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, BOOK_ID);
    private final BookModel book = new BookModel();

    public DarkEnchanterBlockEntityRenderer(BlockEntityRenderDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(DarkEnchanterBlockEntity blockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        matrices.push();
        matrices.translate(0.5D, 0.75D, 0.5D);
        float g = (float) blockEntity.ticks + tickDelta;
        matrices.translate(0.0D, (0.1F + MathHelper.sin(g * 0.1F) * 0.01F), 0.0D);

        float h = blockEntity.bookRotation - blockEntity.bookRotationPrev;
        while (h >= 3.1415927F) {
            h -= 6.2831855F;
        }

        while (h < -3.1415927F) {
            h += 6.2831855F;
        }

        float k = blockEntity.bookRotationPrev + h * tickDelta;
        matrices.multiply(Vector3f.POSITIVE_Y.getRadialQuaternion(-k));
        matrices.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(80.0F));
        float l = MathHelper.lerp(tickDelta, blockEntity.pageAngle, blockEntity.nextPageAngle);
        float m = MathHelper.fractionalPart(l + 0.25F) * 1.6F - 0.3F;
        float n = MathHelper.fractionalPart(l + 0.75F) * 1.6F - 0.3F;
        float o = MathHelper.lerp(tickDelta, blockEntity.pageTurningSpeed, blockEntity.nextPageTurningSpeed);
        this.book.setPageAngles(g, MathHelper.clamp(m, 0.0F, 1.0F), MathHelper.clamp(n, 0.0F, 1.0F), o);
        VertexConsumer vertexConsumer = BOOK_TEX.getVertexConsumer(vertexConsumers, RenderLayer::getEntitySolid);
        this.book.method_24184(matrices, vertexConsumer, light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
        matrices.pop();
    }

}
