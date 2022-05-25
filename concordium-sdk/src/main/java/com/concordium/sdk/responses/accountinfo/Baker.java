package com.concordium.sdk.responses.accountinfo;

import com.concordium.sdk.responses.AccountIndex;
import com.concordium.sdk.responses.transactionstatus.ModuleCreatedResult;
import com.concordium.sdk.transactions.CCDAmount;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.ToString;
import org.apache.commons.codec.binary.Hex;

@Getter
@ToString
@EqualsAndHashCode
public final class Baker {
    /**
     * The baker id.
     */
    private final AccountIndex bakerId;
    /**
     * The staked amount.
     */
    private final CCDAmount stakedAmount;
    /**
     * Whether earnings should be restaked automatically or not.
     */
    private final boolean restakeEarnings;
    /**
     * The baker's public VRF key used to verify that the baker has won the lottery.
     */
    private final byte[] bakerElectionVerifyKey;
    /**
     * The baker's public key, used to verify baker's signatures on the blocks and finalization messages.
     */
    private final byte[] bakerSignatureVerifyKey;
    /**
     * The baker's public key used to verify the baker's signature on finalization records in case the baker is a finalizer.
     */
    private final byte[] bakerAggregationVerifyKey;

    /**
     * The pending changes for the baker.
     */
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "change")
    @JsonSubTypes({
            @JsonSubTypes.Type(value = ReduceStakeChange.class, name = "ReduceStake"),
            @JsonSubTypes.Type(value = RemoveStakeChange.class, name = "RemoveStake")
    })
    private final PendingChange pendingChange;


    /**
     * The baker pool info
     */
    private final BakerPoolInfo bakerPoolInfo;

    @SneakyThrows
    @JsonCreator
    Baker(@JsonProperty("bakerId") AccountIndex bakerId,
          @JsonProperty("stakedAmount") CCDAmount stakedAmount,
          @JsonProperty("restakeEarnings") boolean restakeEarnings,
          @JsonProperty("bakerElectionVerifyKey") String bakerElectionVerifyKey,
          @JsonProperty("bakerSignatureVerifyKey") String bakerSignatureVerifyKey,
          @JsonProperty("bakerAggregationVerifyKey") String bakerAggregationVerifyKey,
          @JsonProperty("bakerPoolInfo") BakerPoolInfo bakerPoolInfo,
          @JsonProperty("pendingChange") PendingChange pendingChange) {
        this.bakerId = bakerId;
        this.stakedAmount = stakedAmount;
        this.restakeEarnings = restakeEarnings;
        this.bakerElectionVerifyKey = Hex.decodeHex(bakerElectionVerifyKey);
        this.bakerSignatureVerifyKey = Hex.decodeHex(bakerSignatureVerifyKey);
        this.bakerAggregationVerifyKey = Hex.decodeHex(bakerAggregationVerifyKey);
        this.bakerPoolInfo = bakerPoolInfo;
        this.pendingChange = pendingChange;
    }
}
