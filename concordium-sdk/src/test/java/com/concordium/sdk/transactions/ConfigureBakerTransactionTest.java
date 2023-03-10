package com.concordium.sdk.transactions;

import com.concordium.sdk.types.UInt32;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ConfigureBakerTransactionTest {

    @SneakyThrows
    @Test
    public void shouldAddBakerKeysTest() {
        val accountAddress = AccountAddress.from("48x2Uo8xCMMxwGuSQnwbqjzKtVqK5MaUud4vG7QEUgDmYkV85e");

        val payload = ConfigureBakerPayload.builder()
                .capital(CCDAmount.fromMicro("14000000000"))
                .restakeEarnings(true)
                .openForDelegation(0)
                .keysWithProofs(ConfigureBakerKeysPayload.getNewConfigureBakerKeysPayload(accountAddress))
                .metadataUrl("abc@xyz.com")
                .transactionFeeCommission(UInt32.from(10000))
                .bakingRewardCommission(UInt32.from(10000))
                .finalizationRewardCommission(UInt32.from(100000))
                .build();


        val transaction = TransactionFactory.newConfigureBaker()
                .sender(accountAddress)
                .nonce(AccountNonce.from(123))
                .expiry(Expiry.from(413223))
                .signer(TransactionTestHelper.getValidSigner())
                .payload(payload)
                .build();

        assertEquals(589, transaction.getBytes().length);
    }


    @SneakyThrows
    @Test
    public void shouldConfigureEmptyBakerTest() {
        val accountAddress = AccountAddress.from("48x2Uo8xCMMxwGuSQnwbqjzKtVqK5MaUud4vG7QEUgDmYkV85e");

        val payload = ConfigureBakerPayload.builder()
                .build();


        val transaction = TransactionFactory.newConfigureBaker()
                .sender(accountAddress)
                .nonce(AccountNonce.from(123))
                .expiry(Expiry.from(413223))
                .signer(TransactionTestHelper.getValidSigner())
                .payload(payload)
                .build();

        assertEquals(202, transaction.getBytes().length);
    }

    @SneakyThrows
    @Test
    public void shouldAddBakerKeysWithoutMetadataUrlTest() {
        val accountAddress = AccountAddress.from("48x2Uo8xCMMxwGuSQnwbqjzKtVqK5MaUud4vG7QEUgDmYkV85e");

        val payload = ConfigureBakerPayload.builder()
                .capital(CCDAmount.fromMicro("14000000000"))
                .restakeEarnings(true)
                .openForDelegation(0)
                .keysWithProofs(ConfigureBakerKeysPayload.getNewConfigureBakerKeysPayload(accountAddress))
                .transactionFeeCommission(UInt32.from(10000))
                .bakingRewardCommission(UInt32.from(10000))
                .finalizationRewardCommission(UInt32.from(100000))
                .build();


        val transaction = TransactionFactory.newConfigureBaker()
                .sender(accountAddress)
                .nonce(AccountNonce.from(123))
                .expiry(Expiry.from(413223))
                .signer(TransactionTestHelper.getValidSigner())
                .payload(payload)
                .build();

        assertEquals(576, transaction.getBytes().length);
    }

    @SneakyThrows
    @Test
    public void shouldConfigureBakerKeysWithOnlyKeysTest() {
        val accountAddress = AccountAddress.from("48x2Uo8xCMMxwGuSQnwbqjzKtVqK5MaUud4vG7QEUgDmYkV85e");

        val payload = ConfigureBakerPayload.builder()
                .keysWithProofs(ConfigureBakerKeysPayload.getNewConfigureBakerKeysPayload(accountAddress))
                .build();


        val transaction = TransactionFactory.newConfigureBaker()
                .sender(accountAddress)
                .nonce(AccountNonce.from(123))
                .expiry(Expiry.from(413223))
                .signer(TransactionTestHelper.getValidSigner())
                .payload(payload)
                .build();

        assertEquals(554, transaction.getBytes().length);
    }
}
