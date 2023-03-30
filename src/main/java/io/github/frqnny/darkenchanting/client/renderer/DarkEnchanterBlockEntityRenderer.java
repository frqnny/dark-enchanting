package io.github.frqnny.darkenchanting.client.renderer;

import io.github.frqnny.darkenchanting.DarkEnchanting;
import io.github.frqnny.darkenchanting.blockentity.DarkEnchanterBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.entity.model.BookModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;


@Environment(EnvType.CLIENT)
public class DarkEnchanterBlockEntityRenderer implements BlockEntityRenderer<DarkEnchanterBlockEntity> {
    public static final Identifier BOOK_ID = DarkEnchanting.id("entity/book1");
    private static final SpriteIdentifier BOOK_TEX = new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, BOOK_ID);
    private final BookModel book;

    public DarkEnchanterBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        this.book = new BookModel(ctx.getLayerModelPart(EntityModelLayers.BOOK));
    }

    @Override
    public void render(DarkEnchanterBlockEntity blockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        matrices.push();
        matrices.translate(0.5D, 0.75D, 0.5D);
        float partialTicks = (float) blockEntity.ticks + tickDelta;
        matrices.translate(0.0D, (0.1F + MathHelper.sin(partialTicks * 0.1F) * 0.01F), 0.0D);

        float bookRotationChange = blockEntity.bookRotation - blockEntity.bookRotationPrev;
        while (bookRotationChange >= 3.1415927F) {
            bookRotationChange -= 6.2831855F;
        }

        while (bookRotationChange < -3.1415927F) {
            bookRotationChange += 6.2831855F;
        }

        float newBookRotation = blockEntity.bookRotationPrev + bookRotationChange * tickDelta;
        matrices.multiply(Vec3f.POSITIVE_Y.getRadialQuaternion(-newBookRotation));
        matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(80.0F));
        float newPageAngle = MathHelper.lerp(tickDelta, blockEntity.pageAngle, blockEntity.nextPageAngle);
        float m = MathHelper.fractionalPart(newPageAngle + 0.25F) * 1.6F - 0.3F;
        float n = MathHelper.fractionalPart(newPageAngle + 0.75F) * 1.6F - 0.3F;
        float newPageTurningSpeed = MathHelper.lerp(tickDelta, blockEntity.pageTurningSpeed, blockEntity.nextPageTurningSpeed);
        this.book.setPageAngles(partialTicks, MathHelper.clamp(m, 0.0F, 1.0F), MathHelper.clamp(n, 0.0F, 1.0F), newPageTurningSpeed);
        VertexConsumer vertexConsumer = BOOK_TEX.getVertexConsumer(vertexConsumers, RenderLayer::getEntitySolid);
        this.book.renderBook(matrices, vertexConsumer, light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
        matrices.pop();
    }

}
