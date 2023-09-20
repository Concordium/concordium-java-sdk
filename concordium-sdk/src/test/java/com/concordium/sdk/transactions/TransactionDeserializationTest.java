package com.concordium.sdk.transactions;

import com.concordium.sdk.crypto.ed25519.ED25519SecretKey;
import com.concordium.sdk.types.AccountAddress;
import com.concordium.sdk.types.Nonce;
import com.concordium.sdk.types.UInt64;
import lombok.val;
import org.junit.Test;

import java.nio.ByteBuffer;

import static org.junit.Assert.assertEquals;

public class TransactionDeserializationTest {

    private static BlockItem bi = Transfer.createNew(
                    AccountAddress.from("3hYXYEPuGyhFcVRhSk2cVgKBhzVcAryjPskYk4SecpwGnoHhuM"),
                    CCDAmount.fromMicro(17))
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
            ).toBlockItem();

    @Test
    public void deserializeSignature() {
        BlockItem deserializedBlockItem = BlockItem.fromVersionedBytes(ByteBuffer.wrap(bi.getVersionedBytes()));
        assertEquals(bi, deserializedBlockItem);
        System.out.println(deserializedBlockItem);
    }
}
