package com.concordium.sdk.responses;

import com.concordium.sdk.types.UInt16;
import com.concordium.sdk.types.UInt64;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.val;

import java.nio.ByteBuffer;

/**
 * Account index
 * A sequential index unique for each account.
 *
 * If the account is registered as a Baker or as a Delegator then
 * the baker id and vice versa the delegator id corresponds to the underlying account index.
 */
@ToString
@Getter
@EqualsAndHashCode
public final class AccountIndex {

    /**
     * The account index
     */
    private final UInt64 index;

    AccountIndex(UInt64 index) {
        this.index = index;
    }

    @JsonCreator
    AccountIndex(long index) {
        this.index = UInt64.from(index);
    }

    public static AccountIndex from(long index) {
        return new AccountIndex(UInt64.from(index));
    }

    public byte[] getBytes() {
        val buffer = ByteBuffer.allocate(UInt16.BYTES);
        buffer.put(index.getBytes());
        return buffer.array();
    }
}
