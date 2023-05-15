package com.concordium.sdk.responses.transactionevent;

import com.concordium.sdk.responses.transactionevent.updatepayloads.UpdatePayload;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.Instant;
import java.time.ZonedDateTime;

import static com.concordium.sdk.Constants.UTC_ZONE;

/**
 * Details about a chain update.
 */
@Builder
@EqualsAndHashCode
@ToString
@Getter
public class UpdateDetails {

    /**
     * The time at which the update will be effective.
     */
    private ZonedDateTime effectiveTime;

    /**
     * The payload for the update.
     */
    private UpdatePayload payload;

    /**
     * Parses {@link com.concordium.grpc.v2.UpdateDetails} to {@link AccountCreationDetails}
     * @param updateDetails {@link com.concordium.grpc.v2.UpdateDetails} returned from the GRPC V2 API
     * @return parsed {@link UpdateDetails}
     */
    public static UpdateDetails parse(com.concordium.grpc.v2.UpdateDetails updateDetails) {
        return UpdateDetails.builder()
                .effectiveTime(Instant.EPOCH.plusSeconds(updateDetails.getEffectiveTime().getValue()).atZone(UTC_ZONE))
                .payload(UpdatePayload.parse(updateDetails.getPayload()))
                .build();
    }
}
