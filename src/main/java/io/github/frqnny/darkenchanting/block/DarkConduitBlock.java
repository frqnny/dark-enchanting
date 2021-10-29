package io.github.frqnny.darkenchanting.block;

import net.minecraft.block.TorchBlock;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.util.Identifier;

import io.github.frqnny.darkenchanting.DarkEnchanting;

public class DarkConduitBlock extends TorchBlock 
{
    public static final Identifier ID = new Identifier(DarkEnchanting.MODID, "dark_conduit");
    
    public DarkConduitBlock(Settings settings, ParticleEffect particle)
    {
        super(settings, particle);
    }
}