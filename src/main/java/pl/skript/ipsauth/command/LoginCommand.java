package pl.skript.ipsauth.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.ixidi.ipsconnect.IdType;
import pl.ixidi.ipsconnect.request.impl.FetchSaltRequest;
import pl.ixidi.ipsconnect.request.impl.LoginRequest;
import pl.ixidi.ipsconnect.responce.Response;
import pl.ixidi.ipsconnect.responce.body.FetchSaltBody;
import pl.ixidi.ipsconnect.responce.body.LoginBody;
import pl.skript.ipsauth.IpsAuthPlugin;
import pl.skript.ipsauth.base.IpsUser;
import pl.skript.ipsauth.exceptions.LoginStateException;
import pl.skript.ipsauth.exceptions.NotAssignedAccountException;
import pl.skript.ipsauth.exceptions.OfflinePlayerException;
import pl.skript.ipsauth.utils.TimeUtils;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static pl.skript.ipsauth.utils.MessageUtils.message;

public class LoginCommand implements CommandExecutor {

    private IpsAuthPlugin plugin;

    private boolean delayEnabled;
    private Map<UUID, Long> lastUseMap;
    private long delay;

    public LoginCommand(IpsAuthPlugin plugin) {
        this.plugin = plugin;
        delayEnabled = plugin.getConfig().getBoolean("command-delay-enabled");
        if (delayEnabled) {
            delay = plugin.getConfig().getInt("command-delay-time") * 1000;
            lastUseMap = new ConcurrentHashMap<>();
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            message(sender, "&cKomenda tylko dla graczy.");
            return true;
        }
        Player player = (Player) sender;
        UUID uuid = player.getUniqueId();
        IpsUser user = plugin.getUserManager().get(uuid);
        if (user.isLogged()) {
            message(player, "&cJestes juz zalogowany.");
            return true;
        }
        if (args.length < 2) {
            message(player, "&cPoprawne uzycie: &7/login <nazwa uzytkownika> <haslo>&c.");
            return true;
        }
        if (delayEnabled) {
            long now = System.currentTimeMillis();
            if (lastUseMap.containsKey(uuid)) {
                long time = now - lastUseMap.get(uuid);
                if (time >= delay) {
                    lastUseMap.put(uuid, now);
                } else {
                    message(player, plugin.getConfig().getString("messages.command-delay")
                            .replace("{TIME}", TimeUtils.milisToFormatedSeconds(delay - time))
                    );
                    return true;
                }
            } else {
                lastUseMap.put(uuid, now);
            }
        }
        if (user.isLoginInProgress())
            return true;
        user.setLoginInProgress(true);
        message(player, "&eLogowanie...");

        String login = args[0];
        String password = args[1];
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            Response saltResponse = plugin.getConnect().request(new FetchSaltRequest(IdType.BOTH, login));
            String salt;
            switch (saltResponse.getStatus()) {
                case SUCCESS:
                     salt = ((FetchSaltBody) saltResponse.getBody()).getSalt();
                     break;
                case ACCOUNT_NOT_FOUND:
                    message(sender, "&cUzytkownik o podanej nazwie nie istnieje!");
                    user.setLoginInProgress(false);
                    return;
                default:
                    message(sender, "&cWystapil problem! (" + saltResponse.getStatus() + ")");
                    user.setLoginInProgress(false);
                    return;
            }

            Response loginResponse = plugin.getConnect().request(new LoginRequest(IdType.BOTH, login, password, salt));
            switch (loginResponse.getStatus()) {
                case SUCCESS:
                    try {
                        user.login((LoginBody) loginResponse.getBody());
                    } catch (LoginStateException | OfflinePlayerException e) {
                        e.printStackTrace();
                        break;
                    } catch (NotAssignedAccountException e) {
                        String message = plugin.getConfig().getString("messages.notAssigned");
                        message = message.replace("{IPS-NAME}", user.getIpsName());
                        message(player, message);
                        break;
                    }
                    message(player, plugin.getConfig().getString("messages.success").replace("{IPS-NAME}", user.getIpsName()));
                    break;
                case WRONG_AUTH:
                    message(sender, "&cPodano haslo jest bledne!");
                    user.setLoginInProgress(false);
                    break;
            }
        });
        return true;
    }

}
