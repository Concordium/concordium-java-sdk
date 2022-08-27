package com.concordium.sdk.transactions;

import com.concordium.sdk.crypto.ed25519.ED25519SecretKey;
import com.concordium.sdk.types.Nonce;
import com.concordium.sdk.types.UInt64;
import lombok.SneakyThrows;
import org.junit.Test;

public class InitContractTest {
    @SneakyThrows
    @Test
    public void testCreateTransfer() {
        Hash mod_ref = Hash.from("37eeb3e92025c97eaf40b66891770fcd22d926a91caeb1135c7ce7a1ba977c07");
        InitContract.createNew(
                        InitContractPayload.from(0, mod_ref.getBytes(), "CIS2-NFT",  new byte[0]), UInt64.from(3000))
                .withHeader(TransactionHeader
                        .builder()
                        .sender(AccountAddress.from("3JwD2Wm3nMbsowCwb1iGEpnt47UQgdrtnq2qT6opJc3z2AgCrc"))
                        .accountNonce(Nonce.from(78910))
                        .expiry(UInt64.from(123456))
                        .build())
                .signWith(
                        TransactionSigner.from(
                                SignerEntry.from(Index.from(0), Index.from(0),
                                        ED25519SecretKey.from("7100071c835a0a35e86dccba7ee9d10b89e36d1e596771cdc8ee36a17f7abbf2")),
                                SignerEntry.from(Index.from(0), Index.from(1),
                                        ED25519SecretKey.from("cd20ea0127cddf77cf2c20a18ec4516a99528a72e642ac7deb92131a9d108ae9"))
                        )
                );
    }
}
