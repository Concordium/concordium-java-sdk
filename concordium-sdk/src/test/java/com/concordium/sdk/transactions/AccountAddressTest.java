package com.concordium.sdk.transactions;

import com.concordium.sdk.types.AccountAddress;
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

    @Test
    public void testIsAliasOf() {
        val base = AccountAddress.from("2wkH4kHMn2WPndf8CxmsoFkX93ouZMJUwTBFSZpDCeNeGWa7dj");
        assertTrue(base.isAliasOf(base));
        assertTrue(base.newAlias(1).isAliasOf(base));
        assertTrue(base.newAlias(197121).isAliasOf(base));
        assertFalse(AccountAddress.from("3XSLuJcXg6xEua6iBPnWacc3iWh93yEDMCqX8FbE3RDSbEnT9P").isAliasOf(base));
    }

    @Test
    public void testCreateAccountAlias() {
        val base = AccountAddress.from("2wkH4kHMn2WPndf8CxmsoFkX93ouZMJUwTBFSZpDCeNeGWa7dj");
        assertEquals("2wkH4kHMn2WPndf8CxmsoFkX93ouZMJUwTBFSZpDBez9cfL8oC", base.newAlias(1 + 2 * 256 + 3 * 65536).encoded());
        assertEquals(base.newAlias(0).encoded(), base.newAlias(16777216).encoded());

        try {
            base.newAlias((2 << 23) + 1);
            fail("Expected invalid alias.");
        } catch (IllegalArgumentException e) {
            if (!e.getMessage().contains("Alias too large, the provided alias must not be larger than 2^24.")) {
                fail("Unexpected error: " + e.getMessage());
            }
        }
    }

    @Test
    public void testCreateAccountAddressFromCredentialRegistrationId() {
        val regId = CredentialRegistrationId.from("8e5c75fda3f791efd025e7e8fb0c26c3e111211e375fc9423b1ca308a696140e44c90dcabe75108b41db625152acae50");
        val address = AccountAddress.from(regId);
        assertEquals("4LYFWsgZvL6bXC5edMgy7SpPg7BV7qK2TcPQRq3YZP9YgShrSQ", address.encoded());
    }
}
