package com.concordium.sdk.transactions;

import com.concordium.sdk.types.AccountAddress;
import com.concordium.sdk.types.Nonce;
import com.concordium.sdk.types.UInt32;
import com.concordium.sdk.types.UInt64;
import lombok.SneakyThrows;
import lombok.val;
import org.bouncycastle.util.encoders.Hex;
import org.junit.Assert;
import org.junit.Test;

import java.nio.ByteBuffer;

public class AccountTransactionV1Test {

    @Test
    @SneakyThrows
    public void serialization() {
        val transaction = new AccountTransactionV1(
                new TransactionSignaturesV1(
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
                ),
                TransactionHeaderV1
                        .builder()
                        .sender(AccountAddress.from("3VwCfvVskERFAJ3GeJy2mNFrzfChqUymSJJCvoLAP9rtAwMGYt"))
                        .sponsor(AccountAddress.from("4ZJBYQbVp3zVZyjCXfZAAYBVkJMyVj8UKUNj9ox5YqTCBdBq2M"))
                        .nonce(Nonce.from(1))
                        .expiry(UInt64.from(1700000000))
                        .maxEnergyCost(UInt64.from(500))
                        .payloadSize(UInt32.from(41))
                        .build(),
                Transfer.createNew(
                        AccountAddress.from("4ZJBYQbVp3zVZyjCXfZAAYBVkJMyVj8UKUNj9ox5YqTCBdBq2M"),
                        CCDAmount.fromMicro(1000000)
                )
        );
        val expectedHex = "010001000040893f2e4a230bcbeee24675454c4ca95a2f55fd33f328958b626c6fa368341e07902c9ffe7864c3bee23b2b2300ed0922eb814ea41fdee25035be8cddc5c3980f010001000040620d859224c40160c2bb03dbe84e9f57b8ed17f1a5df28b4e21f10658992531ef27655e6b74b8e47923e1ccb0413d563205e8b6c0cd22b3adce5dc7dc1daf603000149176df18432686c93c61ca89dafbe1cb383bfe6eb3a301ef8907f852643d98d000000000000000100000000000001f400000029000000006553f100d46bbc5fbbbbabb07752d4acb86892d7a2479856d414182f703e21065dad046d03d46bbc5fbbbbabb07752d4acb86892d7a2479856d414182f703e21065dad046d00000000000f4240";

        Assert.assertEquals(
                expectedHex,
                Hex.toHexString(transaction.getBlockItemBytes())
        );
        Assert.assertEquals(
                transaction,
                AccountTransactionV1.fromBytes(ByteBuffer.wrap(Hex.decode(expectedHex)))
        );
    }

    @Test
    @SneakyThrows
    public void serializationSenderOnly() {
        val transaction = new AccountTransactionV1(
                new TransactionSignaturesV1(
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
                ),
                TransactionHeaderV1
                        .builder()
                        .sender(AccountAddress.from("3VwCfvVskERFAJ3GeJy2mNFrzfChqUymSJJCvoLAP9rtAwMGYt"))
                        .sponsor(null)
                        .nonce(Nonce.from(1))
                        .expiry(UInt64.from(1700000000))
                        .maxEnergyCost(UInt64.from(500))
                        .payloadSize(UInt32.from(41))
                        .build(),
                Transfer.createNew(
                        AccountAddress.from("4ZJBYQbVp3zVZyjCXfZAAYBVkJMyVj8UKUNj9ox5YqTCBdBq2M"),
                        CCDAmount.fromMicro(1000000)
                )
        );
        val expectedHex = "010001000040893f2e4a230bcbeee24675454c4ca95a2f55fd33f328958b626c6fa368341e07902c9ffe7864c3bee23b2b2300ed0922eb814ea41fdee25035be8cddc5c3980f00000049176df18432686c93c61ca89dafbe1cb383bfe6eb3a301ef8907f852643d98d000000000000000100000000000001f400000029000000006553f10003d46bbc5fbbbbabb07752d4acb86892d7a2479856d414182f703e21065dad046d00000000000f4240";

        Assert.assertEquals(
                expectedHex,
                Hex.toHexString(transaction.getBlockItemBytes())
        );
        Assert.assertEquals(
                transaction,
                AccountTransactionV1.fromBytes(ByteBuffer.wrap(Hex.decode(expectedHex)))
        );
    }
}
