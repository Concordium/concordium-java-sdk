package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.responses.BakerId;
import com.concordium.sdk.transactions.TransactionType;
import com.concordium.sdk.types.UInt16;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

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
        ByteBuffer buffer = ByteBuffer.allocate(TransactionType.BYTES);
        if (this.type == DelegationType.PASSIVE) {
            buffer.put((byte) 0);
        } else if (type == DelegationType.BAKER) {
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
