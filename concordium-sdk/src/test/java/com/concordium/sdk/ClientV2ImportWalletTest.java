package com.concordium.sdk;

import com.concordium.sdk.crypto.ed25519.ED25519SecretKey;
import com.concordium.sdk.transactions.Index;
import com.concordium.sdk.transactions.SignerEntry;
import com.concordium.sdk.transactions.TransactionSigner;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Constructs {@link TransactionSigner} from the 2 wallets in /test/'*'/walleterxport
 * and asserts the result is as expected.
 */
public class ClientV2ImportWalletTest {

    public static final TransactionSigner EXPECTED_BROWSER_TRANSACTION_SIGNER = TransactionSigner.from(
            SignerEntry.from(Index.from(0), Index.from(0)
                    , ED25519SecretKey.from("56f60de843790c308dac2d59a5eec9f6b1649513f827e5a13d7038accfe31784"))
    );

    public static final TransactionSigner EXPECTED_GENESIS_TRANSACTION_SIGNER = TransactionSigner.from(
            SignerEntry.from(Index.from(0), Index.from(0),
                    ED25519SecretKey.from("443c20439711361b6870c1679be33860d10cf7cded240e4a567e31ec3a56ecf5"))
    );

    @Test
    public void importBrowserWallet() throws IOException {
        assertEquals(EXPECTED_BROWSER_TRANSACTION_SIGNER, TransactionSigner.from(new File("./src/test/java/com/concordium/sdk/walletexports/browserWallet.json")));
    }

    @Test
    public void importGenesisWallet() throws IOException {
        assertEquals(EXPECTED_GENESIS_TRANSACTION_SIGNER, TransactionSigner.from(new File("./src/test/java/com/concordium/sdk/walletexports/genesisWallet.json")));
    }
}
