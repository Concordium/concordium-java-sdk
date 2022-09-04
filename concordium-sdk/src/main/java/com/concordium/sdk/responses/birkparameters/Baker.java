package com.concordium.sdk.responses.birkparameters;

import com.concordium.sdk.transactions.AccountAddress;
import com.concordium.sdk.types.UInt64;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
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


    @JsonCreator
    public Baker(
            @JsonProperty("bakerId") UInt64 bakerId,
            @JsonProperty("bakerLotteryPower") double bakerLotteryPower,
            @JsonProperty("bakerAccount") AccountAddress bakerAccount
    ) {
        this.bakerId = bakerId;
        this.bakerLotteryPower = bakerLotteryPower;
        this.bakerAccount = bakerAccount;
    }
}
