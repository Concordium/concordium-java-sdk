package com.concordium.sdk.transactions;

import lombok.val;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class TransactionFactoryTest {

    @Test
    public void testCanCreateSimpleTransferFactory() {
        val builder = TransactionFactory.newTransfer();
        assertNotNull(builder);
    }

    @Test
    public void testCanCreateTransferWithMemoFactory() {
        val builder = TransactionFactory.newTransferWithMemo();
        assertNotNull(builder);
    }

    @Test
    public void testCanCreateRegisterDataFactory() {
        val builder = TransactionFactory.newRegisterData();
        assertNotNull(builder);
    }

    @Test
    public void testCanRemoveBakerFactory() {
        val builder = TransactionFactory.newRemoveBaker();
        assertNotNull(builder);
    }
}
