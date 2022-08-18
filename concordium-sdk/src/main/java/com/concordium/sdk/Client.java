package com.concordium.sdk;

import com.concordium.sdk.exceptions.*;
import com.concordium.sdk.responses.AccountIndex;
import com.concordium.sdk.responses.peerlist.Peer;
import com.concordium.sdk.responses.peerStats.PeerStatistics;
import com.concordium.sdk.responses.blocksatheight.BlocksAtHeight;
import com.concordium.sdk.exceptions.AccountNotFoundException;
import com.concordium.sdk.exceptions.BlockNotFoundException;
import com.concordium.sdk.exceptions.TransactionNotFoundException;
import com.concordium.sdk.exceptions.TransactionRejectionException;
import com.concordium.sdk.responses.accountinfo.AccountInfo;
import com.concordium.sdk.responses.blockinfo.BlockInfo;
import com.concordium.sdk.responses.blocksatheight.BlocksAtHeightRequest;
import com.concordium.sdk.responses.blocksummary.BlockSummary;
import com.concordium.sdk.responses.consensusstatus.ConsensusStatus;
import com.concordium.sdk.responses.cryptographicparameters.CryptographicParameters;
import com.concordium.sdk.responses.transactionstatus.TransactionStatus;
import com.concordium.sdk.transactions.*;
import com.google.common.collect.ImmutableList;
import com.google.protobuf.ByteString;
import concordium.ConcordiumP2PRpc;
import concordium.P2PGrpc;
import io.grpc.ManagedChannel;
import lombok.val;

import java.io.IOException;
import java.net.UnknownHostException;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * The Client is responsible for sending requests to the node.
 */
public final class Client {

    /**
     * The grpc stub
     */
    private final P2PGrpc.P2PBlockingStub blockingStub;
    /**
     * The underlying grpc channel
     */
    private final ManagedChannel channel;

    /**
     * The timeout in milliseconds for each gRPC request.
     */
    private final int timeout;

    /**
     * Convenient way of creating a new {@link Client} based on the {@link Connection}
     *
     * @param connection the connection which this client should use.
     * @return A new {@link Client}
     * @throws ClientInitializationException if the {@link Client} could not be constructed.
     */
    public static Client from(Connection connection) throws ClientInitializationException {
        return new Client(connection);
    }

    private Client(Connection connection) throws ClientInitializationException {
        try {
            this.timeout = connection.getTimeout();
            this.channel = connection.newChannel();
            this.blockingStub = P2PGrpc
                    .newBlockingStub(this.channel)
                    .withCallCredentials(connection.getCredentials());
        } catch (IOException e) {
            throw ClientInitializationException.from(e);
        }

    }

    /**
     * Retrieves the {@link AccountInfo} based on the address {@link Hash} and the block {@link Hash}
     *
     * @param accountRequest The {@link AccountRequest}
     *                       See {@link AccountRequest#from(AccountAddress)},
     *                       {@link AccountRequest#from(AccountIndex)}
     * @param blockHash the block hash
     * @return The {@link AccountInfo}
     * @throws AccountNotFoundException if the account was not found.
     */
    public AccountInfo getAccountInfo(AccountRequest accountRequest, Hash blockHash) throws AccountNotFoundException {
        val request = ConcordiumP2PRpc.GetAddressInfoRequest
                .newBuilder()
                .setAddressBytes(accountRequest.getByteString())
                .setBlockHash(blockHash.asHex())
                .build();
        val response = server().getAccountInfo(request);
        val accountInfo = AccountInfo.fromJson(response.getValue());
        if (Objects.isNull(accountInfo)) {
            throw AccountNotFoundException.from(accountRequest, blockHash);
        }
        return accountInfo;
    }

    /**
     * Retrieves the next {@link AccountNonce} for an account.
     * This is the {@link AccountNonce} to use for future transactions
     * E.g. when using {@link Client#sendTransaction(Transaction)}
     *
     * @param address The {@link AccountAddress}
     * @return The next {@link AccountNonce}
     */
    public AccountNonce getNextAccountNonce(AccountAddress address) {
        val request = ConcordiumP2PRpc.AccountAddress
                .newBuilder()
                .setAccountAddressBytes(ByteString.copyFrom(address.getEncodedBytes()))
                .build();
        val nextAccountNonce = server().getNextAccountNonce(request);
        return AccountNonce.fromJson(nextAccountNonce.getValue());
    }

    /**
     * Retrieves the transaction status for a given transaction {@link Hash}
     *
     * @param transactionHash The transaction {@link Hash}
     * @return The {@link TransactionStatus}
     * @throws TransactionNotFoundException if the transaction was not found.
     */
    public TransactionStatus getTransactionStatus(Hash transactionHash) throws TransactionNotFoundException {
        val request = ConcordiumP2PRpc.TransactionHash
                .newBuilder()
                .setTransactionHash(transactionHash.asHex())
                .build();
        val transactionStatus = server().getTransactionStatus(request);
        val status = TransactionStatus.fromJson(transactionStatus.getValue());
        if (Objects.isNull(status)) {
            throw TransactionNotFoundException.from(transactionHash);
        }
        return status;
    }

    /**
     * Retrieves the {@link ConsensusStatus}
     * which yields information of the chain,
     * examples are the protocol version, slot duration, genesis block etc.
     *
     * @return the {@link ConsensusStatus}
     */
    public ConsensusStatus getConsensusStatus() {
        val response = server().getConsensusStatus(ConcordiumP2PRpc.Empty.getDefaultInstance());
        return ConsensusStatus.fromJson(response.getValue());
    }

    /**
     * Retrieves a {@link BlockSummary}
     *
     * @param blockHash the block {@link Hash} to query.
     * @return A {@link BlockSummary} for the block
     * @throws BlockNotFoundException If the block was not found.
     */
    public BlockSummary getBlockSummary(Hash blockHash) throws BlockNotFoundException {
        val request = ConcordiumP2PRpc.BlockHash.getDefaultInstance()
                .newBuilderForType()
                .setBlockHashBytes(ByteString.copyFromUtf8(blockHash.asHex()))
                .build();
        val response = server().getBlockSummary(request);
        val blockSummary = BlockSummary.fromJson(response.getValue());
        if (Objects.isNull(blockSummary)) {
            throw BlockNotFoundException.from(blockHash);
        }
        return blockSummary;
    }

    /**
     * Retrieves a {@link BlockInfo}
     *
     * @param blockHash the block {@link Hash} to query.
     * @return A {@link BlockInfo} for the block
     * @throws BlockNotFoundException If the block was not found.
     */
    public BlockInfo getBlockInfo(Hash blockHash) throws BlockNotFoundException {
        val request = ConcordiumP2PRpc.BlockHash.getDefaultInstance()
                .newBuilderForType()
                .setBlockHashBytes(ByteString.copyFromUtf8(blockHash.asHex()))
                .build();
        val response = server().getBlockInfo(request);
        val blockInfo = BlockInfo.fromJson(response.getValue());
        if (Objects.isNull(blockInfo)) {
            throw BlockNotFoundException.from(blockHash);
        }
        return blockInfo;
    }

    /**
     * Retrieves a {@link BlocksAtHeight}
     *
     * @param height the {@link BlocksAtHeightRequest} request.
     * @return A {@link BlocksAtHeight} if one or more blocks was present at the given height.
     * @throws BlockNotFoundException if no blocks were present at the given height.
     */
    public BlocksAtHeight getBlocksAtHeight(BlocksAtHeightRequest height) throws BlockNotFoundException {
        val requestBuilder = ConcordiumP2PRpc.BlockHeight.getDefaultInstance()
                .newBuilderForType()
                .setBlockHeight(height.getHeight());
        if (height.getType() == BlocksAtHeightRequest.Type.RELATIVE) {
            requestBuilder.setFromGenesisIndex(height.getGenesisIndex());
            requestBuilder.setRestrictToGenesisIndex(height.isRestrictedToGenesisIndex());
        }
        val request = requestBuilder.build();
        val response = server().getBlocksAtHeight(request);
        val blocksAtHeight = BlocksAtHeight.fromJson(response.getValue());
        if (Objects.isNull(blocksAtHeight) || blocksAtHeight.getBlocks().isEmpty()) {
            throw BlockNotFoundException.from(height);
        }
        return blocksAtHeight;
    }

    /**
     * Sends a {@link Transaction} to the node.
     *
     * @param transaction the {@link Transaction} to be sent.
     * @return The transaction {@link Hash} of the transaction sent if the node accepted it,
     * otherwise a {@link TransactionRejectionException} will be thrown.
     * @throws TransactionRejectionException If the transaction was rejected by the node.
     */
    public Hash sendTransaction(Transaction transaction) throws TransactionRejectionException {
        val request = ConcordiumP2PRpc.SendTransactionRequest
                .newBuilder()
                .setNetworkId(transaction.getNetworkId())
                .setPayload(ByteString.copyFrom(transaction.getBytes()))
                .build();
        val response = server().sendTransaction(request);
        if (response.getValue()) {
            return transaction.getHash();
        }
        throw TransactionRejectionException.from(transaction);
    }

    /**
     * Get the {@link CryptographicParameters} at a given block.
     * @param blockHash the hash of the block
     * @return the cryptographic parameters at the given block.
     * @throws BlockNotFoundException if the block was not found.
     */
    public CryptographicParameters getCryptographicParameters(Hash blockHash) throws BlockNotFoundException {
        val request = ConcordiumP2PRpc.BlockHash.getDefaultInstance()
                .newBuilderForType()
                .setBlockHashBytes(ByteString.copyFromUtf8(blockHash.asHex()))
                .build();
        val response = server().getCryptographicParameters(request);
        val cryptographicParameters = CryptographicParameters.from(response.getValue());
        if (Objects.isNull(cryptographicParameters)) {
            throw BlockNotFoundException.from(blockHash);
        }
        return cryptographicParameters;
    }

    /**
     * Gets the Peer uptime.
     * @return Peer Uptime {@link Duration}.
     */
    public Duration getUptime() {
        val res = server().peerUptime(ConcordiumP2PRpc.Empty.newBuilder().build()).getValue();
        return Duration.ofMillis(res);
    }

    /**
     * Gets the total number of packets sent.
     * @return Total number of packets sent.
     */
    public long getTotalSent() {
        return server().peerTotalSent(ConcordiumP2PRpc.Empty.newBuilder().build()).getValue();
    }

    /**
     * Gets Peers list connected to the Node
     * @param includeBootstrappers if true will include Bootstrapper nodes in the response.
     * @return An {@link ImmutableList} of {@link Peer}
     * @throws UnknownHostException When the returned IP address of Peer is Invalid.
     */
    public ImmutableList<Peer> getPeerList(boolean includeBootstrappers) throws UnknownHostException {
        val value = server().peerList(ConcordiumP2PRpc.PeersRequest.newBuilder()
                    .setIncludeBootstrappers(includeBootstrappers)
                .build());
        val list = new ImmutableList.Builder<Peer>();

        for (ConcordiumP2PRpc.PeerElement p : value.getPeersList()) {
            list.add(Peer.parse(p));
        }

        return list.build();
    }

    /**
     * Gets {@link PeerStatistics} of the node.
     * @param includeBootstrappers Whether bootstrappers should be included in the response.
     * @return Peer Statistics in the format {@link PeerStatistics}
     */
    public PeerStatistics getPeerStatistics(final boolean includeBootstrappers) {
        val value = server()
                .peerStats(ConcordiumP2PRpc
                        .PeersRequest
                        .newBuilder()
                        .setIncludeBootstrappers(includeBootstrappers)
                        .build());
        return PeerStatistics.parse(value);
    }

    /**
     * Closes the underlying grpc channel
     * 
     * This should only be done when the {@link Client}
     * is of no more use as creating a new {@link Client} (and the associated)
     * channel is rather expensive.
     *
     * Subsequent calls following a closed channel will throw a {@link io.grpc.StatusRuntimeException}
     */
    public void close() {
        this.channel.shutdown();
    }

    /**
     * Get a {@link concordium.P2PGrpc.P2PBlockingStub} with a timeout
     * The timeout is the one specified in via the {@link Connection} object used to
     * initialize `this`.
     *
     * @return A new stub with a timeout.
     */
    private P2PGrpc.P2PBlockingStub server() {
        return this.blockingStub.withDeadlineAfter(this.timeout, TimeUnit.MILLISECONDS);
    }
}
