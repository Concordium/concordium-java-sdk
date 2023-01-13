package com.concordium.sdk.v2;

import com.concordium.grpc.v2.QueriesGrpc;
import com.concordium.sdk.Connection;
import com.concordium.sdk.exceptions.ClientInitializationException;
import com.concordium.sdk.v2.types.ArInfo;
import com.concordium.sdk.v2.types.BlockHashInput;
import com.concordium.sdk.v2.types.MapperExtensions;
import com.google.common.collect.Iterators;
import io.grpc.ManagedChannel;
import io.grpc.stub.StreamObserver;
import lombok.var;

import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import static com.concordium.sdk.v2.types.MapperExtensions.to;

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
     * The grpc non-blocking stub.
     * <a href="https://grpc.io/docs/languages/java/basics/#instantiating-a-stub">Read more</a>
     */
    private final QueriesGrpc.QueriesStub stub;

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

    private ClientV2(Connection connection) throws ClientInitializationException {
        try {
            this.timeout = connection.getTimeout();
            this.channel = connection.newChannel();
            this.blockingStub = QueriesGrpc
                    .newBlockingStub(this.channel)
                    .withCallCredentials(connection.getCredentials());
            this.stub = QueriesGrpc
                    .newStub(this.channel)
                    .withCallCredentials(connection.getCredentials());
        } catch (IOException e) {
            throw ClientInitializationException.from(e);
        }
    }

    public Iterator<ArInfo> getAnonymityRevokers(BlockHashInput input) {
        var res = this.server().getAnonymityRevokers(to(input));

        return Iterators.transform(res, MapperExtensions::to);
    }

    public void getAnonymityRevokers(BlockHashInput input, StreamObserver<ArInfo> streamObserver) {
        var grpcStreamObserver = Utils.<com.concordium.grpc.v2.ArInfo, ArInfo>to(
                streamObserver,
                MapperExtensions::to);

        this.serverAsync().getAnonymityRevokers(to(input), grpcStreamObserver);
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

    /**
     * Get a {@link QueriesGrpc.QueriesStub} with a timeout
     * The timeout is the one specified in via the {@link Connection} object used to
     * initialize `this`.
     *
     * @return A new stub with a timeout.
     */
    private QueriesGrpc.QueriesStub serverAsync() {
        return this.stub.withDeadlineAfter(this.timeout, TimeUnit.MILLISECONDS);
    }
}
