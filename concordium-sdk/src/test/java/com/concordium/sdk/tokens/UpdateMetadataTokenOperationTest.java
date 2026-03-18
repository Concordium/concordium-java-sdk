package com.concordium.sdk.tokens;

import com.concordium.sdk.serializing.CborMapper;
import com.concordium.sdk.transactions.Hash;
import com.concordium.sdk.transactions.tokens.TokenOperation;
import com.concordium.sdk.transactions.tokens.UpdateMetadataTokenOperation;
import lombok.SneakyThrows;
import lombok.val;
import org.bouncycastle.util.encoders.Hex;
import org.junit.Assert;
import org.junit.Test;

public class UpdateMetadataTokenOperationTest {

    @SneakyThrows
    @Test
    public void testUpdateMetadataTokenOperationSerialization() {
        val operation = UpdateMetadataTokenOperation
                .builder()
                .url("https://example.com/metadata.json")
                .checksumSha256(Hash.from("0123456789abcdef0123456789abcdef0123456789abcdef0123456789abcdef"))
                .build();
        val expectedHex = "bf6e7570646174654d65746164617461bf6375726c782168747470733a2f2f6578616d706c652e636f6d2f6d657461646174612e6a736f6e6e636865636b73756d53686132353658200123456789abcdef0123456789abcdef0123456789abcdef0123456789abcdefffff";
        Assert.assertEquals(
                expectedHex,
                Hex.toHexString(CborMapper.INSTANCE.writeValueAsBytes(operation))
        );
        Assert.assertEquals(
                operation,
                CborMapper.INSTANCE.readValue(
                        Hex.decode(expectedHex),
                        TokenOperation.class
                )
        );
    }

    @SneakyThrows
    @Test
    public void testUpdateTokenMetadataOperationWithoutHashSerialization() {
        val operation = UpdateMetadataTokenOperation
                .builder()
                .url("https://example.com/metadata.json")
                .build();
        val expectedHex = "bf6e7570646174654d65746164617461bf6375726c782168747470733a2f2f6578616d706c652e636f6d2f6d657461646174612e6a736f6effff";
        Assert.assertEquals(
                expectedHex,
                Hex.toHexString(CborMapper.INSTANCE.writeValueAsBytes(operation))
        );
        Assert.assertEquals(
                operation,
                CborMapper.INSTANCE.readValue(
                        Hex.decode(expectedHex),
                        TokenOperation.class
                )
        );
    }
}
