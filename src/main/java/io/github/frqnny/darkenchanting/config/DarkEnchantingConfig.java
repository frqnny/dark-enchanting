package io.github.frqnny.darkenchanting.config;

import draylar.omegaconfig.api.Comment;
import draylar.omegaconfig.api.Config;
import draylar.omegaconfig.api.Syncing;
import net.minecraft.util.Identifier;

@Syncing
public class DarkEnchantingConfig implements Config {
    public float baseCost = 2.5F;

    public float costFactor = 1.0F;

    public float curseFactor = 3.0F;

    public float treasureFactor = 4.0F;

    @Comment("Makes rare enchantments less cheap. Decrease the value increase the cost of Rare & Very Rare enchantments.")
    public float weightDivisor = 2.0F;

    @Comment("Not used yet! Will work in a future update.")
    public Identifier[] blacklist;

    @Override
    public String getName() {
        return "dark-enchanting";
    }


    @Override
    public String getExtension() {
        return "json5";
    }
}
