package pl.skript.ipsauth.listener;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import pl.skript.ipsauth.IpsAuthPlugin;
import pl.skript.ipsauth.base.IpsUser;

public class BlockedListeners implements Listener {

    private IpsAuthPlugin plugin;

    public BlockedListeners(IpsAuthPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        IpsUser user = plugin.getUserManager().get(event.getPlayer().getUniqueId());
        if (user.isLogged())
            return;
        Location to = event.getTo();
        Location from = event.getFrom();
        if (to.getX() != from.getX() || to.getY() != from.getY() || to.getZ() != from.getZ())
            event.setCancelled(true);
    }


    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        IpsUser user = plugin.getUserManager().get(event.getPlayer().getUniqueId());
        if (!user.isLogged())
            event.setCancelled(true);
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        IpsUser user = plugin.getUserManager().get(event.getPlayer().getUniqueId());
        if (!user.isLogged())
            event.setCancelled(true);
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        IpsUser user = plugin.getUserManager().get(event.getPlayer().getUniqueId());
        if (!user.isLogged())
            event.setCancelled(true);
    }

    @EventHandler
    public void onCommandPreprocess(PlayerCommandPreprocessEvent event) {
        IpsUser user = plugin.getUserManager().get(event.getPlayer().getUniqueId());
        if (user.isLogged())
            return;
        if (!event.getMessage().startsWith("/login") || !event.getMessage().startsWith("/l"))
            event.setCancelled(true);
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        IpsUser user = plugin.getUserManager().get(event.getPlayer().getUniqueId());
        if (user.isLogged())
            return;
        if (!event.getMessage().startsWith("/login") || !event.getMessage().startsWith("/l"))
            event.setCancelled(true);
    }

    @EventHandler
    public void onInvetoryClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null)
            return;
        IpsUser user = plugin.getUserManager().get(event.getWhoClicked().getUniqueId());
        if (!user.isLogged()) {
            event.setCancelled(true);
            event.getWhoClicked().closeInventory();
        }
    }
}
