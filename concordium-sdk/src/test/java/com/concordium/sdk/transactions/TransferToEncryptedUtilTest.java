package com.concordium.sdk.transactions;

import com.concordium.sdk.AccountRequest;
import com.concordium.sdk.Client;
import com.concordium.sdk.Connection;
import com.concordium.sdk.Credentials;
import com.concordium.sdk.exceptions.AccountNotFoundException;
import com.concordium.sdk.exceptions.ClientInitializationException;
import com.concordium.sdk.exceptions.TransactionCreationException;
import com.concordium.sdk.exceptions.TransactionRejectionException;
import com.concordium.sdk.responses.accountinfo.AccountInfo;
import com.concordium.sdk.responses.consensusstatus.ConsensusStatus;
import com.concordium.sdk.types.UInt64;
import lombok.val;
import org.junit.Test;

public class TransferToEncryptedUtilTest {

    @Test
    public void testTransferToEncryptedUtil() {
        try {
            Connection connection = Connection.builder()
                    .credentials(Credentials.from("rpcadmin"))
                    .host("0.0.0.0")
                    .port(10001)
                    .build();
            Client client = Client.from(connection);
            ConsensusStatus accountConcensusStatus = client.getConsensusStatus();

            Hash blockHash = accountConcensusStatus.getBestBlock();
            AccountRequest accountRequest = AccountRequest.from(AccountAddress.from("48x2Uo8xCMMxwGuSQnwbqjzKtVqK5MaUud4vG7QEUgDmYkV85e"));
            AccountInfo accountInfo;
            try {
                accountInfo = client.getAccountInfo(accountRequest, blockHash);
            } catch (AccountNotFoundException e) {
                throw new RuntimeException(e);
            }
            long nonceValue = accountInfo.getAccountNonce().getValue().getValue();
            long expiry = System.currentTimeMillis()/1000 + 500;


            val transaction = TransactionFactory.newTransferToEncrypted()
                    .sender(AccountAddress.from("48x2Uo8xCMMxwGuSQnwbqjzKtVqK5MaUud4vG7QEUgDmYkV85e"))
                    .nonce(AccountNonce.from(nonceValue))
                    .expiry(Expiry.from(expiry))
                    .signer(TransactionTestHelper.getValidSigner())
                    .payload(TransferToEncryptedPayload.from(1))
                    .maxEnergyCost(UInt64.from(3000))
                    .build();

            client.sendTransaction(transaction);

        } catch (TransactionRejectionException | ClientInitializationException | TransactionCreationException e) {
            throw new RuntimeException(e);
        }
    }
}