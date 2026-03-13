package com.concordium.sdk.transactions.tokens;

import com.concordium.sdk.types.UInt64;
import lombok.*;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

/**
 * Assign admin role(s) to an account.
 */
@Getter
@Builder
@EqualsAndHashCode
@ToString
// This is for CBOR serialization to use the generated builder.
@Jacksonized
public class AssignAdminRolesTokenOperation implements TokenOperation {

    /**
     * The account to add roles for.
     */
    @NonNull
    private final TaggedTokenHolderAccount account;

    /**
     * The roles to add.
     */
    @NonNull
    private final List<TokenAdminRole> roles;

    @Override
    public UInt64 getBaseCost() {
        // TODO set the real value.
        return UInt64.from(300);
    }

    @Override
    public String getType() {
        return TYPE;
    }

    public static final String TYPE = "assignAdminRoles";
}
