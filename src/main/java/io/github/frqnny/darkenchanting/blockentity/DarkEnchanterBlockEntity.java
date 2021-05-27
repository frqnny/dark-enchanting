package io.github.frqnny.darkenchanting.blockentity;

import io.github.frqnny.darkenchanting.client.gui.DarkEnchanterGUI;
import io.github.frqnny.darkenchanting.init.ModBlocks;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class DarkEnchanterBlockEntity extends BlockEntityWithBook implements ExtendedScreenHandlerFactory {
    public DarkEnchanterBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlocks.DE_BLOCK_ENTITY, pos, state);
    }


    @Override
    public void writeScreenOpeningData(ServerPlayerEntity serverPlayerEntity, PacketByteBuf buf) {
        buf.writeBlockPos(pos);
    }

    @Override
    public Text getDisplayName() {
        return new TranslatableText(this.getCachedState().getBlock().getTranslationKey());
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new DarkEnchanterGUI(syncId, inv, ScreenHandlerContext.create(this.world, this.pos));
    }
}
