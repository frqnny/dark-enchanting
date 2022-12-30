package io.github.frqnny.darkenchanting.config;

import io.github.frqnny.darkenchanting.DarkEnchanting;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.registry.Registry;

import java.util.Optional;

public class ConfigEnchantment {
    public final String enchantmentId;
    public final boolean activated;
    public final float personalFactor;
    public final float personalReceiveFactor;

    private ConfigEnchantment(String enchantmentId, float personalFactor, boolean activated, float personalReceiveFactor) {
        this.enchantmentId = enchantmentId;
        this.personalFactor = personalFactor;
        this.activated = activated;
        this.personalReceiveFactor = personalReceiveFactor;
    }

    public static ConfigEnchantment of(String enchantmentId, float personalFactor, boolean activated, float personalReceiveFactor) {
        return new ConfigEnchantment(enchantmentId, personalFactor, activated, personalReceiveFactor);
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
        return getConfigEnchantmentFor(Registries.ENCHANTMENT.getId(enchantment));
    }
}
