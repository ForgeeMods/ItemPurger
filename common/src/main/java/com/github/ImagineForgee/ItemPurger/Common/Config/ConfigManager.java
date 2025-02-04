package com.github.ImagineForgee.ItemPurger.Common.Config;

import com.electronwill.nightconfig.core.file.FileConfig;
import com.github.ImagineForgee.ItemPurger.Common.ItemPurger;
import com.github.ImagineForgee.ItemPurger.Common.ItemPurgerUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ConfigManager {
    private static final Path CONFIG_PATH = Path.of(ItemPurger.CONFIG_DIR.toAbsolutePath() + "/itempurger.toml");
    private static ConfigData config;
    private static FileConfig configFile;

    public static void loadConfig() {
        ItemPurger.LOGGER.info("Loading config from: " + CONFIG_PATH);
        try {
            configFile = FileConfig.of(CONFIG_PATH.toFile());
            if (!Files.exists(CONFIG_PATH)) {
                saveDefaultConfig();
            } else {
                configFile.load();
            }
            config = new ConfigData();

            config.general.clearInterval = configFile.get("general.clearInterval");
            config.general.clearIntervalString = ItemPurgerUtils.parseTimeString(config.general.clearInterval);
            config.general.enableWarnings = configFile.get("general.enableWarnings");
            config.general.clearByAge = configFile.get("general.clearByAge");
            config.general.itemMaxAge = configFile.get("general.itemMaxAge");

            List<String> rawWarningTimes = configFile.get("warning.warningTimes");
            if (rawWarningTimes == null) {
                rawWarningTimes = new ArrayList<>();
            }
            config.warning.warningTimes = rawWarningTimes;
            config.warning.warningTimesStrings = new ArrayList<>();
            for (String warning : rawWarningTimes) {
                config.warning.warningTimesStrings.add(ItemPurgerUtils.parseTimeString(warning));
            }

            config.items.whitelist = configFile.get("items.whitelist");
            config.items.blacklist = configFile.get("items.blacklist");
            config.items.protectedPlayers = configFile.get("items.protectedPlayers");
            config.items.enableSmartGrouping = configFile.get("items.enableSmartGrouping");
            config.items.itemPriorityList = configFile.get("items.itemPriorityList");

            config.dynamicThreshold.enableDynamicThreshold = configFile.get("dynamicThreshold.enableDynamicThreshold");
            config.dynamicThreshold.tpsThreshold = configFile.get("dynamicThreshold.tpsThreshold");
            config.dynamicThreshold.entityThreshold = configFile.get("dynamicThreshold.entityThreshold");

            config.playerProtection.enablePlayerProtection = configFile.get("playerProtection.enablePlayerProtection");
            config.playerProtection.protectedRadius = configFile.get("playerProtection.protectedRadius");

            config.undo.enableUndo = configFile.get("undo.enableUndo");
            config.undo.undoTimeLimit = configFile.get("undo.undoTimeLimit");

            config.effects.enableClearSound = configFile.get("effects.enableClearSound");
            config.effects.clearSound = configFile.get("effects.clearSound");
            config.effects.enableParticles = configFile.get("effects.enableParticles");
            config.effects.particleType = configFile.get("effects.particleType");

            config.admin.enableAdminConfirmation = configFile.get("admin.enableAdminConfirmation");

            config.region.enableRegionClearing = configFile.get("region.enableRegionClearing");
            config.region.regionList = configFile.get("region.regionList");

        } catch (Exception e) {
            System.err.println("Failed to load config: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void saveConfig() {
        try {
            config.general.clearInterval = ItemPurgerUtils.formatTimeString(config.general.clearIntervalString);
            List<String> formattedWarnings = new ArrayList<>();
            for (Long time : config.warning.warningTimesStrings) {
                formattedWarnings.add(ItemPurgerUtils.formatTimeString(time));
            }
            config.warning.warningTimes = formattedWarnings;

            configFile.set("general.clearInterval", config.general.clearInterval);
            configFile.set("general.enableWarnings", config.general.enableWarnings);
            configFile.set("general.clearByAge", config.general.clearByAge);
            configFile.set("general.itemMaxAge", config.general.itemMaxAge);

            configFile.set("warning.warningTimes", config.warning.warningTimes);

            configFile.set("items.whitelist", config.items.whitelist);
            configFile.set("items.blacklist", config.items.blacklist);
            configFile.set("items.protectedPlayers", config.items.protectedPlayers);
            configFile.set("items.enableSmartGrouping", config.items.enableSmartGrouping);
            configFile.set("items.itemPriorityList", config.items.itemPriorityList);

            configFile.set("dynamicThreshold.enableDynamicThreshold", config.dynamicThreshold.enableDynamicThreshold);
            configFile.set("dynamicThreshold.tpsThreshold", config.dynamicThreshold.tpsThreshold);
            configFile.set("dynamicThreshold.entityThreshold", config.dynamicThreshold.entityThreshold);

            configFile.set("playerProtection.enablePlayerProtection", config.playerProtection.enablePlayerProtection);
            configFile.set("playerProtection.protectedRadius", config.playerProtection.protectedRadius);

            configFile.set("undo.enableUndo", config.undo.enableUndo);
            configFile.set("undo.undoTimeLimit", config.undo.undoTimeLimit);

            configFile.set("effects.enableClearSound", config.effects.enableClearSound);
            configFile.set("effects.clearSound", config.effects.clearSound);
            configFile.set("effects.enableParticles", config.effects.enableParticles);
            configFile.set("effects.particleType", config.effects.particleType);

            configFile.set("admin.enableAdminConfirmation", config.admin.enableAdminConfirmation);

            configFile.set("region.enableRegionClearing", config.region.enableRegionClearing);
            configFile.set("region.regionList", config.region.regionList);

            configFile.save();
        } catch (Exception e) {
            System.err.println("Failed to save config: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void saveDefaultConfig() throws IOException {
        if (!Files.exists(CONFIG_PATH)) {
            Files.createFile(CONFIG_PATH);
        }
        configFile = FileConfig.of(CONFIG_PATH.toFile());
        config = new ConfigData();

        config.general.clearInterval = "10m";
        config.general.enableWarnings = true;
        config.general.clearByAge = false;
        config.general.itemMaxAge = "1m";

        config.warning.warningTimes = List.of("5m", "10m", "15m");
        config.warning.warningTimesStrings = new ArrayList<>();
        for (String warning : config.warning.warningTimes) {
            config.warning.warningTimesStrings.add(ItemPurgerUtils.parseTimeString(warning));
        }

        config.items.whitelist = new ArrayList<>();
        config.items.blacklist = new ArrayList<>();
        config.items.protectedPlayers = new ArrayList<>();
        config.items.enableSmartGrouping = false;
        config.items.itemPriorityList = List.of("dirt", "cobblestone", "rotten_flesh");

        config.dynamicThreshold.enableDynamicThreshold = false;
        config.dynamicThreshold.tpsThreshold = 15;
        config.dynamicThreshold.entityThreshold = 500;

        config.playerProtection.enablePlayerProtection = false;
        config.playerProtection.protectedRadius = 5;

        config.undo.enableUndo = false;
        config.undo.undoTimeLimit = "1m";

        config.effects.enableClearSound = false;
        config.effects.clearSound = "minecraft:entity.generic.explode";
        config.effects.enableParticles = false;
        config.effects.particleType = "explosion";

        config.admin.enableAdminConfirmation = false;

        config.region.enableRegionClearing = false;
        config.region.regionList = new ArrayList<>();

        configFile.set("general.clearInterval", config.general.clearInterval);
        configFile.set("general.enableWarnings", config.general.enableWarnings);
        configFile.set("general.clearByAge", config.general.clearByAge);
        configFile.set("general.itemMaxAge", config.general.itemMaxAge);

        configFile.set("warning.warningTimes", config.warning.warningTimes);

        configFile.set("items.whitelist", config.items.whitelist);
        configFile.set("items.blacklist", config.items.blacklist);
        configFile.set("items.protectedPlayers", config.items.protectedPlayers);
        configFile.set("items.enableSmartGrouping", config.items.enableSmartGrouping);
        configFile.set("items.itemPriorityList", config.items.itemPriorityList);

        configFile.set("dynamicThreshold.enableDynamicThreshold", config.dynamicThreshold.enableDynamicThreshold);
        configFile.set("dynamicThreshold.tpsThreshold", config.dynamicThreshold.tpsThreshold);
        configFile.set("dynamicThreshold.entityThreshold", config.dynamicThreshold.entityThreshold);

        configFile.set("playerProtection.enablePlayerProtection", config.playerProtection.enablePlayerProtection);
        configFile.set("playerProtection.protectedRadius", config.playerProtection.protectedRadius);

        configFile.set("undo.enableUndo", config.undo.enableUndo);
        configFile.set("undo.undoTimeLimit", config.undo.undoTimeLimit);

        configFile.set("effects.enableClearSound", config.effects.enableClearSound);
        configFile.set("effects.clearSound", config.effects.clearSound);
        configFile.set("effects.enableParticles", config.effects.enableParticles);
        configFile.set("effects.particleType", config.effects.particleType);

        configFile.set("admin.enableAdminConfirmation", config.admin.enableAdminConfirmation);

        configFile.set("region.enableRegionClearing", config.region.enableRegionClearing);
        configFile.set("region.regionList", config.region.regionList);

        configFile.save();
    }

    public static ConfigData getConfig() {
        if (config == null) {
            loadConfig();
        }
        return config;
    }
}
