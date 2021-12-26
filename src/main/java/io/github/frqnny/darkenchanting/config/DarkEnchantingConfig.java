package io.github.frqnny.darkenchanting.config;

import com.google.common.collect.Lists;
import draylar.omegaconfig.api.Comment;
import draylar.omegaconfig.api.Config;
import draylar.omegaconfig.api.Syncing;
import io.github.frqnny.darkenchanting.DarkEnchanting;

import java.util.ArrayList;

@Syncing
public class DarkEnchantingConfig implements Config {
    @Comment("""

            Performance: Decide whether Dark Conduits emit particles.
            True means they do emit particles.
            Default: true
            """)
    public boolean hasFancyShrineParticle = true;

    @Syncing
    @Comment("""

            Enchantments will cost this many levels for any transactions.
            Default: 3.0
            """)
    public float baseCost = 3.0F;

    @Syncing
    @Comment("""

            Each enchantment's cost is multiplied by this value.
            Default: 1.0
            """)
    public float costFactor = 1.0F;

    @Syncing
    @Comment("""

            Removing an enchantment from gear will give XP back.
            The amount received back is multiplied by this value.
            Default: 0.49
            """)
    public float receiveFactor = 0.49F;

    @Syncing
    @Comment("""

            Each CURSE enchantment's cost is multiplied by this value.
            Curse Enchantments will show up as red on the Dark Enchanter.
            Default: 3.0
            """)
    public float curseFactor = 3.0F;

    @Syncing
    @Comment("""

            Each TREASURE enchantment's cost is multiplied by this value.
            TREASURE Enchantments will show up as blue on the Dark Enchanter.
            Default: 4.0
            """)
    public float treasureFactor = 4.0F;

    @Syncing
    @Comment("""

            Enchantments contain a certain weight, viewable in the Minecraft Wiki.
            Weight Divisor is used to determine a specialWeightFactor.
            We perform the calculation:

            (11 - weight) / weightDivisor = specialWeightFactor

            specialWeightFactor will ALWAYS be greater than 1. Meaning, if the calculation provides a specialWeightFactor less than 1, we will just use 1 instead.

            For example, Sharpness has a weight of 10, while Mending will have a weight of 2.
            In the equation, Sharpness will have a factor of 1 while Mending will have a factor of 4.5 (WITH DEFAULTS).

            TLDR: higher values means that this will DECREASE rarer enchantment's cost, while lower values will INCREASE the cost.
            Default: 2.0
            """)
    public float weightDivisor = 2.0F;

    @Syncing
    @Comment("""

            Repairing any item will have its cost multiplied by this value.
            Default: 1.0
            """)
    public float repairFactor = 1.0F;

    @Syncing
    @Comment("""

            This number represents the discount from bookshelves.
            Default: 0.4 (40%)
            """)
    public float bookshelvesDiscount = 0.4F;

    @Syncing
    @Comment("""

            This number represents the discount from Dark Conduits.
            Default: 0.1 (10%)
            """)
    public float conduitDiscount = 0.1F;

    @Syncing
    @Comment("""

            This list can be used to configure specific enchantments.
            The values are as follows:

                enchantmentId: The In-Game identification of the Enchantment.
                    No Default.
                activated: Determines whether the enchantment will be available in the Dark Enchanter.
                    Default: true
                personalFactor: During transactions, the cost of the specific enchantment is multiplied by this value.
                    Default: 1.0
                personalReceiveFactor: Removing enchantments allows you to receive back some XP. The amount received is multiplied by this value.

            Below is an example entry for Sharpness with its defaults.
            """)
    public ArrayList<ConfigEnchantment> configEnchantments = Lists.newArrayList(ConfigEnchantment.of("minecraft:sharpness", 1.0F, true, 1.0F));

    @Override
    public String getName() {
        return "dark-enchanting";
    }


    @Override
    public String getExtension() {
        return "json5";
    }

    @Override
    public String getModid() {
        return DarkEnchanting.MODID;
    }
}
