package com.concordium.sdk.types;

import lombok.val;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;

public class TimestampTest {

    @Test
    public void testCreateTimestamps() {
        val now = System.currentTimeMillis();
        assertEquals(new Date(now), Timestamp.newMillis(now).getDate());
    }
}
