package com.concordium.sdk.tokens;

import com.concordium.sdk.serializing.CborMapper;
import com.concordium.sdk.transactions.tokens.TaggedTokenHolderAccount;
import com.concordium.sdk.types.AccountAddress;
import lombok.SneakyThrows;
import org.bouncycastle.util.encoders.Hex;
import org.junit.Assert;
import org.junit.Test;

public class TaggedTokenHolderAccountTest {

    @Test
    @SneakyThrows
    public void testTaggedTokenHolderAccountSerialization() {
        Assert.assertEquals(
                "d99d73a10358201515151515151515151515151515151515151515151515151515151515151515",
                Hex.toHexString(
                        CborMapper.INSTANCE.writeValueAsBytes(
                                new TaggedTokenHolderAccount(
                                        AccountAddress.from(
                                                Hex.decode("1515151515151515151515151515151515151515151515151515151515151515")
                                        )
                                )
                        )
                )
        );
    }
}
