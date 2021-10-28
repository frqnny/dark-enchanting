package io.github.frqnny.darkenchanting.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.WallTorchBlock;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.Random;

import io.github.frqnny.darkenchanting.DarkEnchanting;

public class WallDarkConduitBlock extends WallTorchBlock 
{
    public static final Identifier ID = new Identifier(DarkEnchanting.MODID, "wall_dark_conduit");
    
    public WallDarkConduitBlock(Settings settings, ParticleEffect particle){
        super(settings, particle);
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random)
    {
        Direction direction = state.get(FACING).getOpposite();
        double d = (double)pos.getX() + 0.5D;
        double e = (double)pos.getY() + 0.4D;
        double f = (double)pos.getZ() + 0.5D;
        world.addParticle(ParticleTypes.SMOKE, d + 0.37D * (double)direction.getOffsetX(), e + 0.22D, f + 0.37D * (double)direction.getOffsetZ(), 0.0D, 0.0D, 0.0D);
        world.addParticle(this.particle, d + 0.37D * (double)direction.getOffsetX(), e + 0.22D, f + 0.37D * (double)direction.getOffsetZ(), 0.0D, 0.0D, 0.0D);
    }
}