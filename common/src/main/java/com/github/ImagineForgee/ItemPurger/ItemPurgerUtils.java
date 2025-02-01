package com.github.ImagineForgee.ItemPurger;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ItemPurgerUtils {
    public static void sendPlayerMessage(ServerLevel level, String message) {
        for (Player player : level.getServer().getPlayerList().getPlayers()) {
            player.sendSystemMessage(Component.literal(message));
        }
    }

    public static void sendAdminMessage(ServerLevel level, String message) {
        Component componentMessage = Component.literal(message);

        for (ServerPlayer player : level.players()) {
            if (level.getServer().getPlayerList().isOp(player.getGameProfile())) {
                player.sendSystemMessage(componentMessage);
            }
        }
    }

    public static long parseTimeString(String time) {
        time = time.trim().toLowerCase();
        try {
            if (time.endsWith("ms")) {
                return Long.parseLong(time.substring(0, time.length() - 2));
            } else if (time.endsWith("s")) {
                return Long.parseLong(time.substring(0, time.length() - 1)) * 1000;
            } else if (time.endsWith("m")) {
                return Long.parseLong(time.substring(0, time.length() - 1)) * 60 * 1000;
            } else if (time.endsWith("h")) {
                return Long.parseLong(time.substring(0, time.length() - 1)) * 60 * 60 * 1000;
            } else {
                return Long.parseLong(time);
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid time format: " + time, e);
        }
    }

    public static String formatTimeString(long ticks) {
        if (ticks % (20L * 60L * 60L) == 0) {
            return ticks / (20L * 60L * 60L) + "h";
        } else if (ticks % (20L * 60L) == 0) {
            return ticks / (20L * 60L) + "m";
        } else if (ticks % 20L == 0) {
            return ticks / 20L + "s";
        }
        return ticks + " ticks";
    }

}
