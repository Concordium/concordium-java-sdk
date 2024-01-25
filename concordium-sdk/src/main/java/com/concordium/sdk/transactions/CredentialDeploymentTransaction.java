package com.concordium.sdk.transactions;

import com.concordium.sdk.types.UInt64;
import io.grpc.netty.shaded.io.netty.util.internal.UnstableApi;
import lombok.*;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * {@link CredentialDeploymentTransaction} used to deploy new account credentials on chain.
 * This type of transaction is not paid by the sender and bakers are compensated by the protocol.
 */
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CredentialDeploymentTransaction extends BlockItem {

    /**
     * Indicates when the transaction should expire.
     */
    private final Expiry expiry;

    /**
     * Serialized payload byte array of this transaction.
     */
    private final byte[] payloadBytes;

    /**
     * @param expiry       Indicates when this transaction should expire.
     * @param payloadBytes Payload serialized as byte array.
     */
    private CredentialDeploymentTransaction(final Expiry expiry, final byte[] payloadBytes) {
        super(BlockItemType.CREDENTIAL_DEPLOYMENT);
        this.expiry = expiry;
        this.payloadBytes = payloadBytes;
    }

    /**
     * Creates an instance of {@link CredentialDeploymentTransaction}.
     *
     * @param expiry       Indicates when this transaction should expire.
     * @param payloadBytes Payload serialized as byte array.
     * @return Instance of {@link CredentialDeploymentTransaction}.
     */
    @Builder(builderMethodName = "builderBlockItem")
    public static CredentialDeploymentTransaction from(final UInt64 expiry, final byte[] payloadBytes) {
        return new CredentialDeploymentTransaction(
                Expiry.from(expiry.getValue()),
                Arrays.copyOf(payloadBytes, payloadBytes.length));
    }

    /**
     * Creates an instance of {@link CredentialDeploymentTransaction}.
     *
     * @param expiry       Indicates when this transaction should expire.
     * @param payloadBytes Payload serialized as byte array.
     * @return Instance of {@link CredentialDeploymentTransaction}.
     */
    public static CredentialDeploymentTransaction from(final Expiry expiry, final byte[] payloadBytes) {
        return new CredentialDeploymentTransaction(
                expiry,
                Arrays.copyOf(payloadBytes, payloadBytes.length));
    }

    @Override
    @UnstableApi
    byte[] getBlockItemBytes() {
        val buff = ByteBuffer.allocate(Expiry.BYTES + payloadBytes.length);
        buff.put(expiry.getValue().getBytes());
        buff.put(payloadBytes);

        return buff.array();
    }
}
