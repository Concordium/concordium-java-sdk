package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.transactions.CCDAmount;
import com.concordium.sdk.types.Timestamp;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.*;

import java.util.List;

/**
 * A new individual release. Part of a single 'transfer with schedule' transaction.
 */
@Builder
@AllArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class NewRelease {

    private final Timestamp timestamp;

    private final CCDAmount amount;

    @JsonCreator
    NewRelease(List<String> tuple){
        this.timestamp = Timestamp.newMillis(Long.parseLong(tuple.get(0)));
        this.amount = CCDAmount.fromMicro(tuple.get(1));
    }

    /**
     * Parses {@link com.concordium.grpc.v2.NewRelease} to {@link NewRelease}.
     * @param newRelease {@link com.concordium.grpc.v2.NewRelease} returned by the GRPC V2 API.
     * @return parsed {@link NewRelease}.
     */
    public static NewRelease parse(com.concordium.grpc.v2.NewRelease newRelease) {
        return NewRelease.builder()
                .timestamp(Timestamp.newMillis(newRelease.getTimestamp().getValue()))
                .amount(CCDAmount.fromMicro(newRelease.getAmount().getValue()))
                .build();
    }
}
