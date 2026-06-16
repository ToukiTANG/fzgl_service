package com.ruoyi.touki.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Touki
 */
public abstract class IdGenerateUtil {
    private static final AtomicInteger SEQ = new AtomicInteger(0);

    public static synchronized Long nextId() {

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmmss"));

        int seq = SEQ.incrementAndGet();

        if (seq > 9999) {
            SEQ.set(1);
            seq = 1;
        }

        return Long.parseLong(timestamp + String.format("%04d", seq));
    }
}
