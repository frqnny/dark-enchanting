package io.github.frqnny.darkenchanting.config;

import io.github.frqnny.darkenchanting.DarkEnchanting;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.Optional;

public class ConfigEnchantment {
    private ConfigEnchantment(String enchantmentId, float discountFactor, float costFactor, boolean activated) {
        this.enchantmentId = enchantmentId;
        this.discountFactorEnchantment = discountFactor;
        this.costFactorEnchantment = costFactor;
        this.activated = activated;
    }

    public String enchantmentId;
    public float discountFactorEnchantment;
    public float costFactorEnchantment;
    public boolean activated;

    public static ConfigEnchantment of(String enchantmentId, float discountFactor, float costFactor, boolean activated) {
        return new ConfigEnchantment(enchantmentId, discountFactor, costFactor, activated);
    }

    public static Optional<ConfigEnchantment> getConfigEnchantmentFor(Identifier id) {
        DarkEnchantingConfig config = DarkEnchanting.config;

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
