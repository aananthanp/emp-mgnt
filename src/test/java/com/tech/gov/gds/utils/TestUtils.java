package com.tech.gov.gds.utils;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import lombok.extern.apachecommons.CommonsLog;
import org.slf4j.LoggerFactory;

import java.time.*;

@CommonsLog
public class TestUtils {


    public static void runAtFixedTime(LocalDateTime time, Runnable func) {
        try {
            Clock clock = Clock.fixed(Instant.ofEpochMilli(time.toEpochSecond(ZoneOffset.UTC)), ZoneId.of("UTC"));
            LocalDateTime.now(clock);
            func.run();
        } finally {
            LocalDateTime.now(Clock.systemDefaultZone());
        }
    }

    public static void setDebugLog(Class c) {
        final Logger logger = (Logger) LoggerFactory.getLogger(c);
        logger.setLevel(Level.DEBUG);
    }

    public static void runAtFixedTime(String time, Runnable func) {
        runAtFixedTime(LocalDateTime.parse(time), func);
    }

}

