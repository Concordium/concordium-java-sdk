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

import static org.junit.Assert.fail;

public class InitContractUtilTest {

    @Test
    public void testInitContractUtil() {
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

            byte[] emptyArray = new byte[0];
            Hash mod_ref = Hash.from("37eeb3e92025c97eaf40b66891770fcd22d926a91caeb1135c7ce7a1ba977c07");

            val transaction = InitContractUtil.newInitContract()
                    .sender(AccountAddress.from("48x2Uo8xCMMxwGuSQnwbqjzKtVqK5MaUud4vG7QEUgDmYkV85e"))
                    .nonce(AccountNonce.from(nonceValue))
                    .expiry(Expiry.from(expiry))
                    .signer(TransactionTestHelper.getValidSigner())
                    .payload(InitContractPayload.from(0, mod_ref.getBytes(), "init_CIS2-NFT", emptyArray))
                    .maxEnergyCost(UInt64.from(3000))
                    .build();

            client.sendTransaction(transaction);

        } catch (TransactionCreationException e) {
            fail("Unexpected error: " + e.getMessage());
        } catch (TransactionRejectionException | ClientInitializationException e) {
            throw new RuntimeException(e);
        }
    }
}