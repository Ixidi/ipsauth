package pl.skript.ipsauth.task;

import org.bukkit.entity.Player;
import pl.skript.ipsauth.IpsAuthPlugin;
import pl.skript.ipsauth.utils.StringUtils;

import java.util.Arrays;

public class LoginMessageTask implements Runnable {

    private IpsAuthPlugin plugin;

    public LoginMessageTask(IpsAuthPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        plugin.getUserManager().getUserMap().values().stream()
                .filter(user -> !user.isLogged() && !user.isLoginInProgress())
                .forEach(user -> {
                    Player player = user.getPlayer();
                    if (player != null)
                        player.sendMessage(StringUtils.color(plugin.getConfig().getString("messages.login")));
                });
    }
}
