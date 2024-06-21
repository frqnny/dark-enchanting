package io.github.frqnny.darkenchanting.blockentity;

import io.github.frqnny.darkenchanting.init.ModBlocks;
import io.github.frqnny.darkenchanting.network.ScreenPacket;
import io.github.frqnny.darkenchanting.screen.DarkEnchanterScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class DarkEnchanterBlockEntity extends BlockEntityWithBook implements ExtendedScreenHandlerFactory<ScreenPacket> {
    public DarkEnchanterBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlocks.DE_BLOCK_ENTITY, pos, state);
    }


    @Override
    public Text getDisplayName() {
        return Text.translatable("block.darkenchanting.dark_enchanter");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new DarkEnchanterScreenHandler(syncId, inv, ScreenHandlerContext.create(this.world, this.pos));
    }

    @Override
    public ScreenPacket getScreenOpeningData(ServerPlayerEntity player) {
        return new ScreenPacket(this.pos);
    }
}
