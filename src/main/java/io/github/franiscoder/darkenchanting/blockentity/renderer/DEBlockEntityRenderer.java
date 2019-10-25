package io.github.franiscoder.darkenchanting.blockentity.renderer;

import io.github.franiscoder.darkenchanting.blockentity.DEBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.LayeredVertexConsumerStorage;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.entity.model.BookModel;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.MatrixStack;

@Environment(EnvType.CLIENT)
public class DEBlockEntityRenderer extends BlockEntityRenderer<DEBlockEntity> {

    private static final Identifier BOOK_TEX = new Identifier("dark-enchanting:entity/book1");
    private final BookModel book = new BookModel();

    public DEBlockEntityRenderer() {
        super(BlockEntityRenderDispatcher.INSTANCE);
    }

    @Override
    public void render(DEBlockEntity blockEntity, double x, double y, double z, float partialTicks, MatrixStack matrixStack, LayeredVertexConsumerStorage layeredVertexConsumerStorage, int int_1, int int_2) {
        matrixStack.push();
        matrixStack.translate(0.5D, 0.75D, 0.5D);
        float float_2 = (float)blockEntity.ticks + partialTicks;
        matrixStack.translate(0.0D, (0.1F + MathHelper.sin(float_2 * 0.1F) * 0.01F), 0.0D);

        float float_3 = blockEntity.field_11964 - blockEntity.field_11963;
        while(float_3 >= 3.1415927F) float_3 -= 6.2831855F;

        while(float_3 < -3.1415927F) float_3 += 6.2831855F;

        float float_4 = blockEntity.field_11963 + float_3 * partialTicks;
        matrixStack.multiply(Vector3f.POSITIVE_Y.method_23626(-float_4));
        matrixStack.multiply(Vector3f.POSITIVE_Z.getRotationQuaternion(80.0F));
        float float_5 = MathHelper.lerp(partialTicks, blockEntity.pageAngle, blockEntity.nextPageAngle);
        float float_6 = MathHelper.method_22450(float_5 + 0.25F) * 1.6F - 0.3F;
        float float_7 = MathHelper.method_22450(float_5 + 0.75F) * 1.6F - 0.3F;
        float float_8 = MathHelper.lerp(partialTicks, blockEntity.pageTurningSpeed, blockEntity.nextPageTurningSpeed);
        this.book.setPageAngles(float_2, MathHelper.clamp(float_6, 0.0F, 1.0F), MathHelper.clamp(float_7, 0.0F, 1.0F), float_8);
        VertexConsumer vertexConsumer_1 = layeredVertexConsumerStorage.getBuffer(RenderLayer.getEntitySolid(SpriteAtlasTexture.BLOCK_ATLAS_TEX));
        this.book.render(matrixStack, vertexConsumer_1, int_1, int_2, 1.0F, 1.0F, 1.0F, this.getSprite(BOOK_TEX));
        matrixStack.pop();
    }


}
