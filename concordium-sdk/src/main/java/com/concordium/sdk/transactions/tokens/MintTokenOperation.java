package com.concordium.sdk.transactions.tokens;

import com.concordium.sdk.types.UInt64;
import lombok.*;
import lombok.extern.jackson.Jacksonized;

import java.util.Optional;

/**
 * Mint Token
 * @see <a href="https://github.com/Concordium/concordium-update-proposals/blob/main/source/CIS/cis-7.rst#transfer:~:text=%3B%20Mint%20a%20specified%20amount%20to%20the%20sender%20account.%0Atoken%2Dmint%20%3D%20%7B%0A%20%20%20%20%3B%20The%20operation%20type%20is%20%22mint%22.%0A%20%20%20%20%22mint%22%3A%20token%2Dsupply%2Dupdate%2Ddetails">CBOR Schema</a>
 */
@Getter
@Builder
@EqualsAndHashCode
@ToString
// This is for CBOR serialization to use the generated builder.
@Jacksonized
public class MintTokenOperation implements TokenOperation {

    /**
     * Amount to be transferred.
     * It very important that the decimals in it match the actual value of the token.
     */
    @NonNull
    private final TokenOperationAmount amount;

    @Override
    public UInt64 getBaseCost() {
        return UInt64.from(100);
    }

    @Override
    public String getType() {
        return TYPE;
    }

    public static final String TYPE = "mint";
}
