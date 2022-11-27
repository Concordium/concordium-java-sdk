package com.concordium.sdk.transactions;

import com.concordium.sdk.responses.transactionstatus.DelegationTarget;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ConfigureDelegationTransactionTest {

    @SneakyThrows
    @Test
    public void shouldConfigureDelegationTransaction() {
        val accountAddress = AccountAddress.from("48x2Uo8xCMMxwGuSQnwbqjzKtVqK5MaUud4vG7QEUgDmYkV85e");

        val payload = ConfigureDelegationPayload.builder()
                .capital(CCDAmount.fromMicro("500000"))
                .restakeEarnings(true)
                .delegationTarget(DelegationTarget.builder()
                        .type(DelegationTarget.DelegationType.PASSIVE)
                        .build())
                .build();


        val transaction = ConfigureDelegationTransaction.builder()
                .sender(accountAddress)
                .nonce(AccountNonce.from(123))
                .expiry(Expiry.from(413223))
                .signer(TransactionTestHelper.getValidSigner())
                .payload(payload)
                .build();

        assertEquals(212, transaction.getBytes().length);
    }

}
