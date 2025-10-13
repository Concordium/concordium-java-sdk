package com.concordium.sdk.tokens;

import com.concordium.sdk.serializing.CborMapper;
import com.concordium.sdk.transactions.tokens.TaggedTokenHolderAccount;
import com.concordium.sdk.types.AccountAddress;
import lombok.SneakyThrows;
import lombok.val;
import org.bouncycastle.util.encoders.Hex;
import org.junit.Assert;
import org.junit.Test;

public class TaggedTokenHolderAccountTest {

    @Test
    @SneakyThrows
    public void testTaggedTokenHolderAccountSerialization() {
        val account = new TaggedTokenHolderAccount(
                AccountAddress.from(
                        Hex.decode("1515151515151515151515151515151515151515151515151515151515151515")
                )
        );
        val expectedHex = "d99d73a10358201515151515151515151515151515151515151515151515151515151515151515";
        Assert.assertEquals(
                expectedHex,
                Hex.toHexString(CborMapper.INSTANCE.writeValueAsBytes(account))
        );
        Assert.assertEquals(
                account,
                CborMapper.INSTANCE.readValue(
                        Hex.decode(expectedHex),
                        TaggedTokenHolderAccount.class
                )
        );
    }

    @Test
    @SneakyThrows
    public void testTaggedTokenHolderAccountDeserializationWithCoinInfo() {
        // Coin info is ignored at the moment.
        // Only the address must be read.
        Assert.assertEquals(
                new TaggedTokenHolderAccount(
                        AccountAddress.from(
                                Hex.decode("1515151515151515151515151515151515151515151515151515151515151515")
                        )
                ),
                CborMapper.INSTANCE.readValue(
                        Hex.decode("d99d73a201d99d71a1011903970358201515151515151515151515151515151515151515151515151515151515151515"),
                        TaggedTokenHolderAccount.class
                )
        );
    }
}
