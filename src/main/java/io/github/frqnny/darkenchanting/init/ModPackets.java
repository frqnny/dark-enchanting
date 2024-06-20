package io.github.frqnny.darkenchanting.init;

import io.github.frqnny.darkenchanting.network.EnchantPacket;
import io.github.frqnny.darkenchanting.network.RepairPacket;
import io.github.frqnny.darkenchanting.screen.DarkEnchanterScreenHandler;
import io.github.frqnny.darkenchanting.util.BookcaseUtils;
import io.github.frqnny.darkenchanting.util.EnchantingUtils;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.math.BlockPos;

public class ModPackets {

    public static void init() {
        PayloadTypeRegistry.playC2S().register(EnchantPacket.PACKET_ID, EnchantPacket.PACKET_CODEC);
        PayloadTypeRegistry.playC2S().register(RepairPacket.PACKET_ID, RepairPacket.PACKET_CODEC);

        ServerPlayNetworking.registerGlobalReceiver(EnchantPacket.PACKET_ID, (payload, context) -> {
            BlockPos pos = payload.pos();

            context.server().execute(() -> {
                ScreenHandler screen = context.player().currentScreenHandler;

                if (screen instanceof DarkEnchanterScreenHandler holder) {
                    ItemStack stack = holder.getActualStack();
                    Object2IntMap<Enchantment> currentEnchantments = EnchantingUtils.getEnchantmentMap(stack);

                    if (EnchantingUtils.applyEnchantXP(context.player(), EnchantingUtils.convert(payload.enchantments().object2IntEntrySet()), currentEnchantments, BookcaseUtils.getDiscount(context.player().getWorld(), pos))) {
                        EnchantingUtils.set(EnchantingUtils.convert(payload.enchantments().object2IntEntrySet()), stack);
                        context.player().incrementStat(Stats.ENCHANT_ITEM);
                        Criteria.ENCHANTED_ITEM.trigger(context.player(), stack, 1);
                    }

                    context.player().closeHandledScreen();
                    context.server().getOverworld().playSound(null, pos, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.BLOCKS, 1.0f, context.server().getOverworld().random.nextFloat() * 0.2f + 0.9f);

                }
            });
        });

        ServerPlayNetworking.registerGlobalReceiver(RepairPacket.PACKET_ID, (payload, context) -> {
            BlockPos pos = payload.pos();

            context.server().execute(() -> {
                ScreenHandler screen = context.player().currentScreenHandler;
                if (screen instanceof DarkEnchanterScreenHandler holder) {
                    ItemStack stack = holder.getActualStack();
                    if (EnchantingUtils.applyRepairXP(context.player(), stack, BookcaseUtils.getDiscount(context.player().getEntityWorld(), pos))) {
                        stack.setDamage(0);
                    }
                }
                context.player().closeHandledScreen();
                context.server().getOverworld().playSound(null, pos, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.BLOCKS, 1.0f, context.server().getOverworld().random.nextFloat() * 0.2f + 0.9f);
            });
        });
    }
}
