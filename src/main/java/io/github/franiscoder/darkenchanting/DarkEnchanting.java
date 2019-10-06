package io.github.franiscoder.darkenchanting;


import io.github.franiscoder.darkenchanting.block.DarkEnchanter;
import io.github.franiscoder.darkenchanting.blockentity.DEBlockEntity;
import io.github.franiscoder.darkenchanting.blockentity.inventory.ImplementedInventory;
import io.github.franiscoder.darkenchanting.gui.DEGuiController;
import io.github.franiscoder.darkenchanting.init.BlockEntities;
import io.github.franiscoder.darkenchanting.init.ModBlocks;
import io.github.franiscoder.darkenchanting.init.ModItems;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.container.BlockContext;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import java.util.Map;

public class DarkEnchanting implements ModInitializer {
    public static String MODID = "dark-enchanting";
    private ModItems ItemRegistry = new ModItems();
    private ModBlocks BlockRegistry = new ModBlocks();
    public static final Identifier ENCHANT_PACKET = new Identifier(DarkEnchanting.MODID, "enchant_packet");


    public static final ItemGroup DARK_ENCHANTING = FabricItemGroupBuilder.create(
            new Identifier(MODID, "dark_enchanting_group"))
            .icon(() -> new ItemStack(ModBlocks.DARK_ENCHANTER))
            .appendItems(stacks ->
            {
                stacks.add(new ItemStack(ModItems.ENCHANTED_GEM));
                stacks.add(new ItemStack(ModBlocks.DARK_ENCHANTER));
            })
            .build();

    @Override
    public void onInitialize() {
        ItemRegistry.registerItems();
        BlockRegistry.registerBlocks();
        BlockEntities.registerBlockEntities();
        ContainerProviderRegistry.INSTANCE.registerFactory(DarkEnchanter.ID, (syncId, id, player, buf) -> new DEGuiController(syncId, player.inventory, BlockContext.create(player.world, buf.readBlockPos())));

        ServerSidePacketRegistry.INSTANCE.register(ENCHANT_PACKET,
                (packetContext, attachedData) -> {
                    // Read in the correct, networking thread
                    BlockPos pos = attachedData.readBlockPos();
                    World world = packetContext.getPlayer().getEntityWorld();
                    Enchantment enchantment = Registry.ENCHANTMENT.get(attachedData.readIdentifier());
                    int level = attachedData.readInt();


                    packetContext.getTaskQueue().execute(() -> {
                        BlockEntity be = world.getBlockEntity(pos);
                        Inventory inv = null;
                        if (be instanceof DEBlockEntity) {
                            inv = ImplementedInventory.of(((DEBlockEntity) be).getItems());
                        }

                        ItemStack stack = inv.getInvStack(0);
                        Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(stack);
                        map.put(enchantment, level);
                        EnchantmentHelper.set(map, stack);
                        System.out.println("Dark Enchanting: Enchanting Packet sent succesfully");

                    });

                });
    }
}
