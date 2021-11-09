package io.github.frqnny.darkenchanting.config;

import io.github.frqnny.darkenchanting.DarkEnchanting;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.Optional;

//Config class object so it sadly cannot be a Record class ;-;
public class ConfigEnchantment {
    public final String enchantmentId;
    public final float personalFactor;
    public final boolean activated;

    private ConfigEnchantment(String enchantmentId, float personalFactor, boolean activated) {
        this.enchantmentId = enchantmentId;
        this.personalFactor = personalFactor;
        this.activated = activated;
    }

    public static ConfigEnchantment of(String enchantmentId, float personalFactor, boolean activated) {
        return new ConfigEnchantment(enchantmentId, personalFactor, activated);
    }

    public static Optional<ConfigEnchantment> getConfigEnchantmentFor(Identifier id) {
        DarkEnchantingConfig config = DarkEnchanting.CONFIG;

        for (ConfigEnchantment configEnchantment : config.configEnchantments) {
            if (id.toString().equals(configEnchantment.enchantmentId)) {
                return Optional.of(configEnchantment);
            }
        }

        return Optional.empty();
    }

    public static Optional<ConfigEnchantment> getConfigEnchantmentFor(Enchantment enchantment) {
        return getConfigEnchantmentFor(Registry.ENCHANTMENT.getId(enchantment));
    }
}
