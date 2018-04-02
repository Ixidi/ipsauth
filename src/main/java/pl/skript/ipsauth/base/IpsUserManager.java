package pl.skript.ipsauth.base;

import pl.skript.ipsauth.IpsAuthPlugin;
import pl.skript.ipsauth.utils.LoginRecordUtils;
import pl.skript.ipsauth.utils.YamlFile;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public final class IpsUserManager {

    private final IpsAuthPlugin plugin;
    private final Map<UUID, IpsUser> userMap = new ConcurrentHashMap<>();

    public IpsUserManager(IpsAuthPlugin plugin) {
        this.plugin = plugin;
    }

    public void add(IpsUser user) {
        userMap.put(user.getUuid(), user);
    }

    public IpsUser get(UUID uuid) {
        return userMap.get(uuid);
    }

    public void remove(UUID uuid) {
        userMap.remove(uuid);
    }

    public Map<UUID, IpsUser> getUserMap() {
        return new HashMap<>(userMap);
    }

    public IpsUser loadUser(UUID uuid, boolean createIfNotExists) {
        File file = getFile(uuid);
        if (file.exists()) {
            YamlFile yaml = new YamlFile(file);
            List<String> yamlList = yaml.getStringList("records");
            int id = yaml.getInt("ips.id");
            String name = yaml.getString("ips.name");
            String email = yaml.getString("ips.email");

            List<LoginRecord> loginRecords = new ArrayList<>();
            if (yamlList != null)
                loginRecords = LoginRecordUtils.toRecordList(yamlList);

            if (id == 0) {
                return new IpsUser(uuid, null, name, email, loginRecords);
            }

            return new IpsUser(uuid, id, name, email, loginRecords);
        } else {
            if (!createIfNotExists)
                return null;
            return new IpsUser(uuid);
        }
    }

    public void saveUser(IpsUser user, boolean removeFromMap) {
        File file = getFile(user.getUuid());
        YamlFile yaml = new YamlFile(file);
        yaml.set("ips.id", user.getIpsId());
        yaml.set("ips.name", user.getIpsName());
        yaml.set("ips.email", user.getIpsEmail());

        List<LoginRecord> userRecords = user.getLoginRecordList();
        if (userRecords.size() > 0)
            yaml.set("records", LoginRecordUtils.fromRecordList(userRecords));
        yaml.save();
        if (removeFromMap)
            userMap.remove(user.getUuid());
    }

    private File getFile(UUID uuid) {
        File folder = new File(plugin.getDataFolder(), "users");
        return new File(folder, uuid.toString() + ".yml");
    }
}
