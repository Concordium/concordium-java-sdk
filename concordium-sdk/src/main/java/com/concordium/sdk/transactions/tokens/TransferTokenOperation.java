package com.concordium.sdk.transactions.tokens;

import com.concordium.sdk.types.UInt64;
import lombok.Builder;
import lombok.Getter;
import lombok.val;

import java.util.HashMap;
import java.util.Optional;

@Getter
@Builder
public class TransferTokenOperation implements TokenOperation {

    /**
     * Amount to be transferred.
     * It very important that the decimals in it match the actual value of the token.
     */
    private final TokenOperationAmount amount;

    /**
     * Recipient of the transfer.
     */
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
    public String getType() {
        return "transfer";
    }

    @Override
    public UInt64 getBaseCost() {
        return UInt64.from(100);
    }

    @Override
    public Object getBody() {
        val body = new HashMap<String, Object>();
        body.put("amount", amount);
        body.put("recipient", recipient);
        body.put("memo", memo);
        return body;
    }
}
