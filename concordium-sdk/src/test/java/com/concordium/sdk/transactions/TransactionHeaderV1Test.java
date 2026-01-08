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

public class TransactionHeaderV1Test {

    @Test
    @SneakyThrows
    public void serialization() {
        val header = TransactionHeaderV1
                .builder()
                .sender(AccountAddress.from("3VwCfvVskERFAJ3GeJy2mNFrzfChqUymSJJCvoLAP9rtAwMGYt"))
                .sponsor(AccountAddress.from("4ZJBYQbVp3zVZyjCXfZAAYBVkJMyVj8UKUNj9ox5YqTCBdBq2M"))
                .nonce(Nonce.from(1))
                .expiry(UInt64.from(1700000000))
                .maxEnergyCost(UInt64.from(500))
                .payloadSize(UInt32.from(41))
                .build();
        val expectedHex = "000149176df18432686c93c61ca89dafbe1cb383bfe6eb3a301ef8907f852643d98d000000000000000100000000000001f400000029000000006553f100d46bbc5fbbbbabb07752d4acb86892d7a2479856d414182f703e21065dad046d";

        Assert.assertEquals(
                expectedHex,
                Hex.toHexString(header.getBytes())
        );
        Assert.assertEquals(
                header,
                TransactionHeaderV1.fromBytes(ByteBuffer.wrap(Hex.decode(expectedHex)))
        );
    }

    @Test
    @SneakyThrows
    public void serializationSenderOnly() {
        val header = TransactionHeaderV1
                .builder()
                .sender(AccountAddress.from("3VwCfvVskERFAJ3GeJy2mNFrzfChqUymSJJCvoLAP9rtAwMGYt"))
                .sponsor(null)
                .nonce(Nonce.from(1))
                .expiry(UInt64.from(1700000000))
                .maxEnergyCost(UInt64.from(500))
                .payloadSize(UInt32.from(41))
                .build();
        val expectedHex = "000049176df18432686c93c61ca89dafbe1cb383bfe6eb3a301ef8907f852643d98d000000000000000100000000000001f400000029000000006553f100";

        Assert.assertEquals(
                expectedHex,
                Hex.toHexString(header.getBytes())
        );
        Assert.assertEquals(
                header,
                TransactionHeaderV1.fromBytes(ByteBuffer.wrap(Hex.decode(expectedHex)))
        );
    }
}
