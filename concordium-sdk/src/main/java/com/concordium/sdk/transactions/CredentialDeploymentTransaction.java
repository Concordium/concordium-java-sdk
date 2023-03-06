package com.concordium.sdk.transactions;

import com.concordium.sdk.types.UInt64;
import io.grpc.netty.shaded.io.netty.util.internal.UnstableApi;
import lombok.Builder;
import lombok.Getter;
import lombok.var;

import java.nio.ByteBuffer;

@Getter
public class CredentialDeploymentTransaction extends BlockItem {
    private final Expiry expiry;
    private final byte[] payloadBytes;

    private CredentialDeploymentTransaction(final Expiry expiry, final byte[] payloadBytes) {
        super(BlockItemType.CREDENTIAL_DEPLOYMENT);
        this.expiry = expiry;
        this.payloadBytes = payloadBytes;
    }

    @Builder(builderMethodName = "builderBlockItem")
    public static CredentialDeploymentTransaction from(final UInt64 expiry, final byte[] payloadBytes) {
        return new CredentialDeploymentTransaction(Expiry.from(expiry.getValue()), payloadBytes);
    }

    @Override
    @UnstableApi
    byte[] getBlockItemBytes() {
        var buff = ByteBuffer.allocate(Expiry.BYTES + payloadBytes.length);
        buff.put(expiry.getValue().getBytes());
        buff.put(payloadBytes);

        return buff.array();
    }
}
