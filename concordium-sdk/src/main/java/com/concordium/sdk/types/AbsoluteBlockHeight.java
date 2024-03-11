package com.concordium.sdk.types;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * The absolute height of a block. This is the number of ancestors of a block since the genesis block. In particular, the chain genesis block has absolute height 0.
 */
@ToString
@EqualsAndHashCode
@Getter
@AllArgsConstructor
public class AbsoluteBlockHeight {
    private UInt64 height;

    public static AbsoluteBlockHeight from(UInt64 height) {
        return new AbsoluteBlockHeight(height);
    }
    public static AbsoluteBlockHeight from(long height) {
        return from(UInt64.from(height));
    }

}
