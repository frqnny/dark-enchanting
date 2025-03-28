package io.github.frqnny.darkenchanting.config;

import com.google.common.collect.Lists;
import io.github.frqnny.omegaconfig.api.Comment;
import io.github.frqnny.omegaconfig.api.Config;
import io.github.frqnny.omegaconfig.api.Syncing;

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

            Base experience cost for each enchantment.
            All enchantments will be worth at least this much.
            Default: 35
            """)
    public int baseExperienceCost = 35;

    @Syncing
    @Comment("""

            Each enchantment's cost is multiplied by this value.
            Default: 1.0
            """)
    public float costFactor = 1.0F;

    @Syncing
    @Comment("""

            Removing an enchantment from gear will cost XP.
            The amount received back is multiplied by this value.
            If you want it to cost XP when taking off levels, use takingOffLevelsCostsXP instead of setting this number negative.
            Default: 0.1
            """)
    public float receiveFactor = 0.1F;

    @Syncing
    @Comment("""

            Set to true if you want to cost money to remove levels of an enchantment.
            Set to false if you want to receive xp from taking off enchantment levels.
                        
            WARNING: setting to false will cause exploits, where people can enchant with the vanilla table,
             then take them off at the Dark Enchanter to receive free experience. It is advised to heavily limit the receive factor
             if you decide to turn this off.
            Default: true
            """)
    public boolean takingOffLevelsCostsXP = true;

    @Syncing
    @Comment("""

            Each curse enchantment's cost is multiplied by this value.
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
                        
            How much an enchantment's inverted weight should be multiplied with.
            Each enchantment has a Rarity, and each Rarity has a weight.
            You can check Enchantment weights on many Minecraft wikis.
            Inverted weight is calculated through (11.0 - Weight),
            and we multiply the inverted weight with the cost,
            so that cost is affected by the rarity of the Enchantment.
            This weightFactor allows you to modify the inverted weight.
                        
            Default: 3.0
            """)
    public float weightFactor = 3.0F;

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
                        
            If true, when a player takes off an curse enchantment,
            this will revert the receive factor, so that it costs quite a bit of experience
            to take of the curse enchantment.
            Turn this false to allow curses to be handled like any other enchantment.
            Default: True
            """)
    public boolean curseEnchantmentsHaveSpecialHandling = true;

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
    public ArrayList<ConfigEnchantment> configEnchantments = Lists.newArrayList(
            ConfigEnchantment.of("minecraft:sharpness", 1.0F, true, 1.0F)
    );

    @Override
    public String getName() {
        return "darkenchanting-v1";
    }


    @Override
    public String getExtension() {
        return "json5";
    }

}
