package io.github.frqnny.darkenchanting.block;

import net.minecraft.block.WallTorchBlock;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.util.Identifier;

import io.github.frqnny.darkenchanting.DarkEnchanting;

public class WallDarkConduitBlock extends WallTorchBlock 
{
    public static final Identifier ID = new Identifier(DarkEnchanting.MODID, "wall_dark_conduit");
    
    public WallDarkConduitBlock(Settings settings, ParticleEffect particle){
        super(settings, particle);
    }
}