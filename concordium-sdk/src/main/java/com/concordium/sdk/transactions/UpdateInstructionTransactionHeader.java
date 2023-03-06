package com.concordium.sdk.transactions;

import com.concordium.sdk.types.UInt64;
import io.grpc.netty.shaded.io.netty.util.internal.UnstableApi;
import lombok.Builder;
import lombok.Getter;
import lombok.var;

import java.nio.ByteBuffer;

@Builder
@Getter
public class UpdateInstructionTransactionHeader {
    private final UInt64 effectiveTime;
    private final UInt64 timeout;
    private final UInt64 sequenceNumber;

    @UnstableApi
    byte[] getBytes() {
        var buff = ByteBuffer.allocate(3 * UInt64.BYTES);
        buff.put(effectiveTime.getBytes());
        buff.put(timeout.getBytes());
        buff.put(sequenceNumber.getBytes());

        return buff.array();
    }
}
