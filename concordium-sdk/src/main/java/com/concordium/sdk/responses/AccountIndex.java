package com.concordium.sdk.responses;

import com.concordium.grpc.v2.DelegatorId;
import com.concordium.sdk.types.UInt64;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Account index
 * A sequential index unique for each account.
 * <p>
 * If the account is registered as a Baker or as a Delegator then
 * the baker id and vice versa the delegator id corresponds to the underlying account index.
 */
@Getter
@EqualsAndHashCode
public final class AccountIndex implements ID{

    /**
     * The account index
     */
    private final UInt64 index;

    AccountIndex(UInt64 index) {
        this.index = index;
    }

    public static AccountIndex from(long index) {
        return new AccountIndex(UInt64.from(index));
    }

    public static AccountIndex from(DelegatorId delegatorId) {
        return AccountIndex.from(delegatorId.getId().getValue());
    }

    public byte[] getBytes() {
        return this.index.getBytes();
    }

    @Override
    public String toString() {
        return index.toString();
    }

    @Override
    public long getId() {
        return this.index.getValue();
    }
}
