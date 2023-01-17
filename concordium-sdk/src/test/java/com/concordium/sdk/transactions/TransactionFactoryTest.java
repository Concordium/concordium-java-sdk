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
    public void testCanInitContractFactory() {
        val builder = TransactionFactory.newInitContract();
        assertNotNull(builder);
    }


    @Test
    public void testCanCreateUpdatePayloadFactory() {
        val builder = TransactionFactory.newUpdateContract();
        assertNotNull(builder);
    }

    @Test
    public  void testCanDeployModuleFactory() {
        val builder = TransactionFactory.newDeployModule();
        assertNotNull(builder);
    }

    @Test
    public  void testCanTransferScheduleFactory() {
        val builder = TransactionFactory.newScheduledTransfer();
        assertNotNull(builder);
    }

    @Test
    public  void testCanTransferSchedulWithMemoFactory() {
        val builder = TransactionFactory.newScheduledTransferWithMemo();
        assertNotNull(builder);
    }

    @Test
    public void testCanCreateUpdateCredentialKeysFactory() {
        val builder = TransactionFactory.newUpdateCredentialKeys();
        assertNotNull(builder);
    }

    @Test
    public void testCanTransferToEncryptedFactory() {
        val builder = TransactionFactory.newTransferToEncrypted();
        assertNotNull(builder);
    }
}
