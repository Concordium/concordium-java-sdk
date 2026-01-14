package com.concordium.sdk.transactions;

import com.concordium.sdk.crypto.bakertransactions.BakerKeys;
import com.concordium.sdk.responses.BakerId;
import com.concordium.sdk.responses.transactionstatus.OpenStatus;
import com.concordium.sdk.responses.transactionstatus.PartsPerHundredThousand;
import com.concordium.sdk.types.AccountAddress;
import com.concordium.sdk.types.Nonce;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.Test;

import java.io.StringWriter;

import static org.junit.Assert.assertEquals;

public class ConfigureBakerTransactionTest {

    @SneakyThrows
    @Test
    public void shouldAddBakerKeysTest() {
        val accountAddress = AccountAddress.from("48x2Uo8xCMMxwGuSQnwbqjzKtVqK5MaUud4vG7QEUgDmYkV85e");

        BakerKeys bakerKeys = BakerKeys.createBakerKeys();
        val payload = ConfigureBaker.builder()
                .capital(CCDAmount.fromMicro("14000000000"))
                .restakeEarnings(true)
                .openForDelegation(OpenStatus.OPEN_FOR_ALL)
                .keysWithProofs(ConfigureBakerKeysPayload.getNewConfigureBakerKeysPayload(accountAddress, bakerKeys))
                .metadataUrl("abc@xyz.com")
                .transactionFeeCommission(PartsPerHundredThousand.from(10000))
                .bakingRewardCommission(PartsPerHundredThousand.from(10000))
                .finalizationRewardCommission(PartsPerHundredThousand.from(100000))
                .build();


        val transaction = TransactionFactory
                .newConfigureBaker(payload)
                .sender(accountAddress)
                .nonce(Nonce.from(123))
                .expiry(Expiry.from(413223))
                .sign(TransactionTestHelper.getValidSigner());

        assertEquals(589, transaction.getVersionedBytes().length);
    }


    @SneakyThrows
    @Test
    public void shouldConfigureEmptyBakerTest() {
        val accountAddress = AccountAddress.from("48x2Uo8xCMMxwGuSQnwbqjzKtVqK5MaUud4vG7QEUgDmYkV85e");

        val payload = ConfigureBaker.builder()
                .build();

        val transaction = TransactionFactory
                .newConfigureBaker(payload)
                .sender(accountAddress)
                .nonce(Nonce.from(123))
                .expiry(Expiry.from(413223))
                .sign(TransactionTestHelper.getValidSigner());

        assertEquals(202, transaction.getVersionedBytes().length);
    }

    @SneakyThrows
    @Test
    public void shouldAddBakerKeysWithoutMetadataUrlTest() {
        val accountAddress = AccountAddress.from("48x2Uo8xCMMxwGuSQnwbqjzKtVqK5MaUud4vG7QEUgDmYkV85e");

        val payload = ConfigureBaker.builder()
                .capital(CCDAmount.fromMicro("14000000000"))
                .restakeEarnings(true)
                .openForDelegation(OpenStatus.OPEN_FOR_ALL)
                .keysWithProofs(ConfigureBakerKeysPayload.getNewConfigureBakerKeysPayload(accountAddress, BakerKeys.createBakerKeys()))
                .transactionFeeCommission(PartsPerHundredThousand.from(10000))
                .bakingRewardCommission(PartsPerHundredThousand.from(10000))
                .finalizationRewardCommission(PartsPerHundredThousand.from(100000))
                .build();


        val transaction = TransactionFactory
                .newConfigureBaker(payload)
                .sender(accountAddress)
                .nonce(Nonce.from(123))
                .expiry(Expiry.from(413223))
                .sign(TransactionTestHelper.getValidSigner());

        assertEquals(576, transaction.getVersionedBytes().length);
    }

    @SneakyThrows
    @Test
    public void shouldConfigureBakerKeysWithOnlyKeysTest() {
        val accountAddress = AccountAddress.from("48x2Uo8xCMMxwGuSQnwbqjzKtVqK5MaUud4vG7QEUgDmYkV85e");

        val payload = ConfigureBaker.builder()
                .keysWithProofs(ConfigureBakerKeysPayload.getNewConfigureBakerKeysPayload(accountAddress, BakerKeys.createBakerKeys()))
                .build();


        val transaction = TransactionFactory
                .newConfigureBaker(payload)
                .sender(accountAddress)
                .nonce(Nonce.from(123))
                .expiry(Expiry.from(413223))
                .sign(TransactionTestHelper.getValidSigner());

        assertEquals(554, transaction.getVersionedBytes().length);
    }

    @SneakyThrows
    @Test
    public void testCreateBakerCredentials() {
        StringWriter stringWriter = new StringWriter();
        BakerKeys.createBakerKeys().createBakerCredentials(stringWriter, BakerId.from(0));
    }

    @SneakyThrows
    @Test
    public void shouldConfigureSuspendedBakerTest() {
        val accountAddress = AccountAddress.from("48x2Uo8xCMMxwGuSQnwbqjzKtVqK5MaUud4vG7QEUgDmYkV85e");

        val payload = ConfigureBaker.builder()
                .suspended(true)
                .build();

        val transaction = TransactionFactory
                .newConfigureBaker(payload)
                .sender(accountAddress)
                .nonce(Nonce.from(123))
                .expiry(Expiry.from(413223))
                .sign(TransactionTestHelper.getValidSigner());

        assertEquals(203, transaction.getVersionedBytes().length);
    }
}
