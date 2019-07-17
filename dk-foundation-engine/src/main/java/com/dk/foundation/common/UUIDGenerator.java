package com.dk.foundation.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

/**
 * The type Uuid generator.
 */
public class UUIDGenerator {

    private static final AtomicLong UUID = new AtomicLong(1000);
    private static int serverNodeId = 1;
    private static final long UUID_INTERNAL = 2000000000;

    /**
     * Generate uuid long.
     *
     * @return the long
     */
    public static long generateUUID() {
        long id = UUID.incrementAndGet();
        if (id >= UUID_INTERNAL * (serverNodeId + 1)) {
            synchronized (UUID) {
                if (UUID.get() >= id) {
                    id -= UUID_INTERNAL;
                    UUID.set(id);
                }
            }
        }
        return id;
    }

    /**
     * Gets current uuid.
     *
     * @return the current uuid
     */
    public static long getCurrentUUID() {
        return UUID.get();
    }

    /**
     * Sets uuid.
     *
     * @param expect the expect
     * @param update the update
     * @return the uuid
     */
    public static boolean setUUID(long expect, long update) {
        return UUID.compareAndSet(expect, update);

    }

    /**
     * Init.
     *
     * @param serverNodeId the server node id
     */
    public static void init(int serverNodeId) {
        try {
            UUIDGenerator.serverNodeId = serverNodeId;
            UUID.set(UUID_INTERNAL * serverNodeId);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Calendar cal = Calendar.getInstance();
            Date date = format.parse("2019-01-01");
            cal.setTime(date);
            long base = cal.getTimeInMillis();
            long current = System.currentTimeMillis();
            UUID.addAndGet((current - base) / 1000);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
