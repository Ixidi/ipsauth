package pl.skript.ipsauth.base;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import pl.ixidi.ipsconnect.responce.body.LoginBody;
import pl.skript.ipsauth.exceptions.LoginStateException;
import pl.skript.ipsauth.exceptions.NotAssignedAccountException;
import pl.skript.ipsauth.exceptions.OfflinePlayerException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class IpsUser {

    private UUID uuid;
    private Integer ipsId;
    private String ipsName;
    private String ipsEmail;
    private List<LoginRecord> loginRecordList;
    private LoginRecord currentRecord;
    private boolean logged;

    private boolean loginInProgress;

    public IpsUser(UUID uuid, Integer ipsId, String ipsName, String ipsEmail, List<LoginRecord> loginRecordList) {
        this.uuid = uuid;
        this.ipsId = ipsId;
        this.ipsName = ipsName;
        this.ipsEmail = ipsEmail;
        this.loginRecordList = loginRecordList;
        this.logged = false;
        this.loginInProgress = false;
    }

    public IpsUser(UUID uuid) {
        this(uuid, null, null, null, new ArrayList<>());
    }

    public void login(LoginBody body) throws LoginStateException, OfflinePlayerException, NotAssignedAccountException {
        loginInProgress = false;
        if (logged)
            throw new LoginStateException("User is already logged!");
        Player player = getPlayer();
        if (player == null)
            throw new OfflinePlayerException("Offline player cannot be logged!");
        if (ipsId != null) {
            if (ipsId != body.getId())
                throw new NotAssignedAccountException("Another ips-account is assigned to this minecraft-account!", ipsId);
        } else {
            ipsId = body.getId();
        }
        ipsName = body.getName();
        ipsEmail = body.getEmail();
        currentRecord = new LoginRecord(player.getAddress().getHostName());
        loginRecordList.add(currentRecord);
        getPlayer().setDisplayName(ipsName);
        getPlayer().setPlayerListName(ipsName);
        logged = true;
    }

    public void logout() throws LoginStateException {
        if (!logged)
            throw new LoginStateException("User is not logged!");
        logged = false;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    public UUID getUuid() {
        return uuid;
    }

    public boolean isLogged() {
        return logged;
    }

    public List<LoginRecord> getLoginRecordList() {
        return new ArrayList<>(loginRecordList);
    }

    public LoginRecord getCurrentRecord() {
        return currentRecord;
    }

    public boolean isLoginInProgress() {
        return loginInProgress;
    }

    public void setLoginInProgress(boolean loginInProgress) {
        this.loginInProgress = loginInProgress;
    }

    public Integer getIpsId() {
        return ipsId;
    }

    public String getIpsName() {
        return ipsName;
    }

    public String getIpsEmail() {
        return ipsEmail;
    }

    public void setIpsId(Integer ipsId) {
        this.ipsId = ipsId;
    }

    public void setIpsName(String ipsName) {
        this.ipsName = ipsName;
    }

    public void setIpsEmail(String ipsEmail) {
        this.ipsEmail = ipsEmail;
    }
}
