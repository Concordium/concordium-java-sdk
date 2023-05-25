package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.responses.AccountIndex;
import com.concordium.sdk.transactions.TransactionType;
import com.concordium.sdk.types.UInt16;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.extern.jackson.Jacksonized;

import java.nio.ByteBuffer;

/**
 * Target of delegation.
 */
@Data
@Jacksonized
@Builder
@EqualsAndHashCode
public class DelegationTarget {
    /**
     * Type of delegation
     */
    @JsonProperty("delegateType")
    private final DelegationType type;
    /**
     * Specific baker id to delegate.
     */
    @JsonProperty("bakerId")
    private final AccountIndex bakerId;

    private DelegationTarget(DelegationType type, AccountIndex bakerId) {
        this.type = type;
        this.bakerId = bakerId;
    }

    public static DelegationTarget newPassiveDelegationTarget(){
        return new DelegationTarget(DelegationType.PASSIVE, null);
    }

    public static DelegationTarget newBakerDelegationTarget(AccountIndex bakerId){
        return new DelegationTarget(DelegationType.BAKER, bakerId);
    }

    /**
     * Parses {@link com.concordium.grpc.v2.DelegationTarget} to {@link DelegationTarget}.
     * @param delegationTarget {@link com.concordium.grpc.v2.DelegationTarget} returned by the GRPC V2 API.
     * @return parsed {@link DelegationTarget}.
     */
    public static DelegationTarget parse(com.concordium.grpc.v2.DelegationTarget delegationTarget) {
        switch (delegationTarget.getTargetCase()) {
            case PASSIVE: return newPassiveDelegationTarget();
            case BAKER: return newBakerDelegationTarget(AccountIndex.from(delegationTarget.getBaker().getValue()));
            default: throw new IllegalArgumentException();
        }
    }

    public byte[] getBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(TransactionType.BYTES);
        if(this.type == DelegationType.PASSIVE) {
            buffer.put((byte) 0);
        }
        else if(type == DelegationType.BAKER) {
            buffer = ByteBuffer.allocate(TransactionType.BYTES + UInt16.BYTES);
            buffer.put((byte) 1);
            buffer.put(this.bakerId.getBytes());
        }
        return buffer.array();
    }

    @ToString
    public enum DelegationType {
        /**
         * Delegate passively, i.e., to no specific baker.
         */
        @JsonProperty("Passive")
        PASSIVE,
        /**
         * Delegate to a specific baker.
         */
        @JsonProperty("Baker")
        BAKER
    }

}
