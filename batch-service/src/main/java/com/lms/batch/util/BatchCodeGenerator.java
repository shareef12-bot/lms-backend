package com.lms.batch.util;

import java.util.UUID;

public class BatchCodeGenerator {

    public static String generate() {
        return "BATCH-" + UUID.randomUUID().toString().substring(0, 5).toUpperCase();
    }
}
