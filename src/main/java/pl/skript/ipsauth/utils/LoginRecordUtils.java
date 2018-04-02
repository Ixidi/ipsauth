package pl.skript.ipsauth.utils;

import pl.skript.ipsauth.base.LoginRecord;

import java.util.ArrayList;
import java.util.List;

public final class LoginRecordUtils {

    private LoginRecordUtils() {}

    public static List<LoginRecord> toRecordList(List<String> stringList) {
        List<LoginRecord> recordList = new ArrayList<>();
        stringList.forEach((string) -> {
            try {
                String[] splited = string.split(";");
                String ip = splited[0];
                Long time = Long.parseLong(splited[1]);
                recordList.add(new LoginRecord(ip, time));
            } catch (Exception ignored) {}
        });
        return recordList;
    }

    public static List<String> fromRecordList(List<LoginRecord> recordList) {
        List<String> stringList = new ArrayList<>();
        recordList.forEach((record) -> stringList.add(record.getIp() + ";" + record.getTime()));
        return stringList;
    }

}
