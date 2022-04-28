package io.github.frqnny.darkenchanting.init;

import io.github.frqnny.darkenchanting.DarkEnchanting;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModTags {
    public static final TagKey<Block> BOOKSHELVES = TagKey.of(Registry.BLOCK_KEY, new Identifier("c", "bookshelves"));
    public static final TagKey<Enchantment> DISABLED = TagKey.of(Registry.ENCHANTMENT_KEY, DarkEnchanting.id("enchantment/disabled"));
    public static final TagKey<Block> CRYING_OBSIDIAN = TagKey.of(Registry.BLOCK_KEY, DarkEnchanting.id("crying_obsidian"));

    public static void init() {
    }
}
