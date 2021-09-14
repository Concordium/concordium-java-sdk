package com.concordium.sdk.responsetypes;

import com.concordium.sdk.Base58;
import com.concordium.sdk.transactions.AccountAddress;
import lombok.val;
import org.junit.Test;

import static org.junit.Assert.*;

public class AccountAddressTest {

    @Test
    public void testAccountAddressFromString() {
        val address = "3XSLuJcXg6xEua6iBPnWacc3iWh93yEDMCqX8FbE3RDSbEnT9P";
        val accountAddress = AccountAddress.from(address);
        assertEquals(address, accountAddress.encoded());
    }

    @Test
    public void testCannotCreatAccountAddressFromTooShortAddress() {
        try {
            val tooShortAddress = Base58.encodeChecked(1, new byte[31]);
            AccountAddress.from(tooShortAddress);
            fail("Expected account address to be invalid.");
        } catch (RuntimeException e) {
            if (!e.getMessage().contains("Address bytes must be exactly 32 bytes long.")) {
                fail("Unexpected error: " + e.getMessage());
            }
        }
    }

        @Test
        public void testCannotCreatAccountAddressFromTooLongAddress() {
            try {
                val tooLongAddress = Base58.encodeChecked(1, new byte[33]);
                AccountAddress.from(tooLongAddress);
                fail("Expected account address to be invalid.");
            } catch (RuntimeException e) {
                if (!e.getMessage().contains("Address bytes must be exactly 32 bytes long.")) {
                    fail("Unexpected error: " + e.getMessage());
                }
            }
        }
}
