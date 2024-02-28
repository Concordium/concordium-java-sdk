package com.concordium.sdk.crypto.wallet.web3Id;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import static com.concordium.sdk.crypto.wallet.web3Id.CredentialAttribute.CredentialAttributeType.INT;

public class CredentialAttributeTest {
    @Test
    public void testIsBetweenForNumberAttributes() {
        CredentialAttribute low = new CredentialAttribute("9", INT);
        CredentialAttribute middle = new CredentialAttribute("10", INT);
        CredentialAttribute high = new CredentialAttribute("11", INT);
        assertTrue(!low.isBetween(middle, high));
        assertTrue(middle.isBetween(low, high));
        assertTrue(!high.isBetween(low, middle));
    }

    @Test
    public void testIsBetweenHandles64BitNumbers() {
        CredentialAttribute low = new CredentialAttribute("1", INT);
        CredentialAttribute middle = new CredentialAttribute("18446744073709551600", INT);
        CredentialAttribute high = new CredentialAttribute("18446744073709551615", INT);
        assertFalse(low.isBetween(middle, high));
        assertTrue(middle.isBetween(low, high));
        assertTrue(!high.isBetween(low, middle));
    }
}
