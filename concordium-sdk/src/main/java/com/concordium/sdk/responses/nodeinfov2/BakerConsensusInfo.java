package com.concordium.sdk.responses.nodeinfov2;

import com.concordium.sdk.responses.AccountIndex;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BakerConsensusInfo {

    private BakerConsensusStatus status;

    private AccountIndex bakerId;
}
