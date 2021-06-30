package io.github.frqnny.darkenchanting.item;

import io.github.frqnny.darkenchanting.init.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TableUpgradeItem extends Item {
    public TableUpgradeItem(Settings settings) {
        super(settings);
    }


    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();
        BlockState state = world.getBlockState(pos);
        if (state.isOf(Blocks.ENCHANTING_TABLE)) {
            world.setBlockState(pos, ModBlocks.DARK_ENCHANTER.getDefaultState());
            context.getStack().decrement(1);
            return ActionResult.SUCCESS;
        } else {
            context.getPlayer().sendMessage(new TranslatableText("message.dark-enchanting.table_upgrade"), true);
        }
        return ActionResult.PASS;
    }


    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        user.sendMessage(new TranslatableText("message.dark-enchanting.table_upgrade"), true);
        return super.use(world, user, hand);
    }
}
