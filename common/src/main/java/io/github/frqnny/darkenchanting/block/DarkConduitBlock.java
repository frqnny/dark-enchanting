package io.github.frqnny.darkenchanting.block;

import io.github.frqnny.darkenchanting.DarkEnchanting;
import net.minecraft.block.BlockState;
import net.minecraft.block.TorchBlock;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class DarkConduitBlock extends TorchBlock {
    public static final Identifier ID = DarkEnchanting.id("dark_conduit");

    public DarkConduitBlock(SimpleParticleType particle, Settings settings) {
        super(particle, settings);

    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (DarkEnchanting.CONFIG.hasFancyShrineParticle) {
            int i = pos.getX();
            int j = pos.getY();
            int k = pos.getZ();
            double d = (double) i + random.nextDouble();
            double e = (double) j + 0.7D;
            double f = (double) k + random.nextDouble();
            world.addParticleClient(ParticleTypes.FALLING_OBSIDIAN_TEAR, d, e, f, 0.0D, 0.0D, 0.0D);
            BlockPos.Mutable mutable = new BlockPos.Mutable();

            for (int l = 0; l < 5; ++l) {
                mutable.set(i + MathHelper.nextInt(random, -4, 4), j - random.nextInt(5), k + MathHelper.nextInt(random, -4, 4));
                BlockState blockState = world.getBlockState(mutable);
                if (!blockState.isFullCube(world, mutable)) {
                    world.addParticleClient(ParticleTypes.ENCHANT, (double) mutable.getX() + random.nextDouble(), (double) mutable.getY() + random.nextDouble(), (double) mutable.getZ() + random.nextDouble(), 0.0D, 0.0D, 0.0D);
                }
            }
        }
    }
}