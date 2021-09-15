package com.concordium.sdk.transactions;

import com.concordium.sdk.crypto.ed25519.ED25519SecretKey;
import com.concordium.sdk.exceptions.ED25519Exception;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.Test;

public class TransactionSignerTest {

    @SneakyThrows
    @Test
    public void testTransactionSigner() {
        val signer = TransactionSigner.from(SignerEntry.from(Index.from(0), Index.from(1), new Signer() {
                    @Override
                    public byte[] sign(byte[] message) throws ED25519Exception {
                        return new byte[0];
                    }
                }),
                SignerEntry.from(Index.from(0), Index.from(2), ED25519SecretKey.from(new byte[32])));
    }
}
