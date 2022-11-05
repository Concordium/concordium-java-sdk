package com.concordium.sdk.transactions;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.val;

import java.nio.ByteBuffer;

@Getter
@ToString
@EqualsAndHashCode
public class ContractAddress extends com.concordium.sdk.responses.transactionstatus.ContractAddress {

    public ContractAddress(int subIndex, int index) {
        super(subIndex, index);
    }

    public static ContractAddress from(int index, int subIndex) {
        return new ContractAddress(index, subIndex);
    }

    public byte[] getBytes() {
        val buffer = ByteBuffer.allocate(Long.BYTES + Long.BYTES );
        buffer.putLong(this.getIndex());
        buffer.putLong(this.getSubIndex());
        return buffer.array();
    }
}
