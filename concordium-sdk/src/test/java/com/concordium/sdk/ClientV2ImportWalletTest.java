package com.concordium.sdk;

import com.concordium.sdk.crypto.ed25519.ED25519SecretKey;
import com.concordium.sdk.transactions.Index;
import com.concordium.sdk.transactions.SignerEntry;
import com.concordium.sdk.transactions.TransactionSigner;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class ClientV2ImportWalletTest {

    public static final TransactionSigner EXPECTED_TRANSACTION_SIGNER = TransactionSigner.from(
            SignerEntry.from(Index.from(0), Index.from(0)
                    , ED25519SecretKey.from("56f60de843790c308dac2d59a5eec9f6b1649513f827e5a13d7038accfe31784"))
    );

    @Test
    public void importWallet() throws IOException {
        assertEquals(EXPECTED_TRANSACTION_SIGNER, TransactionSigner.from(new File("./src/test/java/com/concordium/sdk/walletexports/testWalletExport.export")));
    }
}
