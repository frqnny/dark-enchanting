package io.github.frqnny.darkenchanting.init;

import dev.architectury.registry.menu.MenuRegistry;
import dev.architectury.registry.registries.Registrar;
import io.github.frqnny.darkenchanting.DarkEnchanting;
import io.github.frqnny.darkenchanting.client.screen.DarkEnchanterScreen;
import io.github.frqnny.darkenchanting.screen.DarkEnchanterScreenHandler;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;

public class ModGUIs {
    public static final ScreenHandlerType<DarkEnchanterScreenHandler> DARK_ENCHANTER = MenuRegistry.ofExtended(
            (id, playerInv, buf) -> new DarkEnchanterScreenHandler(id, playerInv, ScreenHandlerContext.create(playerInv.player.getEntityWorld(), buf.readBlockPos()))
    );


    public static void init() {
        Registrar<ScreenHandlerType<?>> screenHandlers = DarkEnchanting.MANAGER.get().get(RegistryKeys.SCREEN_HANDLER);
        screenHandlers.register(DarkEnchanting.id("dark_enchanter"), () -> DARK_ENCHANTER);

    }

    @SuppressWarnings("all")
    public static void clientInit() {
        MenuRegistry.registerScreenFactory(DARK_ENCHANTER, DarkEnchanterScreen::new);
    }
}
