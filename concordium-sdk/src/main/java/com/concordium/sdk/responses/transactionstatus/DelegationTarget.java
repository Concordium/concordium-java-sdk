package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.responses.BakerId;
import com.concordium.sdk.transactions.TransactionType;
import com.concordium.sdk.types.UInt16;
import com.concordium.sdk.types.UInt64;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.nio.ByteBuffer;

/**
 * Target of delegation.
 */
@Data
@Builder
@EqualsAndHashCode
public class DelegationTarget {
    /**
     * Type of delegation
     */
    private final DelegationType type;
    /**
     * Specific baker id to delegate.
     */
    private final BakerId bakerId;

    private DelegationTarget(DelegationType type, BakerId bakerId) {
        this.type = type;
        this.bakerId = bakerId;
    }

    public static DelegationTarget newPassiveDelegationTarget() {
        return new DelegationTarget(DelegationType.PASSIVE, null);
    }

    public static DelegationTarget newBakerDelegationTarget(BakerId bakerId) {
        return new DelegationTarget(DelegationType.BAKER, bakerId);
    }

    public static DelegationTarget from(com.concordium.grpc.v2.DelegationTarget delegationTarget) {
        switch (delegationTarget.getTargetCase()) {
            case PASSIVE:
                return DelegationTarget.newPassiveDelegationTarget();
            case BAKER:
                return DelegationTarget.newBakerDelegationTarget(BakerId.from(delegationTarget.getBaker()));
            case TARGET_NOT_SET:
                throw new IllegalArgumentException("Delegation target was not set.");
        }
        throw new IllegalArgumentException("Unrecognized delegation target type.");
    }

    public byte[] getBytes() {
        if (this.type == DelegationType.PASSIVE) {
            return new byte[]{0}; // tag for passive delegation
        } else if (type == DelegationType.BAKER) {
            val buffer = ByteBuffer.allocate(1 + UInt64.BYTES); // baker tag + size of a baker id.
            buffer.put((byte) 1);
            buffer.put(this.bakerId.getBytes());
            return buffer.array();
        }
        throw new IllegalArgumentException("Illegal DelegationType. Must be either PASSIVE or BAKER");
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
