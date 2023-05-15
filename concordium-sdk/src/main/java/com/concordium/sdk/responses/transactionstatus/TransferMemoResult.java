package com.concordium.sdk.responses.transactionstatus;

import com.concordium.grpc.v2.Memo;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.codec.binary.Hex;

@Getter
@ToString
public final class TransferMemoResult extends TransactionResultEvent {
    private final String memo;

    @JsonCreator
    TransferMemoResult(@JsonProperty("memo") String memo) {
        this.memo = memo;
    }

    /**
     * Parses {@link Memo} to {@link TransferMemoResult}.
     * @param memo {@link Memo} returned by the GRPC V2 API.
     * @return parsed {@link TransferMemoResult}.
     */
    public static TransferMemoResult parse(Memo memo) {
        return new TransferMemoResult(Hex.encodeHexString(memo.getValue().toByteArray()));
    }

    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.TRANSFER_MEMO;
    }
}
