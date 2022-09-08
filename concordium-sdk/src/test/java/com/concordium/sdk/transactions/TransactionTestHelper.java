package com.concordium.sdk.transactions;

import com.concordium.sdk.crypto.ed25519.ED25519SecretKey;

public class TransactionTestHelper {

    public static TransactionSigner getValidSigner() {
        ED25519SecretKey firstSecretKey = ED25519SecretKey.from("a6f55bb31c010cae9ffc1ec35c7d535f4bc07f8bac7d274eca0d9786fd2c4693");

        return TransactionSigner.from(
                SignerEntry.from(Index.from(0), Index.from(0),
                        firstSecretKey));
    }
}
