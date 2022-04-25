package com.concordium.sdk;

import com.concordium.sdk.exceptions.AccountNotFoundException;
import com.concordium.sdk.exceptions.BlockNotFoundException;
import com.concordium.sdk.exceptions.TransactionNotFoundException;
import com.concordium.sdk.exceptions.TransactionRejectionException;
import com.concordium.sdk.responses.BlocksAtHeight;
import com.concordium.sdk.responses.accountinfo.AccountInfo;
import com.concordium.sdk.responses.blockinfo.BlockInfo;
import com.concordium.sdk.responses.blocksummary.BlockSummary;
import com.concordium.sdk.responses.consensusstatus.ConsensusStatus;
import com.concordium.sdk.responses.transactionstatus.TransactionStatus;
import com.concordium.sdk.transactions.AccountAddress;
import com.concordium.sdk.transactions.AccountNonce;
import com.concordium.sdk.transactions.Hash;
import com.concordium.sdk.transactions.Transaction;
import com.google.protobuf.ByteString;
import concordium.ConcordiumP2PRpc;
import concordium.P2PGrpc;
import io.grpc.Deadline;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.val;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.concordium.sdk.transactions.Transaction.DEFAULT_NETWORK_ID;

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
     * Convenient way of creating a new {@link Client} based on the {@link Connection}
     *
     * @param connection the connection which this client should use.
     * @return A new {@link Client}
     */
    public static Client from(Connection connection) {
        return new Client(connection);
    }

    private Client(Connection connection) {
        this(connection, ManagedChannelBuilder.forAddress(connection.getHost(), connection.getPort()).usePlaintext());
    }

    private Client(Connection connection, ManagedChannelBuilder<?> builder) {
        val deadline = Deadline.after(connection.getTimeout(), TimeUnit.MILLISECONDS);
        this.channel = builder.build();
        this.blockingStub = P2PGrpc
                .newBlockingStub(channel)
                .withCallCredentials(connection.getCredentials())
                .withDeadline(deadline);
    }

    /**
     * Retrieves the {@link AccountInfo} based on the address {@link Hash} and the block {@link Hash}
     *
     * @param address   The address
     * @param blockHash the block hash
     * @return The {@link AccountInfo}
     * @throws AccountNotFoundException if the account was not found.
     */
    public AccountInfo getAccountInfo(AccountAddress address, Hash blockHash) throws AccountNotFoundException {
        val request = ConcordiumP2PRpc.GetAddressInfoRequest
                .newBuilder()
                .setAddressBytes(accountAddressAsByteString(address))
                .setBlockHash(blockHash.asHex())
                .build();
        val response = blockingStub.getAccountInfo(request);
        val accountInfo = AccountInfo.fromJson(response.getValue());
        if (Objects.isNull(accountInfo)) {
            throw AccountNotFoundException.from(address, blockHash);
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
                .setAccountAddressBytes(accountAddressAsByteString(address))
                .build();
        val nextAccountNonce = blockingStub.getNextAccountNonce(request);
        return AccountNonce.fromJson(nextAccountNonce.getValue());
    }

    private ByteString accountAddressAsByteString(AccountAddress address) {
        return ByteString.copyFrom(address.getEncodedBytes());
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
        val transactionStatus = blockingStub.getTransactionStatus(request);
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
     * @return the {@link ConsensusStatus}
     */
    public ConsensusStatus getConsensusStatus() {
        val response = blockingStub.getConsensusStatus(ConcordiumP2PRpc.Empty.getDefaultInstance());
        return ConsensusStatus.fromJson(response.getValue());
    }

    /**
     * Retrieves a {@link BlockSummary}
     * @param blockHash the block {@link Hash} to query.
     * @return A {@link BlockSummary} for the block
     * @throws BlockNotFoundException If the block was not found.
     */
    public BlockSummary getBlockSummary(Hash blockHash) throws BlockNotFoundException {
        val request = ConcordiumP2PRpc.BlockHash.getDefaultInstance()
                .newBuilderForType()
                .setBlockHashBytes(ByteString.copyFromUtf8(blockHash.asHex()))
                .build();
        val response = blockingStub.getBlockSummary(request);
        val blockSummary = BlockSummary.fromJson(response.getValue());
        if (Objects.isNull(blockSummary)) {
            throw BlockNotFoundException.from(blockHash);
        }
        return blockSummary;
    }

    /**
     * Retrieves a {@link BlockInfo}
     * @param blockHash the block {@link Hash} to query.
     * @return A {@link BlockInfo} for the block
     * @throws BlockNotFoundException If the block was not found.
     */
    public BlockInfo getBlockInfo(Hash blockHash) throws BlockNotFoundException {
        val request = ConcordiumP2PRpc.BlockHash.getDefaultInstance()
                .newBuilderForType()
                .setBlockHashBytes(ByteString.copyFromUtf8(blockHash.asHex()))
                .build();
        val response = blockingStub.getBlockInfo(request);
        val blockInfo = BlockInfo.fromJson(response.getValue());
        if (Objects.isNull(blockInfo)) {
            throw BlockNotFoundException.from(blockHash);
        }
        return blockInfo;
    }

    /**
     * Retrieves a {@link BlocksAtHeight}
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
        val response = blockingStub.getBlocksAtHeight(request);
        val blocksAtHeight = BlocksAtHeight.fromJson(response.getValue());
        if (Objects.isNull(blocksAtHeight) || blocksAtHeight.getBlocks().isEmpty() ) {
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
        val response = blockingStub.sendTransaction(request);
        if (response.getValue()) {
            return transaction.getHash();
        }
        throw TransactionRejectionException.from(transaction);
    }

    /**
     * Closes the underlying grpc channel
     * This should be done when the {@link Client}
     * is of no more use.
     */
    public void close() {
        this.channel.shutdown();
    }
}
