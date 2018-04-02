package pl.skript.ipsauth.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.skript.ipsauth.IpsAuthPlugin;
import pl.skript.ipsauth.base.IpsUser;

import java.util.UUID;

import static pl.skript.ipsauth.utils.MessageUtils.message;

public class IpsAuthCommand implements CommandExecutor {

    private IpsAuthPlugin plugin;

    public IpsAuthCommand(IpsAuthPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (commandSender instanceof Player && !commandSender.hasPermission("ipsauth.admin")) {
            message(commandSender, "&cNie masz uprawnien!");
            return true;
        }
        if (args.length < 1) {
            message(commandSender, "&3&lIpsAuth &3by Ixidi" +
                    "\n&b/ipsauth reset <gracz/uuid> - Gracz moze przypisac inne konto z forum.");
            return true;
        }
        switch (args[0].toLowerCase()) {
            case "reset":
                if (args.length < 2) {
                    message(commandSender, "&cPoprawne uzycie: &7/ipsauth reset <gracz/uuid>&c.");
                    break;
                }
                String key = args[1];
                IpsUser targetUser;
                if (key.matches("/^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$/")) {
                    UUID uuid = UUID.fromString(key);
                    targetUser = plugin.getUserManager().get(uuid);
                    if (targetUser == null)
                        targetUser = plugin.getUserManager().loadUser(uuid, false);
                    if (targetUser == null) {
                        message(commandSender, "&cGracza o uuid &7" + key + " &cnie przypisal konta!");
                        break;
                    }
                } else {
                    Player target = Bukkit.getPlayer(key);
                    if (target == null) {
                        message(commandSender, "&cGracza &7" + key + " &cnie ma na serwerze!");
                        break;
                    }
                    targetUser = plugin.getUserManager().get(target.getUniqueId());
                }
                targetUser.setIpsEmail(null);
                targetUser.setIpsId(null);
                targetUser.setIpsName(null);
                plugin.getUserManager().saveUser(targetUser, false);
                message(commandSender, "&aGracz &7" + key + " &amoze przypisac nowe konto!");
                break;
            default:
                message(commandSender, "&cNieznana komenda, wpisz &7/ipsauth&c.");
                break;
        }
        return true;
    }

}
