package pl.skript.ipsauth.base;

public final class LoginRecord {

    private final String ip;
    private final long time;

    public LoginRecord(String ip, long time) {
        this.ip = ip;
        this.time = time;
    }

    public LoginRecord(String ip) {
        this(ip, System.currentTimeMillis());
    }

    public String getIp() {
        return ip;
    }

    public long getTime() {
        return time;
    }

}
