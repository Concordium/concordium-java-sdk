package com.concordium.sdk.transactions.tokens;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.List;

public enum TokenAdminRole {
    /**
     * Gives authority to perform token-assign-admin-roles and token-revoke-admin-roles operations.
     */
    UPDATE_ADMIN_ROLES("updateAdminRoles"),

    /**
     * Gives authority to perform token-mint operations.
     */
    MINT("mint"),

    /**
     * Gives authority to perform token-burn operations.
     */
    BURN("burn"),

    /**
     * Gives authority to perform token-add-allow-list and token-remove-allow-list operations.
     */
    UPDATE_ALLOW_LIST("updateAllowList"),

    /**
     * Gives authority to perform token-add-deny-list and token-remove-deny-list operations.
     */
    UPDATE_DENY_LIST("updateDenyList"),

    /**
     * Gives authority to perform token-pause and token-unpause operations.
     */
    PAUSE("pause"),

    /**
     * Gives authority to perform token-update-metadata operations.
     */
    UPDATE_METADATA("updateMetadata")
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

    public static final TypeReference<List<TokenAdminRole>> LIST_TYPE = new TypeReference<List<TokenAdminRole>>() {
    };
}
