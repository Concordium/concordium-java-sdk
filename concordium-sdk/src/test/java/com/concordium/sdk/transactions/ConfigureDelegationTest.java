package com.concordium.sdk.transactions;

import com.concordium.sdk.responses.BakerId;
import com.concordium.sdk.responses.transactionstatus.DelegationTarget;
import com.concordium.sdk.types.AccountAddress;
import com.concordium.sdk.types.Nonce;
import lombok.SneakyThrows;
import lombok.val;
import org.apache.commons.codec.binary.Hex;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ConfigureDelegationTest {

    @SneakyThrows
    @Test
    public void shouldConfigureDelegationTransaction() {
        val accountAddress = AccountAddress.from("48x2Uo8xCMMxwGuSQnwbqjzKtVqK5MaUud4vG7QEUgDmYkV85e");

        val payload = ConfigureDelegation.builder()
                .capital(CCDAmount.fromMicro("500000"))
                .restakeEarnings(true)
                .delegationTarget(DelegationTarget.newPassiveDelegationTarget())
                .build();

        val transaction = TransactionFactory
                .newConfigureDelegation(payload)
                .sender(accountAddress)
                .nonce(Nonce.from(123))
                .expiry(Expiry.from(413223))
                .sign(TransactionTestHelper.getValidSigner());

        assertEquals(212, transaction.getVersionedBytes().length);
    }


    @Test
    public void serializeConfigurePassivePoolNoRestake() {
        val payload = ConfigureDelegation
                .builder()
                .delegationTarget(DelegationTarget.builder().type(DelegationTarget.DelegationType.PASSIVE).build())
                .capital(CCDAmount.from(100))
                .restakeEarnings(false)
                .build();
        assertEquals("1a00070000000005f5e1000000", Hex.encodeHexString(payload.getBytes()));
    }

    @Test
    public void serializeConfigurePassivePoolRestake() {
        val payload = ConfigureDelegation
                .builder()
                .delegationTarget(DelegationTarget.builder().type(DelegationTarget.DelegationType.PASSIVE).build())
                .capital(CCDAmount.from(100))
                .restakeEarnings(true)
                .build();
        assertEquals("1a00070000000005f5e1000100", Hex.encodeHexString(payload.getBytes()));
    }

    @Test
    public void serializeConfigureBakerPoolNoRestake() {
        val payload = ConfigureDelegation
                .builder()
                .delegationTarget(DelegationTarget.builder().type(DelegationTarget.DelegationType.BAKER).bakerId(BakerId.from(1234)).build())
                .restakeEarnings(false)
                .capital(CCDAmount.from(100))
                .build();
        assertEquals("1a00070000000005f5e100000100000000000004d2", Hex.encodeHexString(payload.getBytes()));
    }

    @Test
    public void serializeConfigureBakerPoolRestake() {
        val payload = ConfigureDelegation
                .builder()
                .delegationTarget(DelegationTarget.builder().type(DelegationTarget.DelegationType.BAKER).bakerId(BakerId.from(1234)).build())
                .capital(CCDAmount.from(100))
                .restakeEarnings(true)
                .build();
        assertEquals("1a00070000000005f5e100010100000000000004d2", Hex.encodeHexString(payload.getBytes()));
    }

    @Test
    public void serializeDeregisterPayload() {
        val payload = ConfigureDelegation
                .builder()
                .capital(CCDAmount.from(0))
                .build();
        assertEquals("1a00010000000000000000", Hex.encodeHexString(payload.getBytes()));
    }

    @Test
    public void serializeStopRestake() {
        val payload = ConfigureDelegation
                .builder()
                .restakeEarnings(false)
                .build();
        assertEquals("1a000200", Hex.encodeHexString(payload.getBytes()));
    }

    @Test
    public void serializeEnableRestake() {
        val payload = ConfigureDelegation
                .builder()
                .restakeEarnings(true)
                .build();
        assertEquals("1a000201", Hex.encodeHexString(payload.getBytes()));
    }

}
