package pl.skript.ipsauth.utils;

public final class TimeUtils {

    public static String milisToFormatedSeconds(long milis) {
        int time = Math.round(milis / 1000);
        String format;
        switch (time) {
            case 0:
            case 1:
                format = "sekunde";
                break;
            case 2:
            case 3:
            case 4:
                format = time + " sekundy";
                break;
            default:
                format = time + " sekund";
                break;
        }
        return format;
    }

    private TimeUtils() {}

}
