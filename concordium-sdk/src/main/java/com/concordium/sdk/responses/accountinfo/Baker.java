package com.concordium.sdk.responses.accountinfo;

import com.concordium.sdk.crypto.ed25519.ED25519PublicKey;
import com.concordium.sdk.responses.AccountIndex;
import com.concordium.sdk.responses.BakerId;
import com.concordium.sdk.transactions.CCDAmount;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

@Getter
@ToString
@EqualsAndHashCode
@Jacksonized
@Builder
public final class Baker {

    /**
     * The baker id.
     */
    @JsonProperty("bakerId")
    private final BakerId bakerId;

    /**
     * The staked amount.
     */
    @JsonProperty("stakedAmount")
    private final CCDAmount stakedAmount;

    /**
     * Whether earnings should be restaked automatically or not.
     */
    private final boolean restakeEarnings;

    /**
     * The baker's public VRF key used to verify that the baker has won the lottery.
     */
    @JsonProperty("bakerElectionVerifyKey")
    private final ED25519PublicKey bakerElectionVerifyKey;

    /**
     * The baker's public key, used to verify baker's signatures on the blocks and finalization messages.
     */
    @JsonProperty("bakerSignatureVerifyKey")
    private final ED25519PublicKey bakerSignatureVerifyKey;

    /**
     * The baker's public key used to verify the baker's signature on finalization records in case the baker is a finalizer.
     */
    @JsonProperty("bakerAggregationVerifyKey")
    private final ED25519PublicKey bakerAggregationVerifyKey;

    /**
     * The pending changes for the baker.
     */
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "change")
    @JsonSubTypes({
            @JsonSubTypes.Type(value = ReduceStakeChange.class, name = "ReduceStake"),
            @JsonSubTypes.Type(value = RemoveStakeChange.class, name = "RemoveStake")
    })
    @JsonProperty("pendingChange")
    private final PendingChange pendingChange;


    /**
     * The baker pool info
     */
    @JsonProperty("bakerPoolInfo")
    private final BakerPoolInfo bakerPoolInfo;


}
