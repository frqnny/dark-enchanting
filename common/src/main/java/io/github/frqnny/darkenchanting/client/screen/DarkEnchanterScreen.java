package io.github.frqnny.darkenchanting.client.screen;

import com.google.common.collect.ImmutableList;
import dev.architectury.networking.NetworkManager;
import io.github.frqnny.darkenchanting.DarkEnchanting;
import io.github.frqnny.darkenchanting.client.screen.widget.EnchantSlidersListWidget;
import io.github.frqnny.darkenchanting.network.EnchantPacket;
import io.github.frqnny.darkenchanting.network.RepairPacket;
import io.github.frqnny.darkenchanting.screen.DarkEnchanterScreenHandler;
import io.github.frqnny.darkenchanting.util.BookcaseUtils;
import io.github.frqnny.darkenchanting.util.CostUtils;
import io.github.frqnny.darkenchanting.util.EnchantingUtils;
import io.github.frqnny.darkenchanting.util.PlayerUtils;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.util.List;
import java.util.Objects;

@Environment(EnvType.CLIENT)
public class DarkEnchanterScreen extends HandledScreen<DarkEnchanterScreenHandler> {
    public static final Identifier BACKGROUND = DarkEnchanting.id("textures/gui/dark_enchanter.png");
    public final Object2IntMap<RegistryEntry<Enchantment>> enchantmentsToApply = new Object2IntOpenHashMap<>();
    public final Object2IntMap<RegistryEntry<Enchantment>> enchantmentsOnStack = new Object2IntOpenHashMap<>();
    private final BlockPos pos;
    public int enchantCost = 0;
    public int repairCost = 0;
    public int bookshelfDiscount = 0;
    public StringBuilder bookcaseStats = new StringBuilder();
    private MinecraftClient cachedClient;
    private ButtonWidget enchantButton;
    private ButtonWidget repairButton;
    private EnchantSlidersListWidget enchantSliders;

    public DarkEnchanterScreen(DarkEnchanterScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.pos = handler.getPos();
    }

    @Override
    protected void init() {
        this.backgroundHeight += 100;
        this.titleX -= 9;
        this.playerInventoryTitleY = this.backgroundHeight - 94;

        super.init();
        this.enchantButton = this.addDrawableChild(
                ButtonWidget.builder(Text.literal("E"), button -> this.enchant())
                        .dimensions(this.x + 15, this.y + 85, 20, 20)
                        .build()
        );
        enchantButton.active = false;
        this.repairButton = this.addDrawableChild(
                ButtonWidget.builder(Text.literal("R"), button -> this.repair())
                        .dimensions(this.x + 15, this.y + 110, 20, 20)
                        .build()
        );
        repairButton.active = false;
        this.enchantSliders = this.addDrawableChild(
                new EnchantSlidersListWidget(
                        this,
                        this.x + 45,
                        this.y + 25,
                        130,
                        135
                )
        );
    }

    public void onStackUpdate(ItemStack stack) {
        enchantmentsToApply.clear();
        enchantmentsOnStack.clear();

        if (!stack.isEmpty()) {
            var enchantmentMap = EnchantingUtils.getEnchantmentMap(stack);
            for (var entry : enchantmentMap.object2IntEntrySet()) {
                RegistryEntry<Enchantment> enchantment = entry.getKey();
                int level = entry.getIntValue();
                enchantmentsToApply.put(enchantment, level);
                enchantmentsOnStack.put(enchantment, level);
            }

        }
        recalculateEnchantmentCost();
        recalculateRepairCost();

        enchantSliders.populateSliders();
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        context.drawTexture(RenderLayer::getGuiTextured, BACKGROUND, x - 9, y, 0, 0, this.backgroundWidth + 40, this.backgroundHeight, 304, 304);
        context.drawTooltip(MinecraftClient.getInstance().textRenderer, this.getTooltip(), x - 120, y + 43);
    }

    public void recalculateEnchantmentCost() {
        ClientPlayerEntity player = this.getClient().player;
        int totalExperience = PlayerUtils.syncAndGetTotalExperience(player);
        ClientWorld world = this.getClient().world;
        enchantCost = BookcaseUtils.applyDiscount(CostUtils.getExperienceCost(this.cachedClient.world, enchantmentsToApply, enchantmentsOnStack), world, pos);

        if (!bookcaseStats.isEmpty()) {
            bookcaseStats = new StringBuilder(3);
        }

        if (BookcaseUtils.checkInnerObsidianRing(world, pos)) {
            bookcaseStats.append('☆');
        }

        if (BookcaseUtils.checkOuterObsidianRing(world, pos)) {
            bookcaseStats.append('☆');
        }

        if (BookcaseUtils.checkConduits(world, pos)) {
            bookcaseStats.append('☆');
        }

        bookshelfDiscount = (int) (BookcaseUtils.getDiscount(world, pos) * 100);

        boolean enchantmentsHaveChanged = false;

        for (var entry : enchantmentsToApply.object2IntEntrySet()) {
            RegistryEntry<Enchantment> enchantment = entry.getKey();
            int level = entry.getIntValue();

            if (level == 0) {
                if (enchantmentsOnStack.containsKey(enchantment)) {
                    enchantmentsHaveChanged = true; // enchantment fully removed
                    break;
                }
            } else {
                if (enchantmentsOnStack.containsKey(enchantment)) {
                    if (enchantmentsOnStack.getInt(enchantment) != level) {
                        enchantmentsHaveChanged = true; // enchantment level changed
                        break;
                    }
                } else {
                    enchantmentsHaveChanged = true; // enchantment added to stack
                    break;
                }
            }
        }


        enchantButton.active = (totalExperience >= enchantCost && enchantmentsHaveChanged) || player.isCreative();

    }

    public void recalculateRepairCost() {
        ClientPlayerEntity player = this.getClient().player;
        ClientWorld world = this.getClient().world;
        int totalExperience = PlayerUtils.syncAndGetTotalExperience(player);
        ItemStack stack = getScreenHandler().inv.getEnchantStack();
        this.repairCost = BookcaseUtils.applyDiscount(CostUtils.getRepairCost(stack), world, pos);
        repairButton.active = (stack.isDamaged() && totalExperience >= this.repairCost) || player.isCreative();
    }

    public void onSliderValueChange(RegistryEntry<Enchantment> enchantment, int level) {
        if (enchantmentsToApply.containsKey(enchantment)) {
            enchantmentsToApply.replace(enchantment, level);
        } else {
            enchantmentsToApply.put(enchantment, level);
        }

        recalculateEnchantmentCost();
        this.enchantSliders.checkIncompabilities();
    }

    public void enchant() {
        NetworkManager.sendToServer(new EnchantPacket(pos, enchantmentsToApply));
    }

    public void repair() {
        NetworkManager.sendToServer(new RepairPacket(pos));
    }

    public List<Text> getTooltip() {
        String costLine;
        if (DarkEnchanterScreen.this.enchantCost >= 0) {
            costLine = "Pay: " + DarkEnchanterScreen.this.enchantCost + " XP";
        } else {
            costLine = "Receive: " + -DarkEnchanterScreen.this.enchantCost + " XP";
        }
        var emptyLine = Text.literal("");

        return ImmutableList.of(
                Text.literal("Enchant Cost:").formatted(Formatting.DARK_GREEN),
                Text.literal(costLine),
                emptyLine,
                emptyLine,
                Text.literal("Repair Cost:").formatted(Formatting.BLUE),
                Text.literal("Pay: " + DarkEnchanterScreen.this.repairCost + " XP"),
                emptyLine,
                emptyLine,
                Text.literal("Shrine Discount:").formatted(Formatting.DARK_PURPLE),
                Text.literal(bookcaseStats.toString() + " " + DarkEnchanterScreen.this.bookshelfDiscount + " %"),
                emptyLine,
                emptyLine,
                Text.literal("You have: " + PlayerUtils.getTotalExperience(getClient().player) + " XP").formatted(Formatting.GOLD),
                emptyLine,
                emptyLine);
    }


    public MinecraftClient getClient() {
        if (this.cachedClient == null) {
            this.cachedClient = Objects.requireNonNull(MinecraftClient.getInstance());
        }

        return this.cachedClient;
    }

    @Override
    protected void handledScreenTick() {
        super.handledScreenTick();
        var screenHandler = getScreenHandler();
        if (screenHandler.hasStackUpdate()) {
            screenHandler.handleStackUpdate();
            this.onStackUpdate(screenHandler.inv.getEnchantStack());
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        this.drawMouseoverTooltip(context, mouseX, mouseY);
    }

    //fix dragging issues not implemented by vanilla
    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (this.getFocused() != null && this.isDragging()) {
            if (this.getFocused() instanceof EnchantSlidersListWidget listWidget) {
                listWidget.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
            }
        }

        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }
}

