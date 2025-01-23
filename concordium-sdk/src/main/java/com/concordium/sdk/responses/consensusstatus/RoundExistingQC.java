package com.concordium.sdk.responses.consensusstatus;

import com.concordium.sdk.responses.Epoch;
import com.concordium.sdk.responses.Round;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
@EqualsAndHashCode
public class RoundExistingQC {

    /**
     * The round for which a QC was seen.
     */
    private final Round round;

    /**
     * The epoch of the QC.
     */
    private final Epoch epoch;
}
