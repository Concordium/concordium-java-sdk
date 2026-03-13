package com.concordium.sdk.tokens;

import com.concordium.sdk.serializing.JsonMapper;
import com.concordium.sdk.transactions.tokens.TokenAdminRole;
import com.fasterxml.jackson.databind.exc.ValueInstantiationException;
import com.google.common.collect.ImmutableList;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.Assert;
import org.junit.Test;

public class TokenAdminRoleTest {

    @SneakyThrows
    @Test
    public void testTokenAdminRoleSerialization() {
        val roles = ImmutableList.of(
                TokenAdminRole.UPDATE_ADMIN_ROLES,
                TokenAdminRole.UPDATE_ALLOW_LIST,
                TokenAdminRole.PAUSE
        );
        val expectedJson = "[\"updateAdminRoles\",\"updateAllowList\",\"pause\"]";
        Assert.assertEquals(
                expectedJson,
                JsonMapper.INSTANCE.writeValueAsString(roles)
        );
        Assert.assertEquals(
                roles,
                JsonMapper.INSTANCE
                        .readerFor(TokenAdminRole.LIST_TYPE)
                        .readValue(expectedJson)
        );
    }

    @SneakyThrows
    @Test(expected = ValueInstantiationException.class)
    public void testUnknownTokenAdminRoleSerialization() {
        val json = "[\"unknownRole\",\"updateAllowList\",\"pause\"]";
        JsonMapper.INSTANCE
                .readerFor(TokenAdminRole.LIST_TYPE)
                .readValue(json);
    }
}
