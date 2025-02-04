package com.github.ImagineForgee.ItemPurger.quilt;

import com.github.ImagineForgee.ItemPurger.Common.ItemPurger;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.quiltmc.qsl.lifecycle.api.event.ServerLifecycleEvents;
import org.quiltmc.qsl.lifecycle.api.event.ServerTickEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;

public final class ItemPurgerQuilt implements ModInitializer {

    @Override
    public void onInitialize(ModContainer mod) {
        ItemPurger.init();

        ServerLifecycleEvents.READY.register((MinecraftServer server) -> {
            ItemPurger.onServerStartup();
        });

        ServerLifecycleEvents.STOPPING.register((MinecraftServer server) -> {
            ItemPurger.onServerShutdown();
        });

        ServerTickEvents.END.register((MinecraftServer server) -> {
            ServerLevel level = server.getLevel(ServerLevel.OVERWORLD);
            if (level != null) {
                ItemPurger.onServerTick(level);
            }
        });
    }
}
