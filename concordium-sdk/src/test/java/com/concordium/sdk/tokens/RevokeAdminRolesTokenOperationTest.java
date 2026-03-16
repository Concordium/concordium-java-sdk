package com.concordium.sdk.tokens;

import com.concordium.sdk.serializing.CborMapper;
import com.concordium.sdk.transactions.tokens.*;
import com.concordium.sdk.types.AccountAddress;
import com.google.common.collect.ImmutableList;
import lombok.SneakyThrows;
import lombok.val;
import org.bouncycastle.util.encoders.Hex;
import org.junit.Assert;
import org.junit.Test;

public class RevokeAdminRolesTokenOperationTest {

    @SneakyThrows
    @Test
    public void testRevokeAdminRolesTokenOperationSerialization() {
        val operation = RevokeAdminRolesTokenOperation
                .builder()
                .account(new TaggedTokenHolderAccount(
                        AccountAddress.from(
                                "3CbvrNVpcHpL7tyT2mhXxQwNWHiPNYEJRgp3CMgEcMyXivms6B"
                        )
                ))
                .roles(ImmutableList.of(
                        TokenAdminRole.MINT,
                        TokenAdminRole.UPDATE_ADMIN_ROLES
                ))
                .build();
        val expectedHex = "bf707265766f6b6541646d696e526f6c6573bf676163636f756e74d99d73a103582021bc8745c81c07ca7f3fb79a8bd161624cb1d5da788baec13f5a5d9eac3a29b765726f6c657382646d696e747075706461746541646d696e526f6c6573ffff";
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
