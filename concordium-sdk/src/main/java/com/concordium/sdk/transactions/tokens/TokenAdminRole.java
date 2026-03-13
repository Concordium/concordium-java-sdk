package com.concordium.sdk.transactions.tokens;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.List;

public enum TokenAdminRole {
    UPDATE_ADMIN_ROLES("updateAdminRoles"),
    MINT("mint"),
    BURN("burn"),
    UPDATE_ALLOW_LIST("updateAllowList"),
    UPDATE_DENY_LIST("updateDenyList"),
    PAUSE("pause"),
    ;

    private final String identifier;

    TokenAdminRole(String identifier) {
        this.identifier = identifier;
    }

    @JsonValue
    public String getIdentifier() {
        return identifier;
    }

    @JsonCreator
    public static TokenAdminRole from(String identifier) {
        for (TokenAdminRole role : TokenAdminRole.values()) {
            if (role.getIdentifier().equals(identifier)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Unsupported TokenAdminRole: " + identifier);
    }

    public static final TypeReference<List<TokenAdminRole>> LIST_TYPE =
            new TypeReference<List<TokenAdminRole>>() {
            };
}
