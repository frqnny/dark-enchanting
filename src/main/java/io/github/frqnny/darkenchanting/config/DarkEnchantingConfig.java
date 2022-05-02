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

            Base experience cost for each enchantment.
            All enchantments will be worth at least this much.
            Default: 30
            """)
    public int baseExperienceCost = 30;

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
            
            As a security feature, the client can no longer send packets to the server requesting above
            maximum enchantment levels.
                        
            However, improper implementation from other mods can result in this check leading to undesirable effects.
            For example, mods that remove the limit will likely want this config option off to ensure
            compability.
            Default: true
            """)
    public boolean shouldRejectEnchantmentAttemptsAboveMaxValue = true;

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
            A mending example is also put on here, but this one changes the default cost of Mending to give less when you are disenchanting items. This is added to avoid Mending XP farming.
            """)
    public ArrayList<ConfigEnchantment> configEnchantments = Lists.newArrayList(
            ConfigEnchantment.of("minecraft:sharpness", 1.0F, true, 1.0F),
            ConfigEnchantment.of("minecraft:mending", 1.0F, true, 0.5F)
    );

    @Override
    public String getName() {
        return this.getModid();
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
