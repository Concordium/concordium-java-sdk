package com.concordium.sdk;

import com.concordium.grpc.v2.AccountInfoRequest;
import com.concordium.grpc.v2.Empty;
import com.concordium.grpc.v2.QueriesGrpc;
import com.concordium.sdk.exceptions.BlockNotFoundException;
import com.concordium.sdk.exceptions.ClientInitializationException;
import com.concordium.sdk.requests.BlockHashInput;
import com.concordium.sdk.requests.getaccountinfo.AccountRequest;
import com.concordium.sdk.responses.BlockIdentifier;
import com.concordium.sdk.responses.accountinfo.AccountInfo;
import com.concordium.sdk.responses.blockinfo.BlockInfo;
import com.concordium.sdk.responses.blocksummary.updates.queues.AnonymityRevokerInfo;
import com.concordium.sdk.responses.consensusstatus.ConsensusStatus;
import com.concordium.sdk.transactions.AccountAddress;
import com.concordium.sdk.transactions.AccountNonce;
import com.concordium.sdk.transactions.Transaction;
import com.concordium.sdk.transactions.BlockItem;
import io.grpc.CallCredentials;
import io.grpc.ManagedChannel;
import io.grpc.StatusRuntimeException;
import lombok.val;
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
        try {
            return new ClientV2(connection.getTimeout(), connection.newChannel(), connection.getCredentials());
        } catch (IOException e) {
            throw ClientInitializationException.from(e);
        }
    }

    ClientV2(final int timeout, final ManagedChannel channel, CallCredentials credentials) {
        this.timeout = timeout;
        this.channel = channel;
        this.blockingStub = QueriesGrpc.newBlockingStub(channel).withCallCredentials(credentials);
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
     * Gets an {@link Iterator} of Blocks Arriving at the node.
     * With Specified Timeout.
     * Form the time request is made and onwards.
     * This can be used to listen for incoming blocks.
     *
     * @param timeoutMillis Timeout for the request in Milliseconds.
     * @return {@link Iterator<  BlockIdentifier  >}
     */
    public Iterator<BlockIdentifier> getBlocks(int timeoutMillis) {
        var grpcOutput = this.server(timeoutMillis).getBlocks(Empty.newBuilder().build());

        return to(grpcOutput, ClientV2MapperExtensions::to);
    }

    /**
     * Gets an {@link Iterator} of Finalized Blocks.
     * With Specified Timeout.
     * Form the time request is made and onwards.
     * This can be used to listen for blocks being Finalized.
     *
     * @param timeoutMillis Timeout for the request in Milliseconds.
     * @return {@link Iterator<  BlockIdentifier  >}
     */
    public Iterator<BlockIdentifier> getFinalizedBlocks(int timeoutMillis) {
        var grpcOutput = this.server(timeoutMillis)
                .getFinalizedBlocks(Empty.newBuilder().build());

        return to(grpcOutput, ClientV2MapperExtensions::to);
    }

    /**
     * Retrieve the information about the given account in the given block.
     *
     * @param input             Pointer to the Block.
     * @param accountIdentifier Identifier of the Account.
     * @return Account Information ({@link AccountInfo})
     */
    public AccountInfo getAccountInfo(
            final BlockHashInput input,
            final AccountRequest accountIdentifier) {
        val grpcOutput = this.server().getAccountInfo(
                AccountInfoRequest.newBuilder()
                        .setBlockHash(to(input))
                        .setAccountIdentifier(to(accountIdentifier))
                        .build());

        return to(grpcOutput);
    }

    /**
     * Retrieve the list of accounts that exist at the end of the given block.
     *
     * @param input Pointer to the Block.
     * @return {@link Iterator<  AccountAddress  >}.
     */
    public Iterator<AccountAddress> getAccountList(final BlockHashInput input) {
        var grpcOutput = this.server().getAccountList(to(input));

        return to(grpcOutput, ClientV2MapperExtensions::to);
    }

    /**
     * Gets the Block Items for a Particular Input Block.
     * Block Item represents transactions which are part of a block.
     * Type of Block Items currently supported are
     * <br/> {@link com.concordium.sdk.transactions.BlockItemType#ACCOUNT_TRANSACTION}
     * <br/> {@link com.concordium.sdk.transactions.BlockItemType#CREDENTIAL_DEPLOYMENT}
     * <br/> {@link com.concordium.sdk.transactions.BlockItemType#UPDATE_INSTRUCTION}
     *
     * @param input Pointer to the Block.
     * @return
     */
    public Iterator<BlockItem> getBlockItems(final BlockHashInput input) {
        var grpcOutput = this.server().getBlockItems(to(input));

        return to(grpcOutput, ClientV2MapperExtensions::to);
    }

    /**
     * Retrieve the Consensus Info that contains the summary of the current state
     * of the chain from the perspective of the node.
     *
     * @return Concensus Status ({@link ConsensusStatus})
     */
    public ConsensusStatus getConsensusInfo() {
        var grpcOutput = this.server()
                .getConsensusInfo(Empty.newBuilder().build());
        return to(grpcOutput);
    }

    /**
     * Retrieves a {@link BlockInfo}
     *
     * @param blockHashInput the block {@link BlockHashInput} to query.
     * @return A {@link BlockInfo} for the block
     * @throws BlockNotFoundException If the block was not found.
     */
    public BlockInfo getBlockInfo(BlockHashInput blockHashInput) throws BlockNotFoundException {
        try {
            return to(this.server()
                    .getBlockInfo(to(blockHashInput)));
        } catch (StatusRuntimeException e) {
            throw BlockNotFoundException.from(blockHashInput.getBlockHash());
        }
    }


    /**
     * Retrieves the next {@link AccountNonce} for an account.
     * This is the {@link AccountNonce} to use for future transactions
     * E.g. when using {@link Client#sendTransaction(Transaction)}
     * When this function is queried with a non existent account it will report the next available account nonce to be 1 and all transactions as finalized.
     *
     * @param address The {@link AccountAddress}
     * @return The next {@link AccountNonce}
     */
    public AccountNonce getNextAccountSequenceNumber(AccountAddress address) {
        var grpcOutput = this.server()
                .getNextAccountSequenceNumber(to(address));
        return to(grpcOutput);
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
     * Get a {@link QueriesGrpc.QueriesBlockingStub} with a timeout
     *
     * @param timeoutMillis Custom Timeout in Milliseconds.
     * @return A new stub with a timeout.
     */
    private QueriesGrpc.QueriesBlockingStub server(int timeoutMillis) {
        return this.blockingStub.withDeadlineAfter(timeoutMillis, TimeUnit.MILLISECONDS);
    }
}
