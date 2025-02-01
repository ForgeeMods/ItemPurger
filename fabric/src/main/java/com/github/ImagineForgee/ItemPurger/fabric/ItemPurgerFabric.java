package com.github.ImagineForgee.ItemPurger.fabric;

import com.github.ImagineForgee.ItemPurger.ItemPurger;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;

public final class ItemPurgerFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        ItemPurger.init();

        ServerLifecycleEvents.SERVER_STARTED.register((MinecraftServer server) -> {
            ItemPurger.onServerStartup();
        });

        ServerLifecycleEvents.SERVER_STOPPING.register((MinecraftServer server) -> {
            ItemPurger.onServerShutdown();
        });

        ServerTickEvents.END_SERVER_TICK.register((MinecraftServer server) -> {
            ServerLevel level = server.getLevel(ServerLevel.OVERWORLD);
            if (level != null) {
                ItemPurger.onServerTick(level);
            }
        });
    }
}
