package io.github.frqnny.darkenchanting.config;

import com.google.common.collect.Lists;
import draylar.omegaconfig.api.Comment;
import draylar.omegaconfig.api.Config;
import draylar.omegaconfig.api.Syncing;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

@Syncing
public class DarkEnchantingConfig implements Config {

    @Comment("All enchantments cost this much to start.")
    public float baseCost = 2.5F;

    @Comment("Make enchantments cost more.")
    public float costFactor = 1.0F;

    @Comment("How much curses should be multiplied by.")
    public float curseFactor = 3.0F;

    @Comment("How much treasure enchantments should be multiplied by.")
    public float treasureFactor = 4.0F;

    @Comment("How much the discount should be worth. Cannot be more than 1.0 or it will create a loop. It has been disabled in the code.")
    public float discountFactor = 0.75F;

    @Comment("Makes rare enchantments less cheap. Decrease the value increase the cost of Rare & Very Rare enchantments.")
    public float weightDivisor = 2.0F;

    @Comment("Not used yet! Will work in a future update. The following is an example entry. This entry represents an entry for sharpness in its default form. The Id of the enchantments, factors to multiply during enchanting (discount is when taking off enchantments, cost is when putting on),  and activated decides whether the enchantment can be used")
    public ArrayList<ConfigEnchantment> configEnchantments = Lists.newArrayList(ConfigEnchantment.of("minecraft:sharpness", 1.0F, 1.0F, true));

    @Override
    public String getName() {
        return "dark-enchanting";
    }


    @Override
    public String getExtension() {
        return "json5";
    }
}
