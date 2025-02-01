package com.github.ImagineForgee.ItemPurger.forge;

import com.github.ImagineForgee.ItemPurger.ItemPurger;
import dev.architectury.platform.forge.EventBuses;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(ItemPurger.MOD_ID)
public final class ItemPurgerForge {
    public ItemPurgerForge() {
        EventBuses.registerModEventBus(ItemPurger.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        ItemPurger.init();

        MinecraftForge.EVENT_BUS.addListener(this::onServerStarting);
        MinecraftForge.EVENT_BUS.addListener(this::onServerStopping);
        MinecraftForge.EVENT_BUS.addListener(this::onServerTick);
    }

    private void onServerStarting(ServerStartingEvent event) {
        ItemPurger.onServerStartup();
    }

    private void onServerStopping(ServerStoppingEvent event) {
        ItemPurger.onServerShutdown();
    }

    private void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.ServerTickEvent.Phase.END) {
            ServerLevel level = event.getServer().getLevel(ServerLevel.OVERWORLD);
            if (level != null) {
                ItemPurger.onServerTick(level);
            }
        }
    }
}
