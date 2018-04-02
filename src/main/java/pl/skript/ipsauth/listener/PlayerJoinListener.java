package pl.skript.ipsauth.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import pl.skript.ipsauth.IpsAuthPlugin;
import pl.skript.ipsauth.base.IpsUser;
import pl.skript.ipsauth.utils.StringUtils;

public class PlayerJoinListener implements Listener {

    private IpsAuthPlugin plugin;

    public PlayerJoinListener(IpsAuthPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.sendMessage(StringUtils.color(plugin.getConfig().getString("messages.join")));
        IpsUser user = plugin.getUserManager().loadUser(player.getUniqueId(), true);
        plugin.getUserManager().add(user);
    }

}
