package com.concordium.sdk.transactions;

import com.concordium.sdk.Client;
import com.concordium.sdk.Connection;
import com.concordium.sdk.Credentials;
import com.concordium.sdk.requests.getaccountinfo.AccountRequest;
import com.concordium.sdk.responses.consensusstatus.ConsensusStatus;
import com.concordium.sdk.responses.transactionstatus.DelegationTarget;
import com.concordium.sdk.types.UInt64;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.Assert;
import org.junit.Test;

public class ConfigureDelegationUtilTest {

    @SneakyThrows
    @Test
    public void testConfigureBakerUtil() {
        Connection connection = Connection.builder()
                .credentials(Credentials.from("rpcadmin"))
                .host("127.0.0.1")
                .port(10001)
                .build();
        Client client = Client.from(connection);
        ConsensusStatus accountConcensusStatus = client.getConsensusStatus();

        Hash blockHash = accountConcensusStatus.getBestBlock();
        val accountAddress = AccountAddress.from("48x2Uo8xCMMxwGuSQnwbqjzKtVqK5MaUud4vG7QEUgDmYkV85e");
        val accountInfo = client.getAccountInfo(AccountRequest.from(accountAddress), blockHash);
        long nonceValue = accountInfo.getAccountNonce().getValue().getValue();
        long expiry = System.currentTimeMillis() / 1000 + 500;

        val payload = ConfigureDelegationPayload.builder()
                .capital(CCDAmount.fromMicro("500000"))
                .restakeEarnings(true)
                .delegationTarget(DelegationTarget.builder()
                        .type(DelegationTarget.DelegationType.PASSIVE)
                        .build())
                .build();

        val transaction = TransactionFactory.newConfigureDelegation()
                .sender(accountAddress)
                .nonce(AccountNonce.from(nonceValue))
                .expiry(Expiry.from(expiry))
                .signer(TransactionTestHelper.getValidSigner())
                .payload(payload)
                .maxEnergyCost(UInt64.from(30000))
                .build();

        val txnHash = client.sendTransaction(transaction);
        Assert.assertEquals("", "");
    }
}