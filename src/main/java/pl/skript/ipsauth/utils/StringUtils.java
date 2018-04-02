package pl.skript.ipsauth.utils;

import org.bukkit.ChatColor;

public final class StringUtils {

    public static String color(String text) {
        if (text == null)
            text = "null";
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public static String getOrElse(String text, String elseText) {
        if (text == null)
            return elseText != null ? elseText : "null";
        else
            return text;
    }

    private StringUtils() {}

}
