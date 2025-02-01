package com.github.ImagineForgee.ItemPurger.Config;

import java.util.ArrayList;
import java.util.List;

public class ConfigData {
    public GeneralSettings general = new GeneralSettings();
    public WarningSettings warning = new WarningSettings();
    public ItemSettings items = new ItemSettings();
    public DynamicThresholdSettings dynamicThreshold = new DynamicThresholdSettings();
    public PlayerProtectionSettings playerProtection = new PlayerProtectionSettings();
    public UndoSettings undo = new UndoSettings();
    public EffectsSettings effects = new EffectsSettings();
    public AdminSettings admin = new AdminSettings();
    public RegionSettings region = new RegionSettings();

    public static class GeneralSettings {
        public long clearIntervalString;
        public boolean enableWarnings = true;
        public boolean clearByAge = false;
        public String itemMaxAge = "1m";
        public String clearInterval = "30m";
    }

    public static class WarningSettings {
        public List<Long> warningTimesStrings = new ArrayList<>();
        public List<String> warningTimes = List.of("10m", "5m", "10s", "30s");
    }

    public static class ItemSettings {
        public List<String> whitelist = new ArrayList<>();
        public List<String> blacklist = new ArrayList<>();
        public List<String> protectedPlayers = new ArrayList<>();
        public boolean enableSmartGrouping = false;
        public List<String> itemPriorityList = List.of("dirt", "cobblestone", "rotten_flesh");
    }

    public static class DynamicThresholdSettings {
        public boolean enableDynamicThreshold = false;
        public int tpsThreshold = 15;
        public int entityThreshold = 500;
    }

    public static class PlayerProtectionSettings {
        public boolean enablePlayerProtection = false;
        public int protectedRadius = 5;
    }

    public static class UndoSettings {
        public boolean enableUndo = false;
        public String undoTimeLimit = "1m";
    }

    public static class EffectsSettings {
        public boolean enableClearSound = false;
        public String clearSound = "minecraft:entity.generic.explode";
        public boolean enableParticles = false;
        public String particleType = "explosion";
    }

    public static class AdminSettings {
        public boolean enableAdminConfirmation = false;
    }

    public static class RegionSettings {
        public boolean enableRegionClearing = false;
        public List<String> regionList = new ArrayList<>();
    }
}
