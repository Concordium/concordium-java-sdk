package com.concordium.sdk.responses.transactionstatus;

import com.concordium.grpc.v2.NewRelease;
import com.concordium.sdk.transactions.CCDAmount;
import com.concordium.sdk.types.Timestamp;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;

/**
 * A CCD release as a result of a {@link TransferredWithScheduleResult}
 */
@EqualsAndHashCode
@Getter
@Builder
@ToString
@JsonFormat(shape = JsonFormat.Shape.ARRAY)
@JsonPropertyOrder({"timestamp", "amount"})
public class TransferRelease {
    /**
     * Amount scheduled for the release.
     */
    private final CCDAmount amount;

    /**
     * Time that the amount is released.
     */
    private final Timestamp timestamp;

    public static TransferRelease from(NewRelease release) {
        return TransferRelease
                .builder()
                .amount(CCDAmount.from(release.getAmount()))
                .timestamp(Timestamp.from(release.getTimestamp()))
                .build();
    }

    // used for json deserializing of the tuples returned by the V1 api.
    public static TransferRelease from(Map<Long, String> release) {
        Long ts = release.keySet().iterator().next();
        return TransferRelease
                .builder()
                .timestamp(Timestamp.newMillis(ts))
                .amount(CCDAmount.fromMicro(release.get(ts)))
                .build();
    }
}
