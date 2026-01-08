package com.concordium.sdk.transactions;

import lombok.SneakyThrows;
import lombok.val;
import org.bouncycastle.util.encoders.Hex;
import org.junit.Assert;
import org.junit.Test;

import java.nio.ByteBuffer;

public class TransactionSignaturesV1Test {

    @Test
    @SneakyThrows
    public void serialization() {
        val signatures = new TransactionSignaturesV1(
                TransactionSignature
                        .builder()
                        .signature(
                                Index.from(0),
                                TransactionSignatureAccountSignatureMap
                                        .builder()
                                        .signature(
                                                Index.from(0),
                                                Signature.from(Hex.decode("893f2e4a230bcbeee24675454c4ca95a2f55fd33f328958b626c6fa368341e07902c9ffe7864c3bee23b2b2300ed0922eb814ea41fdee25035be8cddc5c3980f"))
                                        )
                                        .build()
                        )
                        .build(),
                TransactionSignature
                        .builder()
                        .signature(
                                Index.from(0),
                                TransactionSignatureAccountSignatureMap
                                        .builder()
                                        .signature(
                                                Index.from(0),
                                                Signature.from(Hex.decode("620d859224c40160c2bb03dbe84e9f57b8ed17f1a5df28b4e21f10658992531ef27655e6b74b8e47923e1ccb0413d563205e8b6c0cd22b3adce5dc7dc1daf603"))
                                        )
                                        .build()
                        )
                        .build()
        );
        val expectedHex = "010001000040893f2e4a230bcbeee24675454c4ca95a2f55fd33f328958b626c6fa368341e07902c9ffe7864c3bee23b2b2300ed0922eb814ea41fdee25035be8cddc5c3980f010001000040620d859224c40160c2bb03dbe84e9f57b8ed17f1a5df28b4e21f10658992531ef27655e6b74b8e47923e1ccb0413d563205e8b6c0cd22b3adce5dc7dc1daf603";

        Assert.assertEquals(
                expectedHex,
                Hex.toHexString(signatures.getBytes())
        );
        Assert.assertEquals(
                signatures,
                TransactionSignaturesV1.fromBytes(ByteBuffer.wrap(Hex.decode(expectedHex)))
        );
    }

    @Test
    @SneakyThrows
    public void serializationSenderOnly() {
        val signatures = new TransactionSignaturesV1(
                TransactionSignature
                        .builder()
                        .signature(
                                Index.from(0),
                                TransactionSignatureAccountSignatureMap
                                        .builder()
                                        .signature(
                                                Index.from(0),
                                                Signature.from(Hex.decode("893f2e4a230bcbeee24675454c4ca95a2f55fd33f328958b626c6fa368341e07902c9ffe7864c3bee23b2b2300ed0922eb814ea41fdee25035be8cddc5c3980f"))
                                        )
                                        .build()
                        )
                        .build(),
                null
        );
        val expectedHex = "010001000040893f2e4a230bcbeee24675454c4ca95a2f55fd33f328958b626c6fa368341e07902c9ffe7864c3bee23b2b2300ed0922eb814ea41fdee25035be8cddc5c3980f00";

        Assert.assertEquals(
                expectedHex,
                Hex.toHexString(signatures.getBytes())
        );
        Assert.assertEquals(
                signatures,
                TransactionSignaturesV1.fromBytes(ByteBuffer.wrap(Hex.decode(expectedHex)))
        );
    }
}
