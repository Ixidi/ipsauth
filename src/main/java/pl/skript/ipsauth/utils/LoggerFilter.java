package pl.skript.ipsauth.utils;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.filter.AbstractFilter;
import org.apache.logging.log4j.message.Message;

import java.util.Arrays;

public class LoggerFilter extends AbstractFilter {

    private static final String[] BLOCKED = {
            "/login",
            "/l"
    };

    public Result validate(String text) {
        String message = text.toLowerCase();
        return Arrays.stream(BLOCKED)
                .anyMatch(word -> message.contains("issued server command") && message.contains(word))
        ? Result.DENY : Result.NEUTRAL;
    }

    @Override
    public Result filter(LogEvent event) {
        return validate(event.getMessage().getFormattedMessage());
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, Message msg, Throwable t) {
        return validate(msg.getFormattedMessage());
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, Object msg, Throwable t) {
        return validate(msg.toString());
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, String msg, Object... params) {
        return validate(msg);
    }
}
