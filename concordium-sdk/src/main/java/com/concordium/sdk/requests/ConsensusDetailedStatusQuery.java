package com.concordium.sdk.requests;

import com.concordium.sdk.responses.GenesisIndex;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ConsensusDetailedStatusQuery {

    /**
     * If specified, this determines the genesis index to get status for.
     * If not specified, the status is returned for the latest genesis index.
     */
    @Builder.Default
    private final GenesisIndex genesisIndex = null;
}
