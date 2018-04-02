package pl.skript.ipsauth;

import org.apache.logging.log4j.LogManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import pl.ixidi.ipsconnect.IpsConnect;
import pl.skript.ipsauth.base.IpsUser;
import pl.skript.ipsauth.base.IpsUserManager;
import pl.skript.ipsauth.command.IpsAuthCommand;
import pl.skript.ipsauth.command.LoginCommand;
import pl.skript.ipsauth.listener.BlockedListeners;
import pl.skript.ipsauth.listener.PlayerJoinListener;
import pl.skript.ipsauth.listener.PlayerQuitListener;
import pl.skript.ipsauth.task.LoginMessageTask;
import pl.skript.ipsauth.utils.LoggerFilter;

import java.util.Arrays;
import java.util.logging.Logger;

public final class IpsAuthPlugin extends JavaPlugin {

    private IpsUserManager userManager;
    private IpsConnect connect;

    @Override
    public void onLoad() {
        if (!getDataFolder().exists())
            getDataFolder().mkdirs();
        getConfig().addDefault("ipsConnect.url", "https://example.com/ipsConnect.php");
        getConfig().addDefault("ipsConnect.key", "37126gdas87ddasb8das8");
        getConfig().addDefault("messages.join", "&3Zaloguj sie uzywajac swojego konta na forum.\n&3Uzyj komendy &7/login <nazwa uzytkownika/email> <haslo>&3.");
        getConfig().addDefault("messages.login", "&3Zaloguj sie uzywajac swojego konta na forum.\n&3Uzyj komendy &7/login <nazwa uzytkownika/email> <haslo>&3.");
        getConfig().addDefault("messages.notAssigned", "&cDo tego konta przypisane jest inne konto z forum. (&7{IPS-NAME}&c)");
        getConfig().addDefault("messages.success", "&aZalogowano pomyslnie, witaj &7{IPS-NAME}&a!");
        getConfig().addDefault("messages.command-delay", "&cMozesz sprobowac ponownie za &7{TIME}&c!");
        getConfig().addDefault("login-message-time", 5);
        getConfig().addDefault("login-message-enabled", true);
        getConfig().addDefault("command-delay-time", 10);
        getConfig().addDefault("command-delay-enabled", true);
        getConfig().options().copyDefaults(true);
        saveConfig();
        connect = new IpsConnect(getConfig().getString("ipsConnect.url"), getConfig().getString("ipsConnect.key"));
        userManager = new IpsUserManager(this);
        Bukkit.getOnlinePlayers().forEach(player -> userManager.add(new IpsUser(player.getUniqueId())));
    }

    @Override
    public void onEnable() {
        ((org.apache.logging.log4j.core.Logger) LogManager.getRootLogger()).addFilter(new LoggerFilter());
        listeners(
            new PlayerJoinListener(this),
            new PlayerQuitListener(this),
            new BlockedListeners(this)
        );
        command("login", new LoginCommand(this));
        command("ipsauth", new IpsAuthCommand(this));
        Bukkit.getOnlinePlayers().forEach(player ->
            userManager.add(userManager.loadUser(player.getUniqueId(), true))
        );
        if (getConfig().getBoolean("login-message-enabled"))
            Bukkit.getScheduler().runTaskTimer(
                    this, new LoginMessageTask(this), 0, getConfig().getInt("login-message-time") * 20
            );
    }

    @Override
    public void onDisable() {
        userManager.getUserMap().values().forEach(user -> userManager.saveUser(user, true));
    }

    private void listeners(Listener... listeners) {
        Arrays.stream(listeners).forEach(listener -> getServer().getPluginManager().registerEvents(listener, this));
    }

    private void command(String name, CommandExecutor executor) {
        getCommand(name).setExecutor(executor);
    }

    public IpsUserManager getUserManager() {
        return userManager;
    }

    public IpsConnect getConnect() {
        return connect;
    }
}
