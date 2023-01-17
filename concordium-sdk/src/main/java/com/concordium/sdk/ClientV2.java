package com.concordium.sdk;

import com.concordium.grpc.v2.QueriesGrpc;
import com.concordium.sdk.exceptions.ClientInitializationException;
import com.concordium.sdk.requests.BlockHashInput;
import com.concordium.sdk.responses.blocksummary.updates.queues.AnonymityRevokerInfo;
import io.grpc.ManagedChannel;
import lombok.var;

import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import static com.concordium.sdk.ClientV2MapperExtensions.to;

/**
 * The Client is responsible for sending requests to the node.
 */
public final class ClientV2 {

    /**
     * The grpc blocking stub.
     * <a href="https://grpc.io/docs/languages/java/basics/#instantiating-a-stub">Read more</a>
     */
    private final QueriesGrpc.QueriesBlockingStub blockingStub;

    /**
     * The underlying grpc channel
     */
    private final ManagedChannel channel;

    /**
     * The timeout in milliseconds for each gRPC request.
     */
    private final int timeout;

    public static ClientV2 from(final Connection connection) throws ClientInitializationException {
        return new ClientV2(connection);
    }

    private ClientV2(final Connection connection) throws ClientInitializationException {
        try {
            this.timeout = connection.getTimeout();
            this.channel = connection.newChannel();
            this.blockingStub = QueriesGrpc
                    .newBlockingStub(this.channel)
                    .withCallCredentials(connection.getCredentials());
        } catch (IOException e) {
            throw ClientInitializationException.from(e);
        }
    }

    /**
     * Gets all the Anonymity Revokers at the end of the block pointed by {@link BlockHashInput}.
     *
     * @param input Pointer to the Block.
     * @return {@link Iterator} of {@link AnonymityRevokerInfo}
     */
    public Iterator<AnonymityRevokerInfo> getAnonymityRevokers(final BlockHashInput input) {
        var grpcOutput = this.server().getAnonymityRevokers(to(input));

        return to(grpcOutput, ClientV2MapperExtensions::to);
    }

    /**
     * Closes the underlying grpc channel
     * <p>
     * This should only be done when the {@link ClientV2}
     * is of no more use as creating a new {@link ClientV2} (and the associated)
     * channel is rather expensive.
     * <p>
     * Subsequent calls following a closed channel will throw a {@link io.grpc.StatusRuntimeException}
     */
    public void close() {
        this.channel.shutdown();
    }

    /**
     * Get a {@link QueriesGrpc.QueriesBlockingStub} with a timeout
     * The timeout is the one specified in via the {@link Connection} object used to
     * initialize `this`.
     *
     * @return A new stub with a timeout.
     */
    private QueriesGrpc.QueriesBlockingStub server() {
        return this.blockingStub.withDeadlineAfter(this.timeout, TimeUnit.MILLISECONDS);
    }
}
