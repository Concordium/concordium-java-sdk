package com.concordium.sdk.transactions.tokens;

import com.concordium.sdk.types.UInt64;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.jackson.Jacksonized;

import java.util.Optional;

@Getter
@Builder
@EqualsAndHashCode
// This is for CBOR serialization to use the generated builder.
@Jacksonized
public class TransferTokenOperation implements TokenOperation {

    /**
     * Amount to be transferred.
     * It very important that the decimals in it match the actual value of the token.
     */
    @NonNull
    private final TokenOperationAmount amount;

    /**
     * Recipient of the transfer.
     */
    @NonNull
    private final TaggedTokenHolderAccount recipient;

    /**
     * Optional memo (message) to be included to the transfer,
     * which will be <b>publicly available</b> on the blockchain.
     */
    private final CborMemo memo;

    public Optional<CborMemo> getMemo() {
        return Optional.ofNullable(memo);
    }

    @Override
    public UInt64 getBaseCost() {
        return UInt64.from(100);
    }

    @Override
    public String getType() {
        return TYPE;
    }

    public static final String TYPE = "transfer";
}
