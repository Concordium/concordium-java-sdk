package com.concordium.sdk.responses.transactionstatus;

import com.concordium.grpc.v2.NewRelease;
import com.concordium.sdk.transactions.CCDAmount;
import com.concordium.sdk.types.Timestamp;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * A CCD release as a result of a {@link TransferredWithScheduleResult}
 */
@EqualsAndHashCode
@Getter
@Builder
@ToString
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
}
