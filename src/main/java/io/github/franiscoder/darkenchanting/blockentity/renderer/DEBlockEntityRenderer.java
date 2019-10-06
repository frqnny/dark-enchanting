package io.github.franiscoder.darkenchanting.blockentity.renderer;

import io.github.franiscoder.darkenchanting.blockentity.DEBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockRenderLayer;
import net.minecraft.class_4576;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.entity.model.BookModel;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Quaternion;

@Environment(EnvType.CLIENT)
public class DEBlockEntityRenderer extends class_4576<DEBlockEntity> {

    private static final Identifier BOOK_TEX = new Identifier("dark-enchanting:entity/book1");
    private final BookModel book = new BookModel();

    //public DEBlockEntityRenderer() {
    //    super(BlockEntityRenderDispatcher.INSTANCE);
    //}

    @Override
    protected void method_22738(DEBlockEntity enchantingTableBlockEntity_1, double double_1, double double_2, double double_3, float float_1, int int_1, BlockRenderLayer blockRenderLayer_1, BufferBuilder bufferBuilder_1, int int_2, int int_3) {
        bufferBuilder_1.method_22629();
        bufferBuilder_1.method_22626(0.5D, 0.75D, 0.5D);
        float float_2 = (float) enchantingTableBlockEntity_1.ticks + float_1;
        bufferBuilder_1.method_22626(0.0D, 0.1F + MathHelper.sin(float_2 * 0.1F) * 0.01F, 0.0D);

        float float_3;
        for (float_3 = enchantingTableBlockEntity_1.field_11964 - enchantingTableBlockEntity_1.field_11963; float_3 >= 3.1415927F; float_3 -= 6.2831855F) {
        }

        while (float_3 < -3.1415927F) {
            float_3 += 6.2831855F;
        }

        float float_4 = enchantingTableBlockEntity_1.field_11963 + float_3 * float_1;
        bufferBuilder_1.method_22622(new Quaternion(Vector3f.field_20705, -float_4, false));
        bufferBuilder_1.method_22622(new Quaternion(Vector3f.field_20707, 80.0F, true));
        float float_5 = MathHelper.lerp(float_1, enchantingTableBlockEntity_1.pageAngle, enchantingTableBlockEntity_1.nextPageAngle);
        float float_6 = MathHelper.method_22450(float_5 + 0.25F) * 1.6F - 0.3F;
        float float_7 = MathHelper.method_22450(float_5 + 0.75F) * 1.6F - 0.3F;
        float float_8 = MathHelper.lerp(float_1, enchantingTableBlockEntity_1.pageTurningSpeed, enchantingTableBlockEntity_1.nextPageTurningSpeed);
        this.book.setPageAngles(float_2, MathHelper.clamp(float_6, 0.0F, 1.0F), MathHelper.clamp(float_7, 0.0F, 1.0F), float_8);
        this.book.render(bufferBuilder_1, 0.0625F, int_2, int_3, this.method_22739(BOOK_TEX));
        bufferBuilder_1.method_22630();
    }


    //From Mojang Code, redone to slightly replace some but not all random names.
/*
    @Override
    public void render(DEBlockEntity blockEntity, double x, double y, double z, float partialTicks, class_4587 class_4587_1, class_4597 class_4597_1, int int_1) {
        class_4587_1.method_22903();
        class_4587_1.method_22904(0.5D, 0.75D, 0.5D);
        float float_2 = (float) blockEntity.ticks + partialTicks;
        class_4587_1.method_22904(0.0D, (0.1F + MathHelper.sin(float_2 * 0.1F) * 0.01F), 0.0D);

        float float_3 = blockEntity.field_11964 - blockEntity.field_11963;
        while (float_3 >= 3.1415927F) {
            float_3 -= 6.2831855F;
        }

        while (float_3 < -3.1415927F) {
            float_3 += 6.2831855F;
        }

        float float_4 = blockEntity.field_11963 + float_3 * partialTicks;
        class_4587_1.method_22907(Vector3f.field_20705.method_23214(-float_4, false));
        class_4587_1.method_22907(Vector3f.field_20707.method_23214(80.0F, true));
        float float_5 = MathHelper.lerp(partialTicks, blockEntity.pageAngle, blockEntity.nextPageAngle);
        float float_6 = MathHelper.method_22450(float_5 + 0.25F) * 1.6F - 0.3F;
        float float_7 = MathHelper.method_22450(float_5 + 0.75F) * 1.6F - 0.3F;
        float float_8 = MathHelper.lerp(partialTicks, blockEntity.pageTurningSpeed, blockEntity.nextPageTurningSpeed);
        this.book.setPageAngles(float_2, MathHelper.clamp(float_6, 0.0F, 1.0F), MathHelper.clamp(float_7, 0.0F, 1.0F), float_8);
        class_4588 class_4588_1 = class_4597_1.getBuffer(BlockRenderLayer.SOLID);
        this.book.render(class_4587_1, class_4588_1, 0.0625F, int_1, this.method_23082(BOOK_TEX));
        class_4587_1.method_22909();
    }

 */


}
