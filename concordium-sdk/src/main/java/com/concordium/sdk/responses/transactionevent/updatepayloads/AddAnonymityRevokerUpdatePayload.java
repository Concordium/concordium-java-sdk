package com.concordium.sdk.responses.transactionevent.updatepayloads;

import com.concordium.grpc.v2.ArInfo;
import com.concordium.sdk.responses.blocksummary.updates.queues.AnonymityRevokerInfo;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * An anonymity revoker was added.
 */
@Builder
@Getter
@EqualsAndHashCode
@ToString
public class AddAnonymityRevokerUpdatePayload implements UpdatePayload {


    /**
     * Info of the added anonymity revoker.
     */
    private AnonymityRevokerInfo anonymityRevokerInfo;



    /**
     * Parses {@link ArInfo} to {@link AddAnonymityRevokerUpdatePayload}.
     * @param arInfo {@link ArInfo} returned by the GRPC V2 API.
     * @return parsed {@link AddAnonymityRevokerUpdatePayload}.
     */
    public static AddAnonymityRevokerUpdatePayload parse(ArInfo arInfo) {
        return AddAnonymityRevokerUpdatePayload.builder()
                .anonymityRevokerInfo(AnonymityRevokerInfo.parse(arInfo))
                .build();
    }

    @Override
    public UpdateType getType() {
        return UpdateType.ADD_ANONYMITY_REVOKER_UPDATE;
    }
}
