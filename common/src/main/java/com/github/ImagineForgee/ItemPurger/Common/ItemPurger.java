package com.github.ImagineForgee.ItemPurger.Common;

import com.github.ImagineForgee.ItemPurger.Common.Config.ConfigData;
import com.github.ImagineForgee.ItemPurger.Common.Config.ConfigManager;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public final class ItemPurger {
    public static final String MOD_ID = "itempurger";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID.toUpperCase());
    public static final Path CONFIG_DIR = Path.of("config");

    private static final Map<ServerLevel, Long> lastClearTimes = new HashMap<>();
    private static final List<ItemStack> backup = new ArrayList<>();
    private static final Set<Long> sentWarnings = new HashSet<>();

    private static int totalItemsCleared = 0;

    public static void init() {
        LOGGER.info("Initializing {}", MOD_ID.toUpperCase());

        try {
            if (!Files.exists(CONFIG_DIR)) {
                Files.createDirectories(CONFIG_DIR);
                LOGGER.info("Created configuration directory at {}", CONFIG_DIR);
            }
        } catch (Exception e) {
            LOGGER.error("Failed to create configuration directory at {}", CONFIG_DIR, e);
        }

        ConfigManager.loadConfig();
        LOGGER.info("Configuration loaded: {}", ConfigManager.getConfig());
    }

    public static void onServerStartup() {
        LOGGER.info("{} server startup complete.", MOD_ID.toUpperCase());
    }

    public static void onServerShutdown() {
        LOGGER.info("{} is shutting down.", MOD_ID.toUpperCase());
    }

    public static void onServerTick(ServerLevel level) {
        ConfigData config = ConfigManager.getConfig();
        long currentTime = level.getGameTime();
        long lastClearTime = lastClearTimes.getOrDefault(level, 0L);
        long timeUntilNextClear = config.general.clearIntervalString - (currentTime - lastClearTime);

        if (timeUntilNextClear <= 0) {
            clearItems(level);
            lastClearTimes.put(level, currentTime);
            sentWarnings.clear();
        } else if (config.general.enableWarnings) {
            config.warning.warningTimes.forEach(warningTime -> {
                long warningSeconds = ItemPurgerUtils.parseTimeString(warningTime);
                if (timeUntilNextClear <= warningSeconds && !sentWarnings.contains(warningSeconds)) {
                    String countdownMessage = formatCountdownMessage(timeUntilNextClear);
                    ItemPurgerUtils.sendPlayerMessage(level, "⚠ Warning: Items will be cleared in " + countdownMessage + "!");
                    sentWarnings.add(warningSeconds);
                }
            });
        }
    }

    private static void clearItems(ServerLevel level) {
        ConfigData config = ConfigManager.getConfig();
        int itemsCleared = 0;

        try {
            for (Entity entity : level.getEntitiesOfClass(ItemEntity.class, new AABB(
                    level.getMinBuildHeight(), 0, level.getMinBuildHeight(),
                    level.getMaxBuildHeight(), 256, level.getMaxBuildHeight()))) {

                if (entity instanceof ItemEntity itemEntity && shouldClearItem(itemEntity, config)) {
                    backupItem(itemEntity);
                }
            }

            if (itemsCleared > 0) {
                ItemPurgerUtils.sendPlayerMessage(level, "⛔ " + itemsCleared + " items have been removed to prevent lag.");
                ItemPurgerUtils.sendAdminMessage(level, "✔ Purge completed: " + itemsCleared + " items cleared.");
            }
        } catch (Exception e) {
            LOGGER.error("Error clearing items: ", e);
        }

        totalItemsCleared += itemsCleared;
        LOGGER.info("Cleared {} items in this purge.", itemsCleared);
    }

    private static boolean shouldClearItem(ItemEntity itemEntity, ConfigData config) {
        if (config.general.clearByAge && itemEntity.tickCount > ItemPurgerUtils.parseTimeString(config.general.itemMaxAge)) {
            return true;
        }

        String itemName = itemEntity.getItem().getItem().getDescriptionId();
        if (config.items.whitelist.contains(itemName)) return false;
        if (config.items.blacklist.isEmpty() || config.items.blacklist.contains(itemName)) return true;
        return !config.items.protectedPlayers.contains(itemEntity.getThrower());
    }

    private static void backupItem(ItemEntity itemEntity) {
        backup.add(itemEntity.getItem().copy());
    }

    public static void reloadConfig() {
        ConfigManager.loadConfig();
        LOGGER.info("Configuration reloaded: {}", ConfigManager.getConfig());
    }

    private static String formatCountdownMessage(long seconds) {
        long minutes = seconds / 60;
        long remainingSeconds = seconds % 60;
        if (minutes > 0) {
            return minutes + " minute(s) " + remainingSeconds + " second(s)";
        } else {
            return remainingSeconds + " second(s)";
        }
    }
}
