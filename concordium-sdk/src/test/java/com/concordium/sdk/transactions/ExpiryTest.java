package com.concordium.sdk.transactions;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Test;

import com.concordium.sdk.types.Timestamp;

public class ExpiryTest {

    @Test
    public void testExpiryAddMinutes() {
        Expiry expiry = Expiry.from(1);
        long secondsIn5Minutes = 300;

        Expiry result = expiry.addMinutes(5);

        assertEquals(expiry.getValue().getValue() + secondsIn5Minutes, result.getValue().getValue());
    }

    @Test
    public void testExpiryAddSeconds() {
        Expiry expiry = Expiry.from(1);
        int secondsToAdd = 1664115;

        Expiry result = expiry.addSeconds(secondsToAdd);

        assertEquals(expiry.getValue().getValue() + secondsToAdd, result.getValue().getValue());
    }

    @Test
    public void testExpiryFromSeconds() {
        long value = 1513;
        Expiry expiry = Expiry.from(value);

        assertEquals(value, expiry.getValue().getValue());
    }

    @Test
    public void testExpiryFromDate() {
        long valueInSeconds = 15605;
        Date date = new Date(valueInSeconds * 1000);

        Expiry expiry = Expiry.from(date);

        assertEquals(valueInSeconds, expiry.getValue().getValue());
    }

    @Test
    public void testExpiryFromTimestamp() {
        long valueInSeconds = 51356;
        Timestamp timestamp = Timestamp.newSeconds(valueInSeconds);

        Expiry expiry = Expiry.from(timestamp);

        assertEquals(valueInSeconds, expiry.getValue().getValue());
    }
}
