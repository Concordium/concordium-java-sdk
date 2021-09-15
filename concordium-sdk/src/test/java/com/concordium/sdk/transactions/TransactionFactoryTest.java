package com.concordium.sdk.transactions;

import lombok.val;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class TransactionFactoryTest {

    @Test
    public void testCanCreateSimpleTransferFactory() {
        val builder = TransactionFactory.newSimpleTransfer();
        assertNotNull(builder);
    }
}
