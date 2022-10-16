package com.concordium.sdk.transactions;

import com.concordium.sdk.Client;
import com.concordium.sdk.Connection;
import com.concordium.sdk.Credentials;
import com.concordium.sdk.requests.getaccountinfo.AccountRequest;
import com.concordium.sdk.responses.accountinfo.credential.Credential;
import com.concordium.sdk.responses.accountinfo.credential.Key;
import com.concordium.sdk.responses.consensusstatus.ConsensusStatus;
import com.concordium.sdk.types.UInt64;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

public class UpdateCredentialKeysTest {

    @SneakyThrows
    @Test
    public void testUpdateCredentialKeysUtil() {
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

        Map<Index, Credential> y = accountInfo.getAccountCredentials();
        Map<Index, Key> keys =  null;
        int threshold = 0;
        CredentialRegistrationId regId = null;
        for (Index index : y.keySet()) {
            Credential cred = y.get(index);
            keys = cred.getCredentialPublicKeys().getKeys();
            threshold = cred.getCredentialPublicKeys().getThreshold();
            regId = cred.getCredId();
        }

        CredentialPublicKeys credentialPublicKeys = CredentialPublicKeys.from(keys, threshold);
        val transaction = TransactionFactory.newUpdateCredentialKeys()
                .sender(accountAddress)
                .nonce(AccountNonce.from(nonceValue))
                .expiry(Expiry.from(expiry))
                .signer(TransactionTestHelper.getValidSigner())
                .credentialRegistrationID(regId)
                .keys(credentialPublicKeys)
                .maxEnergyCost(UInt64.from(20000))
                .build();

        val txnHash = client.sendTransaction(transaction);
        Assert.assertEquals(Hash.from(""), txnHash);
    }
}
