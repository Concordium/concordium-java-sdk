package com.concordium.sdk.responses.birkparamsters;

import com.concordium.sdk.responses.AccountIndex;
import com.concordium.sdk.types.AccountAddress;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

/**
 * State of an individual baker.
 */
@Data
@Jacksonized
@Builder
public class Baker {

    /**
     * ID of the baker. Matches their account index.
     */
    private final AccountIndex bakerId;

    /**
     * The lottery power of the baker. This is the baker's stake relative to the total staked amount.
     */
    private final double bakerLotteryPower;

    /**
     * Address of the account this baker is associated with.
     */
    private final AccountAddress bakerAccount;
}
