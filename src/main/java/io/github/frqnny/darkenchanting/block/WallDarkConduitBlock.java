package io.github.frqnny.darkenchanting.block;

import io.github.frqnny.darkenchanting.DarkEnchanting;
import net.minecraft.block.BlockState;
import net.minecraft.block.WallTorchBlock;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class WallDarkConduitBlock extends WallTorchBlock {
    public static final Identifier ID = new Identifier(DarkEnchanting.MODID, "wall_dark_conduit");

    public WallDarkConduitBlock(Settings settings, ParticleEffect particle) {
        super(settings, particle);
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        Direction direction = state.get(FACING);
        double d = (double) pos.getX() + 0.5D;
        double e = (double) pos.getY() + 0.8D;
        double f = (double) pos.getZ() + 0.5D;
        Direction direction2 = direction.getOpposite();
        world.addParticle(ParticleTypes.ENCHANT, d + 0.27D * (double) direction2.getOffsetX(), e + 0.22D, f + 0.27D * (double) direction2.getOffsetZ(), 0.0D, -0.3D, 0.0D);
        world.addParticle(this.particle, d + 0.27D * (double) direction2.getOffsetX(), e + 0.22D, f + 0.27D * (double) direction2.getOffsetZ(), 0.0D, 0.0D, 0.0D);
    }
}