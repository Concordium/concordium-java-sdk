package com.concordium.sdk.responses.birkparamsters;

import com.concordium.sdk.transactions.AccountAddress;
import com.concordium.sdk.types.UInt64;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

/**
 * State of an individual baker.
 */
@Data
@Getter
@ToString
public class Baker {

    /**
     * ID of the baker. Matches their account index.
     */
    public UInt64 bakerId;

    /**
     * The lottery power of the baker. This is the baker's stake relative to the total staked amount.
     */
    public double bakerLotteryPower;

    /**
     * Address of the account this baker is associated with.
     */
    public AccountAddress bakerAccount;
}
