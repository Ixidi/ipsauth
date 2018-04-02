package pl.skript.ipsauth.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import pl.skript.ipsauth.IpsAuthPlugin;
import pl.skript.ipsauth.base.IpsUser;

public class PlayerQuitListener implements Listener {

    private IpsAuthPlugin plugin;

    public PlayerQuitListener(IpsAuthPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        IpsUser user = plugin.getUserManager().get(player.getUniqueId());
        plugin.getUserManager().saveUser(user, true);
    }

}
