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
    public void testCanTransferSchedulWithMemoFactory() {
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

    @Test
    public void testCanRemoveBakerFactory() {
        val builder = TransactionFactory.newRemoveBaker();
        assertNotNull(builder);
    }

    @Test
    public void testCanConfigureBakerFactory() {
        val builder = TransactionFactory.newConfigureBaker();
        assertNotNull(builder);
    }

    @Test
    public void testCanUpdateBakerStakeFactory() {
        val stake = CCDAmount.fromMicro("10000000");
        val builder = TransactionFactory.newUpdateBakerStake(stake);
        assertNotNull(builder);
    }

    @Test
    public void testCanUpdateBakerRestakeEarningsFactory() {
        val builder = TransactionFactory.newUpdateBakerRestakeEarnings(true);
        assertNotNull(builder);
    }

    @Test
    public void testCanUpdateBakerKeysFactory() {
        AccountAddress sender = AccountAddress.from("3JwD2Wm3nMbsowCwb1iGEpnt47UQgdrtnq2qT6opJc3z2AgCrc");
        val builder = TransactionFactory.newUpdateBakerKeys(sender);
        assertNotNull(builder);
    }

    @Test
    public void testConfigureDelegationFactory() {
        val builder = TransactionFactory.newConfigureDelegation();
        assertNotNull(builder);
    }
}
