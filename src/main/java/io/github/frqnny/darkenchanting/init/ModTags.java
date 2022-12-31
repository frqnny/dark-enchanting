package io.github.frqnny.darkenchanting.init;

import io.github.frqnny.darkenchanting.DarkEnchanting;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class ModTags {
    public static final TagKey<Block> BOOKSHELVES = TagKey.of(RegistryKeys.BLOCK, new Identifier("c", "bookshelves"));
    public static final TagKey<Enchantment> DISABLED = TagKey.of(RegistryKeys.ENCHANTMENT, DarkEnchanting.id("enchantment/disabled"));
    public static final TagKey<Block> CRYING_OBSIDIAN = TagKey.of(RegistryKeys.BLOCK, DarkEnchanting.id("crying_obsidian"));

    public static void init() {
    }
}
