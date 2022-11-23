package com.concordium.sdk.transactions;

import com.concordium.sdk.Client;
import com.concordium.sdk.Connection;
import com.concordium.sdk.Credentials;
import com.concordium.sdk.requests.getaccountinfo.AccountRequest;
import com.concordium.sdk.responses.consensusstatus.ConsensusStatus;
import com.concordium.sdk.types.UInt32;
import com.concordium.sdk.types.UInt64;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.Assert;
import org.junit.Test;

public class AddBakerUtilTest {

    @SneakyThrows
    @Test
    public void testAddBakerUtil() {
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
        val payload = ConfigureBakerPayload.builder()
                .capital(CCDAmount.fromMicro("14000000000"))
                .restakeEarnings(true)
                .openForDelegation(0)
                .keysWithProofs(ConfigureBakerKeysPayload.newBakerKeysWithPayload(accountAddress))
                .metadataUrl("abc@xyz.com")
                .transactionFeeCommission(UInt32.from(10000))
                .bakingRewardCommission(UInt32.from(10000))
                .finalizationRewardCommission(UInt32.from(100000))
                .build();

        val transaction = TransactionFactory.newConfigureBaker()
                .sender(accountAddress)
                .nonce(AccountNonce.from(nonceValue))
                .expiry(Expiry.from(expiry))
                .signer(TransactionTestHelper.getValidSigner())
                .payload(payload)
                .maxEnergyCost(UInt64.from(30000))
                .build();

//        val transaction = TransactionFactory.newRemoveBaker()
//                .sender(accountAddress)
//                .nonce(AccountNonce.from(nonceValue))
//                .expiry(Expiry.from(expiry))
//                .signer(TransactionTestHelper.getValidSigner())
//                .maxEnergyCost(UInt64.from(30000))
//                .build();

//        val transaction = TransactionFactory.newUpdateBakerStake(10000)
//                .sender(accountAddress)
//                .nonce(AccountNonce.from(nonceValue))
//                .expiry(Expiry.from(expiry))
//                .signer(TransactionTestHelper.getValidSigner())
//                .maxEnergyCost(UInt64.from(300000))
//                .build();
//
//        val transaction = TransactionFactory.newUpdateBakerRestakeEarnings(true)
//                .sender(accountAddress)
//                .nonce(AccountNonce.from(nonceValue))
//                .expiry(Expiry.from(expiry))
//                .signer(TransactionTestHelper.getValidSigner())
//                .maxEnergyCost(UInt64.from(300000))
//                .build();

//        val transaction = TransactionFactory.newUpdateBakerKeys(accountAddress)
//                .sender(accountAddress)
//                .nonce(AccountNonce.from(nonceValue))
//                .expiry(Expiry.from(expiry))
//                .signer(TransactionTestHelper.getValidSigner())
//                .maxEnergyCost(UInt64.from(300000))
//                .build();

        val txnHash = client.sendTransaction(transaction);
        Assert.assertEquals(Hash.from(""), txnHash);
    }
}