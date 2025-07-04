package com.concordium.sdk.transactions.tokens;

import com.concordium.grpc.v2.plt.TokenAmount;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;
import lombok.Getter;

import java.util.Optional;

@Builder
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransferTokenOperation {

    /**
     * Amount to be transferred.
     * It very important that the decimals match the actual ones of the token.
     */
    @JsonProperty("amount")
    @JsonSerialize(using = TokenAmountCborSerializer.class)
    private final TokenAmount amount;

    /**
     * Recipient of the transfer.
     */
    @JsonProperty("recipient")
    private final TaggedTokenHolderAccount recipient;

    /**
     * Optional memo (message) to be included to the transfer,
     * which will be <b>publicly available</b> on the blockchain.
     */
    @JsonProperty("memo")
    private final CborMemo memo;

    public Optional<CborMemo> getMemo() {
        return Optional.ofNullable(memo);
    }
}
