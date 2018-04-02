package pl.skript.ipsauth.utils;

import org.bukkit.command.CommandSender;

public final class MessageUtils {

    public static void message(CommandSender sender, String message) {
        if (message == null)
            message = "null";
        sender.sendMessage(StringUtils.color(message));
    }

    private MessageUtils() {}

}
