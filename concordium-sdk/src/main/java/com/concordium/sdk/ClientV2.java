package com.concordium.sdk;

import com.concordium.grpc.v2.*;
import com.concordium.sdk.exceptions.ClientInitializationException;
import com.concordium.sdk.requests.ConsensusDetailedStatusQuery;
import com.concordium.sdk.requests.Range;
import com.concordium.sdk.requests.*;
import com.concordium.sdk.requests.dumpstart.DumpRequest;
import com.concordium.sdk.requests.smartcontracts.InvokeInstanceRequest;
import com.concordium.sdk.responses.AccountIndex;
import com.concordium.sdk.responses.BakerId;
import com.concordium.sdk.responses.DelegatorInfo;
import com.concordium.sdk.responses.DelegatorRewardPeriodInfo;
import com.concordium.sdk.responses.*;
import com.concordium.sdk.responses.accountinfo.AccountInfo;
import com.concordium.sdk.responses.bakersrewardperiod.BakerRewardPeriodInfo;
import com.concordium.sdk.responses.blockcertificates.BlockCertificates;
import com.concordium.sdk.responses.blockinfo.BlockInfo;
import com.concordium.sdk.responses.blockitemstatus.BlockItemStatus;
import com.concordium.sdk.responses.blockitemstatus.FinalizedBlockItem;
import com.concordium.sdk.responses.blockitemsummary.Summary;
import com.concordium.sdk.responses.blocksatheight.BlocksAtHeightRequest;
import com.concordium.sdk.responses.blocksummary.FinalizationData;
import com.concordium.sdk.responses.blocksummary.specialoutcomes.SpecialOutcome;
import com.concordium.sdk.responses.blocksummary.updates.queues.AnonymityRevokerInfo;
import com.concordium.sdk.responses.blocksummary.updates.queues.IdentityProviderInfo;
import com.concordium.sdk.responses.branch.Branch;
import com.concordium.sdk.responses.chainparameters.ChainParameters;
import com.concordium.sdk.responses.consensusstatus.ConsensusDetailedStatus;
import com.concordium.sdk.responses.consensusstatus.ConsensusStatus;
import com.concordium.sdk.responses.cryptographicparameters.CryptographicParameters;
import com.concordium.sdk.responses.election.ElectionInfo;
import com.concordium.sdk.responses.modulelist.ModuleRef;
import com.concordium.sdk.responses.nodeinfo.NodeInfo;
import com.concordium.sdk.responses.peerlist.PeerInfo;
import com.concordium.sdk.responses.poolstatus.BakerPoolStatus;
import com.concordium.sdk.responses.rewardstatus.RewardsOverview;
import com.concordium.sdk.responses.smartcontracts.InvokeInstanceResult;
import com.concordium.sdk.responses.winningbaker.WinningBaker;
import com.concordium.sdk.transactions.AccountTransaction;
import com.concordium.sdk.transactions.BlockItem;
import com.concordium.sdk.transactions.*;
import com.concordium.sdk.transactions.smartcontracts.WasmModule;
import com.concordium.sdk.types.AbsoluteBlockHeight;
import com.concordium.sdk.types.AccountAddress;
import com.concordium.sdk.types.ContractAddress;
import com.concordium.sdk.types.Timestamp;
import com.concordium.sdk.types.*;
import com.google.common.collect.ImmutableList;
import com.google.protobuf.ByteString;
import io.grpc.ManagedChannel;
import io.grpc.Status;
import lombok.val;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;

import static com.concordium.sdk.ClientV2MapperExtensions.to;
import static com.concordium.sdk.ClientV2MapperExtensions.toTransactionHash;

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
            return new ClientV2(connection.getTimeout(), connection.newChannel(), Optional.ofNullable(connection.getCredentials()));
        } catch (IOException e) {
            throw ClientInitializationException.from(e);
        }
    }

    /**
     * Construct a new client
     *
     * @param timeout     The timeout in milliseconds.
     * @param channel     the underlying grpc channel.
     * @param credentials Optionally extra headers.
     */
    ClientV2(final int timeout, final ManagedChannel channel, final Optional<Credentials> credentials) {
        this.timeout = timeout;
        this.channel = channel;
        if (credentials.isPresent()) {
            this.blockingStub = QueriesGrpc.newBlockingStub(channel).withCallCredentials(credentials.get().getCallCredentials());
        } else {
            this.blockingStub = QueriesGrpc.newBlockingStub(channel);
        }
    }

    /**
     * Gets all the Anonymity Revokers at the end of the block pointed by {@link BlockQuery}.
     *
     * @param input Pointer to the Block.
     * @return {@link Iterator} of {@link AnonymityRevokerInfo}
     */
    public Iterator<AnonymityRevokerInfo> getAnonymityRevokers(final BlockQuery input) {
        val grpcOutput = this.server().getAnonymityRevokers(to(input));
        return to(grpcOutput, ClientV2MapperExtensions::to);
    }

    /**
     * Gets an {@link Iterator} of Blocks Arriving at the node.
     * With Specified Timeout.
     * Form the time request is made and onwards.
     * This can be used to listen for incoming blocks.
     *
     * @param timeoutMillis Timeout for the request in Milliseconds.
     * @return {@link Iterator<BlockIdentifier>}
     */
    public Iterator<BlockIdentifier> getBlocks(int timeoutMillis) {
        val grpcOutput = this.server(timeoutMillis).getBlocks(Empty.newBuilder().build());

        return to(grpcOutput, ClientV2MapperExtensions::to);
    }

    /**
     * Gets an {@link Iterator} of Finalized Blocks with specified timeout from the time request is made and onwards.
     * This can be used to listen for blocks being Finalized.
     *
     * @param timeoutMillis Timeout for the request in Milliseconds.
     * @return {@link Iterator<BlockIdentifier>}
     */
    public Iterator<BlockIdentifier> getFinalizedBlocks(int timeoutMillis) {
        val grpcOutput = this.server(timeoutMillis)
                .getFinalizedBlocks(Empty.newBuilder().build());

        return to(grpcOutput, ClientV2MapperExtensions::to);
    }

    /**
     * Gets an {@link Iterator} of Finalized Blocks from the time request is made and onwards.
     * This can be used to listen for blocks being Finalized. <p>
     * <p>
     * Note, may block indefinitely. Use {@link ClientV2#getFinalizedBlocks(int)} to specify a timeout.
     *
     * @return {@link Iterator<BlockIdentifier>}
     */
    public Iterator<BlockIdentifier> getFinalizedBlocks() {
        val grpcOutput = this.blockingStub
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
            final BlockQuery input,
            final AccountQuery accountIdentifier) {
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
     * @return {@link Iterator<AccountAddress>}.
     */
    public Iterator<AccountAddress> getAccountList(final BlockQuery input) {
        val grpcOutput = this.server().getAccountList(to(input));

        return to(grpcOutput, ClientV2MapperExtensions::to);
    }

    /**
     * Retrieve a stream of transaction events in the specified block.
     *
     * @param input the block to query.
     * @return the stream of transaction events
     */
    public Iterator<Summary> getBlockTransactionEvents(final BlockQuery input) {
        val output = this.server().getBlockTransactionEvents(to(input));
        return to(output, ClientV2MapperExtensions::to);
    }

    /**
     * Gets the transactions of a Block.
     * Type of Block Items currently supported are
     * <br/> {@link com.concordium.sdk.transactions.BlockItemType#ACCOUNT_TRANSACTION}
     * <br/> {@link com.concordium.sdk.transactions.BlockItemType#CREDENTIAL_DEPLOYMENT}
     * <br/> {@link com.concordium.sdk.transactions.BlockItemType#UPDATE_INSTRUCTION}
     *
     * @param input Pointer to the Block.
     * @return {@link Iterator<BlockItem>}
     */
    public Iterator<BlockItem> getBlockItems(final BlockQuery input) {
        val grpcOutput = this.server().getBlockItems(to(input));
        return to(grpcOutput, ClientV2MapperExtensions::to);
    }

    /**
     * Retrieve the Consensus Info that contains the summary of the current state
     * of the chain from the perspective of the node.
     *
     * @return the Consensus Status ({@link ConsensusStatus})
     * @see ClientV2#getConsensusDetailedStatus(ConsensusDetailedStatusQuery)
     */
    public ConsensusStatus getConsensusInfo() {
        val grpcOutput = this.server()
                .getConsensusInfo(Empty.newBuilder().build());
        return to(grpcOutput);
    }

    /**
     * Get the detailed status of the consensus. This is only available for consensus version 1.
     *
     * @param query {@link ConsensusDetailedStatusQuery} representing the genesis index to get status for.
     * @return the detailed status
     * @throws io.grpc.StatusRuntimeException with {@link io.grpc.Status.Code}: <ul>
     *                                        <li> {@link io.grpc.Status#NOT_FOUND} if the query specifies an unknown genesis index</li>
     *                                        <li> {@link io.grpc.Status#INVALID_ARGUMENT} if the query specifies a genesis index at consensus version 0</li>
     *                                        <li> {@link io.grpc.Status#UNIMPLEMENTED} if the endpoint is disabled on the node</li>
     *                                        </ul>
     * @see ClientV2#getConsensusInfo()
     */
    public ConsensusDetailedStatus getConsensusDetailedStatus(final ConsensusDetailedStatusQuery query) {
        val grpcOutput = this.server().getConsensusDetailedStatus(to(query));
        return to(grpcOutput);
    }

    /**
     * Get all accounts that have scheduled releases, with the timestamp of the first pending
     * scheduled release for that account. (Note, this only identifies accounts by index, and
     * only indicates the first pending release for each account.)
     */
    public Iterator<AccountPending> getScheduledReleaseAccounts(BlockQuery input) {
        return this.server().getScheduledReleaseAccounts(to(input));
    }

    /**
     * Get all accounts that have stake in cooldown, with the timestamp of the first pending
     * cooldown expiry for each account. (Note, this only identifies accounts by index,
     * and only indicates the first pending cooldown for each account.)
     * Prior to protocol version 7, the resulting stream will always be empty.
     */
    public Iterator<AccountPending> getCooldownAccounts(BlockQuery input) {
        return this.server().getCooldownAccounts(to(input));
    }

    /**
     * Get all accounts that have stake in pre-cooldown.
     * (This only identifies accounts by index.)
     * Prior to protocol version 7, the resulting stream will always be empty.
     */
    public Iterator<AccountIndex> getPreCooldownAccounts(BlockQuery input) {
        val grpcOutput = this.server().getPreCooldownAccounts(to(input));
        return to(grpcOutput, ClientV2MapperExtensions::to);
    }

    /**
     * Get all accounts that have stake in pre-pre-cooldown.
     * (This only identifies accounts by index.)
     * Prior to protocol version 7, the resulting stream will always be empty.
     */
    public Iterator<AccountIndex> getPrePreCooldownAccounts(BlockQuery input) {
        val grpcOutput = this.server().getPrePreCooldownAccounts(to(input));
        return to(grpcOutput, ClientV2MapperExtensions::to);
    }

    /**
     * Sends an Account Transaction to the Concordium Node.
     *
     * @param accountTransaction Account Transaction to send.
     * @return Transaction {@link Hash}.
     */
    public Hash sendTransaction(final AccountTransaction accountTransaction) {
        val req = ClientV2MapperExtensions.to(accountTransaction);
        val grpcOutput = this.server().sendBlockItem(req);

        return to(grpcOutput);
    }

    /**
     * Sends a credential deployment transaction to the Concordium Node.
     *
     * @param credentialDeploymentTransaction the credential deployment to send.
     * @return Transaction {@link Hash}.
     */
    public Hash sendCredentialDeploymentTransaction(CredentialDeploymentTransaction credentialDeploymentTransaction) {
        val req = ClientV2MapperExtensions.to(credentialDeploymentTransaction);
        val grpcOutput = this.server().sendBlockItem(req);
        return to(grpcOutput);
    }

    /**
     * Gets all the Identity Providers at the end of the block pointed by {@link BlockQuery}.
     *
     * @param input Pointer to the Block.
     * @return {@link Iterator} of {@link IdentityProviderInfo}
     */
    public Iterator<IdentityProviderInfo> getIdentityProviders(final BlockQuery input) {
        val grpcOutput = this.server().getIdentityProviders(to(input));

        return to(grpcOutput, ClientV2MapperExtensions::to);
    }

    /**
     * Get the {@link CryptographicParameters} at a given block.
     *
     * @param blockHash the hash of the block
     * @return the cryptographic parameters at the given block.
     */
    public CryptographicParameters getCryptographicParameters(final BlockQuery blockHash) {
        val grpcOutput = this.server()
                .getCryptographicParameters(to(blockHash));
        return to(grpcOutput);
    }

    /**
     * Get the information about total amount of CCD and the state of valious special accounts in the provided block.
     *
     * @param blockHash Block at which the reward status is to be retrieved.
     * @return Parsed {@link RewardsOverview}.
     */
    public RewardsOverview getRewardStatus(final BlockQuery blockHash) {
        val grpcOutput = this.server().getTokenomicsInfo(to(blockHash));
        return to(grpcOutput);
    }

    /**
     * Retrieves a {@link BlockInfo}
     *
     * @param blockQuery the block {@link BlockQuery} to query.
     * @return A {@link BlockInfo} for the block
     */
    public BlockInfo getBlockInfo(BlockQuery blockQuery) {
        return to(this.server()
                .getBlockInfo(to(blockQuery)));

    }

    /**
     * Get the blockchain parameters
     *
     * @param blockQuery block to query.
     * @return the {@link ChainParameters}
     */
    public ChainParameters getChainParameters(BlockQuery blockQuery) {
        val blockChainParameters = this.server().getBlockChainParameters(to(blockQuery));
        return to(blockChainParameters);
    }

    /**
     * Retrieves the next {@link Nonce} for an account.
     * This is the {@link Nonce} to use for future transactions
     * E.g. when using {@link ClientV2#sendTransaction(AccountTransaction)}
     * When this function is queried with a non-existent account it will report the next available account nonce to be 1 and all transactions as finalized.
     *
     * @param address The {@link AccountAddress}
     * @return The next {@link Nonce}
     */
    public Nonce getNextAccountSequenceNumber(AccountAddress address) {
        val grpcOutput = this.server()
                .getNextAccountSequenceNumber(to(address));
        return to(grpcOutput);
    }

    /**
     * Retrieves the {@link BlockItemStatus} for a given transaction {@link Hash}
     *
     * @param transactionHash The transaction {@link Hash}
     * @return The {@link BlockItemStatus}
     * @throws io.grpc.StatusRuntimeException with {@link io.grpc.Status.Code}: <ul>
     *                                        <li> {@link io.grpc.Status#NOT_FOUND} if the transaction is not known to the node.
     *                                        </ul>
     */
    public BlockItemStatus getBlockItemStatus(Hash transactionHash) {
        val grpcOutput = this.server()
                .getBlockItemStatus(toTransactionHash(transactionHash));
        return BlockItemStatus.from(grpcOutput);
    }


    /**
     * Get the list of transactions hashes for transactions from the given account,
     * but which are not yet finalized. They are either committed to a block or still pending.
     * If the account does not exist an empty list will be returned.
     *
     * @param address {@link AccountAddress}
     * @return {@link Iterator} of Transaction {@link Hash}
     */
    public Iterator<Hash> getAccountNonFinalizedTransactions(AccountAddress address) {
        val grpcOutput = this.server().getAccountNonFinalizedTransactions(to(address));
        return to(grpcOutput, ClientV2MapperExtensions::to);
    }

    /**
     * Start dumping packages into the specified {@link File}
     * Only enabled if the node was built with the 'network_dump' feature enabled
     *
     * @param dumpRequest {@link DumpRequest} specifying the file and if raw packages should be dumped
     * @throws {@link io.grpc.StatusRuntimeException} if the network dump failed to start or if the node was not built with the 'network_dump' feature
     */
    public void dumpStart(DumpRequest dumpRequest) {
        val grpcRequest = com.concordium.grpc.v2.DumpRequest.newBuilder()
                .setFile(dumpRequest.getPath().toString())
                .setRaw(dumpRequest.isRaw())
                .build();
        this.server().dumpStart(grpcRequest);
    }

    /**
     * Stop dumping packages
     * Only enabled if the node was built with the 'network_dump' feature enabled
     *
     * @throws io.grpc.StatusRuntimeException if the network dump failed to be stopped or if the node was not built with the 'network_dump' feature
     */
    public void dumpStop() {
        this.server().dumpStop(Empty.newBuilder().build());
    }

    /**
     * Retrieves the {@link FinalizationData} of a given block {@link BlockQuery}
     * Note. Returns NULL if there is no finalization data in the block
     *
     * @param blockQuery the block {@link BlockQuery} to query
     * @return The {@link FinalizationData} of the block
     */
    public Optional<FinalizationData> getBlockFinalizationSummary(BlockQuery blockQuery) {
        val grpcOutput = this.server().getBlockFinalizationSummary(to(blockQuery));
        return to(grpcOutput);
    }

    /**
     * Get information related to the baker election for a particular block.
     *
     * @param input {@link BlockQuery}
     * @return {@link ElectionInfo}
     */
    public ElectionInfo getElectionInfo(BlockQuery input) {
        val grpcOutput = this.server().getElectionInfo(to(input));

        return ClientV2MapperExtensions.to(grpcOutput);
    }

    /**
     * Retrieves a list of {@link SpecialOutcome}s in a given block {@link BlockQuery}
     * These are events generated by the protocol, such as minting and reward payouts.
     * They are not directly generated by any transaction.
     *
     * @param blockQuery the block {@link BlockQuery} to query
     * @return {@link ImmutableList} of {@link SpecialOutcome}s not directly caused by a transaction
     */
    public ImmutableList<SpecialOutcome> getBlockSpecialEvents(BlockQuery blockQuery) {
        val grpcOutput = this.server().getBlockSpecialEvents(to(blockQuery));
        return to(grpcOutput);
    }

    /**
     * Gets Block Ancestor Blocks.
     *
     * @param blockQuery {@link BlockQuery} of the block.
     * @param num        Total no of Ancestor blocks to get.
     * @return {@link Iterator} of {@link Hash}
     */
    public Iterator<Hash> getAncestors(BlockQuery blockQuery, long num) {
        val getAncestorsRequestInput = AncestorsRequest.newBuilder()
                .setBlockHash(to(blockQuery))
                .setAmount(num)
                .build();
        val grpcOutput = this.server().getAncestors(getAncestorsRequestInput);
        return to(grpcOutput, ClientV2MapperExtensions::to);
    }

    /**
     * Get the IDs of the bakers registered in the given block.
     *
     * @param blockQuery {@link BlockQuery} of the block bakers are to be retrieved.
     * @return Parsed {@link Iterator} of {@link com.concordium.sdk.responses.BakerId}
     */
    public Iterator<com.concordium.sdk.responses.BakerId> getBakerList(BlockQuery blockQuery) {
        val grpcOutput = this.server().getBakerList(to(blockQuery));
        return to(grpcOutput, ClientV2MapperExtensions::to);
    }

    /**
     * Get the list of contract addresses in the given block.
     *
     * @param blockQuery {@link BlockQuery} of the block bakers are to be retrieved.
     * @return Parsed {@link Iterator} of {@link ContractAddress}
     */
    public Iterator<ContractAddress> getInstanceList(BlockQuery blockQuery) {
        val grpcOutput = this.server().getInstanceList(to(blockQuery));
        return to(grpcOutput, ClientV2MapperExtensions::to);
    }

    /**
     * Get info about a smart contract instance as it appears at the end of the given block.
     *
     * @param input           {@link BlockQuery}
     * @param contractAddress {@link ContractAddress} of the contract instance.
     * @return {@link com.concordium.sdk.responses.intanceinfo.InstanceInfo} Information about the contract instance.
     */
    public com.concordium.sdk.responses.intanceinfo.InstanceInfo getInstanceInfo(
            BlockQuery input,
            ContractAddress contractAddress) {
        val grpcOutput = this.server().getInstanceInfo(
                InstanceInfoRequest
                        .newBuilder()
                        .setBlockHash(to(input))
                        .setAddress(to(contractAddress))
                        .build());

        return to(grpcOutput);
    }

    /**
     * Get the fixed delegators of a given pool for the reward period of the given block.
     * In contracts to the `GetPoolDelegators` which returns delegators registered
     * for the given block, this endpoint returns the fixed delegators contributing
     * stake in the reward period containing the given block.
     * The stream will end when all the delegators has been returned.
     *
     * @param input   {@link BlockQuery}.
     * @param bakerId {@link com.concordium.sdk.responses.BakerId}.
     * @return {@link Iterator<DelegatorRewardPeriodInfo>}.
     */
    public Iterator<DelegatorRewardPeriodInfo> getPoolDelegatorsRewardPeriod(
            BlockQuery input,
            com.concordium.sdk.responses.BakerId bakerId) {
        val grpcOutput = this.server().getPoolDelegatorsRewardPeriod(GetPoolDelegatorsRequest.newBuilder()
                .setBlockHash(to(input))
                .setBaker(to(bakerId))
                .build());

        return ClientV2MapperExtensions.to(grpcOutput, ClientV2MapperExtensions::to);
    }

    /**
     * Get the fixed passive delegators for the reward period of the given block.
     * In contracts to the `GetPassiveDelegators` which returns delegators registered
     * for the given block, this endpoint returns the fixed delegators contributing
     * stake in the reward period containing the given block.
     * The stream will end when all the delegators has been returned.
     *
     * @param input {@link BlockQuery}.
     * @return {@link Iterator<DelegatorRewardPeriodInfo>}.
     */
    public Iterator<DelegatorRewardPeriodInfo> getPassiveDelegatorsRewardPeriod(
            BlockQuery input) {
        val grpcOutput = this.server().getPassiveDelegatorsRewardPeriod(to(input));

        return ClientV2MapperExtensions.to(grpcOutput, ClientV2MapperExtensions::to);
    }

    /**
     * Get the branches of the node's tree. Branches are all live blocks that
     * are successors of the last finalized block. In particular this means
     * that blocks which do not have a parent are not included in this
     * response
     *
     * @return {@link Branch}
     */
    public Branch getBranches() {
        val grpcOutput = this.server().getBranches(Empty.getDefaultInstance());

        return ClientV2MapperExtensions.to(grpcOutput);
    }


    /**
     * Get information about the passive delegators at the end of a given block.
     *
     * @param input {@link BlockQuery}.
     * @return {@link Iterator} of {@link DelegatorInfo}.
     */
    public Iterator<DelegatorInfo> getPassiveDelegators(BlockQuery input) {
        val grpcOutput = this.server().getPassiveDelegators(to(input));

        return to(grpcOutput, ClientV2MapperExtensions::to);
    }

    /**
     * Get the registered delegators of a given pool at the end of a given block.
     * Any changes to delegators are immediately visible in this list.
     *
     * @param input   {@link BlockQuery}
     * @param bakerId {@link AccountIndex}
     * @return {@link Iterator} of {@link DelegatorInfo}. List of delegators that are registered in the block.
     */
    public Iterator<DelegatorInfo> getPoolDelegators(BlockQuery input, AccountIndex bakerId) {
        val grpcOutput = this.server().getPoolDelegators(GetPoolDelegatorsRequest.newBuilder()
                .setBlockHash(to(input))
                .setBaker(com.concordium.grpc.v2.BakerId.newBuilder().setValue(bakerId.getIndex().getValue()).build())
                .build());

        return to(grpcOutput, ClientV2MapperExtensions::to);
    }

    /**
     * Get information about the passive delegators at the end of a given block.
     *
     * @param input {@link BlockQuery}
     */
    public PassiveDelegationInfo getPassiveDelegationInfo(BlockQuery input) {
        return this.server().getPassiveDelegationInfo(to(input));
    }

    /**
     * Get next available sequence numbers for updating chain parameters after a given block.
     *
     * @param input {@link BlockQuery}.
     * @return {@link com.concordium.sdk.responses.NextUpdateSequenceNumbers}.
     */
    public com.concordium.sdk.responses.NextUpdateSequenceNumbers getNextUpdateSequenceNumbers(BlockQuery input) {
        val grpcOutput = this.server().getNextUpdateSequenceNumbers(to(input));

        return ClientV2MapperExtensions.to(grpcOutput);
    }

    public Iterator<PendingUpdateV2> getBlockPendingUpdates(BlockQuery input) {
        val grpcOutput = this.server().getBlockPendingUpdates(to(input));

        return ClientV2MapperExtensions.to(grpcOutput, ClientV2MapperExtensions::to);
    }


    /**
     * Get the exact state of a specific contract instance, streamed as a list of key-value pairs.
     * The list is streamed in lexicographic order of keys.
     *
     * @param input           {@link BlockQuery}.
     * @param contractAddress {@link ContractAddress}.
     * @return {@link Iterator} of {@link KeyValurPair}.
     */
    public Iterator<KeyValurPair> getInstanceState(
            BlockQuery input,
            ContractAddress contractAddress) {
        val grpcOutput = this.server().getInstanceState(InstanceInfoRequest.newBuilder()
                .setBlockHash(to(input))
                .setAddress(to(contractAddress))
                .build());

        return to(grpcOutput, ClientV2MapperExtensions::to);
    }

    /**
     * Get the value at a specific key of a contract state.
     * In contrast to {@link ClientV2#getInstanceState(BlockQuery, ContractAddress)} this is more efficient,
     * but requires the user to know the specific key to look for.
     *
     * @param input           {@link BlockQuery}.
     * @param contractAddress {@link ContractAddress}.
     * @param key             Instance State Key to Lookup.
     * @return Instance State Value for the input `key`
     */
    public byte[] instanceStateLookup(
            BlockQuery input,
            ContractAddress contractAddress,
            byte[] key) {
        final InstanceStateValueAtKey grpcOutput = this.server().instanceStateLookup(InstanceStateLookupRequest.newBuilder()
                .setKey(ByteString.copyFrom(key))
                .setAddress(to(contractAddress))
                .setBlockHash(to(input))
                .build());

        return grpcOutput.getValue().toByteArray();
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
     * Retrieves valious information about the node
     *
     * @return {@link NodeInfo} containing valious information about the node
     */
    public NodeInfo getNodeInfo() {
        val grpcOutput = this.server().getNodeInfo(Empty.newBuilder().build());
        return NodeInfo.parse(grpcOutput);
    }

    /**
     * Returns a {@link ImmutableList} of {@link PeerInfo} containing information about the nodes' peers
     *
     * @return {@link ImmutableList} of {@link PeerInfo}
     * @throws UnknownHostException When the returned IP address of Peer is Invalid
     */
    public ImmutableList<PeerInfo> getPeersInfo() throws UnknownHostException {
        val grpcOutput = this.server().getPeersInfo(Empty.newBuilder().build());
        return PeerInfo.parseToList(grpcOutput);
    }

    /**
     * Get information about a given pool at the end of a given block.
     *
     * @param input   {@link BlockQuery}.
     * @param bakerId {@link BakerId}.
     * @return {@link BakerPoolStatus}.
     */
    public BakerPoolStatus getPoolInfo(BlockQuery input, com.concordium.sdk.responses.BakerId bakerId) {
        val grpcOutput = this.server().getPoolInfo(PoolInfoRequest.newBuilder()
                .setBlockHash(to(input))
                .setBaker(to(bakerId))
                .build());

        return ClientV2MapperExtensions.to(grpcOutput);
    }

    /**
     * Get list of Smart Contract modules at the end of the given block.
     *
     * @param input {@link BlockQuery}.
     * @return {@link Iterator<ModuleRef>}.
     */
    public Iterator<ModuleRef> getModuleList(BlockQuery input) {
        val grpcOutput = this.server().getModuleList(to(input));

        return to(grpcOutput, ClientV2MapperExtensions::to);
    }

    /**
     * Shut down the node. Return a GRPC error if the shutdown failed.
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void shutdown() {
        this.server().shutdown(Empty.getDefaultInstance());
    }

    /**
     * Tries to connect to a peer with the submitted {@link InetSocketAddress}.
     * If successful, adds the peer to the list of given addresses.
     * Note. The peer might not be connected instantly, in that case the node will
     * try to establish the connection in near future. In this case {@link io.grpc.Status#OK} is returned
     *
     * @param socketAddress {@link InetSocketAddress} of the peer to connect to
     * @throws {@link io.grpc.StatusRuntimeException} if unsuccessful
     */
    public void peerConnect(InetSocketAddress socketAddress) {
        val grpcIpSocket = IpSocketAddress.newBuilder()
                .setIp(IpAddress.newBuilder().setValue(socketAddress.getAddress().getHostAddress()).build())
                .setPort(Port.newBuilder().setValue(socketAddress.getPort()))
                .build();
        this.server().peerConnect(grpcIpSocket);
    }

    /**
     * Disconnect from the peer with the submitted {@link InetSocketAddress} and remove them from
     * the address list if they are on it.
     *
     * @param socketAddress {@link InetSocketAddress} of the peer to disconnect from
     * @throws {@link io.grpc.StatusRuntimeException} if unsuccessful
     */
    public void peerDisconnect(InetSocketAddress socketAddress) {
        val grpcIpSocket = IpSocketAddress.newBuilder()
                .setIp(IpAddress.newBuilder().setValue(socketAddress.getAddress().getHostAddress()).build())
                .setPort(Port.newBuilder().setValue(socketAddress.getPort()))
                .build();
        this.server().peerDisconnect(grpcIpSocket);
    }

    /***
     * Ban a specific peer
     * Note this will ban all peers located behind the specified IP even though they are using different ports
     * @param ipToBan ip of the node to ban
     * @throws {@link io.grpc.StatusRuntimeException} If the action failed
     */
    public void banPeer(InetAddress ipToBan) {
        this.server().banPeer(PeerToBan.newBuilder()
                .setIpAddress(IpAddress.newBuilder().setValue(ipToBan.getHostAddress()).build())
                .build());
    }

    /**
     * Unban a specific peer
     *
     * @param ipToUnban ip of the node to unban
     * @throws {@link io.grpc.StatusRuntimeException} If the action failed
     */
    public void unbanPeer(InetAddress ipToUnban) {
        this.server().unbanPeer(BannedPeer.newBuilder()
                .setIpAddress(IpAddress.newBuilder().setValue(ipToUnban.getHostAddress()).build())
                .build());
    }

    /**
     * Get a list of banned peers
     *
     * @return {@link ImmutableList} of {@link InetAddress} of banned peers
     * @throws UnknownHostException When the returned IP address of a peer is invalid
     */
    public ImmutableList<InetAddress> getBannedPeers() throws UnknownHostException {
        val response = this.server().getBannedPeers(Empty.newBuilder().build());
        val bannedPeers = new ImmutableList.Builder<InetAddress>();
        for (BannedPeer peer : response.getPeersList()) {
            bannedPeers.add(InetAddress.getByName(peer.getIpAddress().getValue()));
        }
        return bannedPeers.build();
    }

    /**
     * Get the source of a smart contract module from
     * the perspective of the last finalized block.
     *
     * @param query     The query is at the end of the block specified.
     * @param moduleRef the reference of the module.
     * @return the parsed {@link WasmModule}.
     * @throws io.grpc.StatusRuntimeException if the module could not be looked up on the chain.
     */
    public WasmModule getModuleSource(BlockQuery query, ModuleRef moduleRef) {
        val response = this.server().getModuleSource(ModuleSourceRequest.newBuilder()
                .setBlockHash(to(query))
                .setModuleRef(to(moduleRef))
                .build());

        return to(response);
    }

    /**
     * This function can be used to either
     * 1. Dry-run the smart contract entrypoint with the
     * provided context and in the state at the end of the given block.
     * 2. Call a view function on a smart contract.
     * View functions are a special kind of functions that smart contract writers
     * write in order to be able to read the state of a smart contract instance.
     * <br>
     * Note that calling a smart contract instance with this function does not
     * have any effect on the actual chain, it's solely a local operation on the node,
     * hence it's "free" to call a smart contract instance with this function.
     *
     * @param request {@link InvokeInstanceRequest}
     * @return {@link InvokeInstanceResult}
     */
    public InvokeInstanceResult invokeInstance(InvokeInstanceRequest request) {
        val grpcRequest = com.concordium.grpc.v2.InvokeInstanceRequest.newBuilder()
                .setBlockHash(to(request.getBlockHash()));
        if (request.hasInvoker()) {
            grpcRequest.setInvoker(to(request.getInvoker()));
        }
        grpcRequest.setInstance(to(request.getInstance()))
                .setAmount(to(CCDAmount.from(0)))
                .setEntrypoint(to(request.getEntrypoint()))
                .setParameter(to(request.getParameter()));
        if (request.getEnergy().isPresent()) {
            grpcRequest.setEnergy(com.concordium.grpc.v2.Energy.newBuilder().setValue(request.getEnergy().get().getValue().getValue()));
        }
        val grpcResponse = this.server().invokeInstance(grpcRequest.build());
        return InvokeInstanceResult.parse(grpcResponse);
    }

    /**
     * Get all bakers in the reward period of a block.
     * This endpoint is only supported for protocol version 6 and onwards.
     *
     * @param input The block to query.
     * @return {@link ImmutableList} with the {@link BakerRewardPeriodInfo} of all the bakers in the block.
     * @throws io.grpc.StatusRuntimeException with {@link io.grpc.Status.Code}:
     *                                        <ul><li>{@link io.grpc.Status.Code#UNIMPLEMENTED} if the protocol does not support the endpoint.</ul>
     */
    public ImmutableList<BakerRewardPeriodInfo> getBakersRewardPeriod(BlockQuery input) {
        val response = this.server().getBakersRewardPeriod(to(input));
        val periodInfos = new ImmutableList.Builder<BakerRewardPeriodInfo>();
        response.forEachRemaining(
                info -> periodInfos.add(BakerRewardPeriodInfo.from(info))
        );
        return periodInfos.build();
    }

    /**
     * Retrieves the {@link BlockCertificates} for a given block.
     *
     * @param block The block to query
     * @return {@link BlockCertificates} of the block.
     * @throws io.grpc.StatusRuntimeException with {@link io.grpc.Status.Code}:<ul>
     *                                        <li>{@link io.grpc.Status.Code#UNIMPLEMENTED} if the endpoint is not enabled by the node.
     *                                        <li>{@link io.grpc.Status.Code#INVALID_ARGUMENT} if the block being pointed to is not a product of ConcordiumBFT, i.e. created before protocol version 6.
     *                                        </ul>
     */
    public BlockCertificates getBlockCertificates(BlockQuery block) {
        val res = this.server().getBlockCertificates(to(block));
        return BlockCertificates.from(res);
    }

    /**
     * Get the projected earliest time at which a particular baker will be required to bake a block. <p>
     * <p>
     * If the baker is not a baker for the current reward period, this returns a timestamp at the
     * start of the next reward period. <p>
     * If the baker is a baker for the current reward period, the
     * earliest win time is projected from the current round forward, assuming that each round after
     * the last finalized round will take the minimum block time. (If blocks take longer, or timeouts
     * occur, the actual time may be later, and the reported time in subsequent queries may reflect
     * this.) <p>
     * At the end of an epoch (or if the baker is not projected to bake before the end of the
     * epoch) the earliest win time for a (current) baker will be projected as the start of the next
     * epoch. This is because the seed for the leader election is updated at the epoch boundary, and
     * so the winners cannot be predicted beyond that. <p>
     * Note that in some circumstances the returned timestamp can be in the past, especially at the end of an epoch.
     *
     * @param bakerId id of the baker to query.
     * @return {@link Timestamp} as described in the method documentation.
     * @throws io.grpc.StatusRuntimeException with {@link io.grpc.Status.Code}:
     *                                        <ul><li>{@link io.grpc.Status.Code#UNIMPLEMENTED} if the current consensus version is 0, as the endpoint is only supported by consensus version 1.</ul>
     */
    public Timestamp getBakerEarliestWinTime(BakerId bakerId) {
        val res = this.server().getBakerEarliestWinTime(to(bakerId));
        return Timestamp.from(res);
    }

    /**
     * Get the block hash of the first finalized block in a specified epoch.
     *
     * @param epochQuery {@link EpochQuery} representing the specific epoch to query.
     * @return {@link Hash} of the first finalized block in the epoch.
     * @throws io.grpc.StatusRuntimeException with {@link io.grpc.Status.Code}: <ul>
     *                                        <li> {@link io.grpc.Status#NOT_FOUND} if the query specifies an unknown block.
     *                                        <li> {@link io.grpc.Status#UNAVAILABLE} if the query is for an epoch that is not finalized in the current genesis index, or is for a future genesis index.
     *                                        <li> {@link io.grpc.Status#INVALID_ARGUMENT} if the query is for an epoch with no finalized blocks for a past genesis index.
     *                                        <li> {@link io.grpc.Status#INVALID_ARGUMENT} if the input {@link EpochQuery} is malformed.
     *                                        <li> {@link io.grpc.Status#UNIMPLEMENTED} if the endpoint is disabled on the node.
     *                                        </ul>
     */
    public Hash getFirstBlockEpoch(EpochQuery epochQuery) {
        val res = this.server().getFirstBlockEpoch(to(epochQuery));
        return Hash.from(res);
    }

    /**
     * Get the list of bakers that won the lottery in a particular historical epoch (i.e. the last finalized block is in a later epoch). <p>
     * This lists the winners for each round in the epoch, starting from the round after the last block in the previous epoch, running to the round before the first block in the next epoch. <p>
     * It also indicates if a block in each round was included in the finalized chain.
     *
     * @param epochQuery {@link EpochQuery} representing the specific epoch to query.
     * @return {@link ImmutableList} of bakers that won the lottery in the specified epoch.
     * @throws io.grpc.StatusRuntimeException with {@link io.grpc.Status.Code}: <ul>
     *                                        <li> {@link io.grpc.Status#NOT_FOUND} if the query specifies an unknown block.
     *                                        <li> {@link io.grpc.Status#UNAVAILABLE} if the query is for an epoch that is not finalized in the current genesis index, or is for a future genesis index.
     *                                        <li> {@link io.grpc.Status#INVALID_ARGUMENT} if the query is for an epoch that is not finalized for a past genesis index.
     *                                        <li> {@link io.grpc.Status#INVALID_ARGUMENT} if the query is for a genesis index at consensus version 0.
     *                                        <li> {@link io.grpc.Status#INVALID_ARGUMENT} if the input {@link EpochQuery} is malformed.
     *                                        <li> {@link io.grpc.Status#UNIMPLEMENTED} if the endpoint is disabled on the node.
     *                                        </ul>
     */
    public ImmutableList<WinningBaker> getWinningBakersEpoch(EpochQuery epochQuery) {
        val res = this.server().getWinningBakersEpoch(to(epochQuery));
        val winners = new ImmutableList.Builder<WinningBaker>();
        res.forEachRemaining(
                winner -> winners.add(WinningBaker.parse(winner))
        );
        return winners.build();
    }

    /**
     * Waits until a given transaction is finalized and returns the corresponding {@link Optional<FinalizedBlockItem>}.
     * If the transaction is unknown to the node or not finalized, the client starts listening for newly finalized blocks,
     * and returns the corresponding {@link Optional<FinalizedBlockItem>} once the transaction is finalized.
     *
     * @param transactionHash the {@link Hash} of the transaction to wait for.
     * @param timeoutMillis   the number of milliseconds to listen for newly finalized blocks.
     * @return {@link Optional<FinalizedBlockItem>} of the transaction if it was finalized before exceeding the timeout, Empty otherwise.
     */
    public Optional<FinalizedBlockItem> waitUntilFinalized(Hash transactionHash, int timeoutMillis) {
        Optional<FinalizedBlockItem> maybeStatus = getFinalizedTransaction(transactionHash);
        // if it's finalized return it.
        if (maybeStatus.isPresent()) {
            return maybeStatus;
        }
        // check newly finalized blocks until we time out
        Optional<FinalizedBlockItem> result = Optional.empty();
        try {
            Iterator<BlockIdentifier> finalizedBlockStream = this.getFinalizedBlocks(timeoutMillis);
            while (finalizedBlockStream.hasNext()) {
                finalizedBlockStream.next();
                Optional<FinalizedBlockItem> finalizedBlockItem = getFinalizedTransaction(transactionHash);
                if (finalizedBlockItem.isPresent()) {
                    // the transaction is included in a finalized block, break and return the finalized status.
                    result = finalizedBlockItem;
                    break;
                }
            }
        } catch (io.grpc.StatusRuntimeException e) {
            // we timed out. Return empty to indicate that the transaction could not be found.
            if (e.getStatus().getCode().equals(Status.Code.DEADLINE_EXCEEDED)) {
                return Optional.empty();
            }
            throw e;
        }
        return result;
    }

    /**
     * Helper function for {@link ClientV2#waitUntilFinalized(Hash, int)}. Retrieves the {@link Optional<FinalizedBlockItem>} of the transaction if it is finalized.
     *
     * @param transactionHash the {@link Hash} of the transaction to wait for.
     * @return {@link Optional<FinalizedBlockItem>} of the transaction if it is finalized, Empty otherwise.
     */
    private Optional<FinalizedBlockItem> getFinalizedTransaction(Hash transactionHash) {
        try {
            BlockItemStatus status = this.getBlockItemStatus(transactionHash);
            if (status.getFinalizedBlockItem().isPresent()) {
                return Optional.of(status.getFinalizedBlockItem().get());
            }
            // Only return a finalized transaction.
            return Optional.empty();
        } catch (io.grpc.StatusRuntimeException e) {
            // If the transaction is not found then return empty.
            if (e.getStatus().getCode().equals(Status.Code.NOT_FOUND)) {
                return Optional.empty();
            }
            // report back any other exceptions.
            throw e;
        }
    }

    /**
     * Find a finalized block with the lowest height that satisfies the given condition. If a block is not found return {@link Optional#empty()}.<p>
     * <p>
     * The provided `test` method should, given a {@link ClientV2} and a {@link BlockQuery},
     * return {@link Optional#of(T)} if the object is found in the block, and {@link Optional#empty()} otherwise.
     * It can also throw exceptions which will terminate the search immediately and pass on the exception.<p>
     * <p>
     * The precondition for this method is that the `test` method is monotone, i.e. if a block at height `h` satisfies the test then also a block at height `h+1` does.
     * If this precondition does not hold then the return value from this method is unspecified.<p>
     * <p>
     * Note, this searches the entire chain. Use {@link ClientV2#findAtLowestHeight(Range, BiFunction)} to only search a given range.
     *
     * @param test {@link BiFunction} satisfying the conditions described above.
     * @param <T>  The type to be returned if the search is successful.
     * @return {@link Optional#of(T)} if the search was successful, {@link Optional#empty()} otherwise.
     */
    public <T> Optional<T> findAtLowestHeight(BiFunction<ClientV2, BlockQuery, Optional<T>> test) {
        return findAtLowestHeight(Range.newUnbounded(), test);
    }

    /**
     * Find a finalized block with the lowest height that satisfies the given condition. If a block is not found return {@link Optional#empty()}.<p>
     * <p>
     * The provided `test` method should, given a {@link ClientV2} and a {@link BlockQuery},
     * return {@link Optional#of(T)} if the object is found in the block, and {@link Optional#empty()} otherwise.
     * It can also throw exceptions which will terminate the search immediately and pass on the exception.<p>
     * <p>
     * The precondition for this method is that the `test` method is monotone, i.e. if a block at height `h` satisfies the test then also a block at height `h+1` does.
     * If this precondition does not hold then the return value from this method is unspecified.<p>
     * <p>
     * The search is limited to at most the given range, the upper bound is always at most the last finalized block at the time of the call.
     * If the lower bound is not provided it defaults to 0, if the upper bound is not provided it defaults to the last finalized block at the time of the call.
     *
     * @param range {@link Range} optionally specifying upper and lower bounds of the search.
     * @param test  {@link BiFunction} satisfying the conditions described above.
     * @param <T>   The type to be returned if the search is successful.
     * @return {@link Optional#of(T)} if the search was successful, {@link Optional#empty()} otherwise.
     */
    public <T> Optional<T> findAtLowestHeight(Range<AbsoluteBlockHeight> range, BiFunction<ClientV2, BlockQuery, Optional<T>> test) {
        long start = 0;
        if (range.getLowerBound().isPresent()) {
            start = range.getLowerBound().get().getHeight().getValue();
        }
        long end = this.getConsensusInfo().getLastFinalizedBlockHeight();
        if (range.getUpperBound().isPresent()) {
            end = Math.min(end, range.getUpperBound().get().getHeight().getValue());
        }

        if (end < start) {
            throw new IllegalArgumentException("Start height must be before end height");
        }
        Optional<T> lastFound = Optional.empty();
        while (start < end) {
            long mid = start + (end - start) / 2;
            Optional<T> ok = test.apply(this, BlockQuery.HEIGHT(BlocksAtHeightRequest.newAbsolute(mid)));
            if (ok.isPresent()) {
                end = mid;
                lastFound = ok;
            } else {
                start = mid + 1;
            }
        }

        return lastFound;
    }

    /**
     * Find a block in which the {@link AccountAddress} was created, if it exists and is finalized.
     * The returned {@link FindAccountResponse}, if present, contains the absolute block height, corresponding {@link Hash} and the {@link AccountInfo} at the end of the block.
     * The block is the first block in which the account appears.<p>
     * <p>
     * Note that this is not necessarily the initial state of the account since there can be transactions updating it in the same block that it is created.<p>
     * <p>
     * The search is limited to at most the given range, the upper bound is always at most the last finalized block at the time of the call.
     * If the lower bound is not provided it defaults to 0, if the upper bound is not provided it defaults to the last finalized block at the time of the call.<p>
     * <p>
     * If the account is not found, {@link Optional#empty()} is returned.
     *
     * @param range   {@link Range} optionally specifying upper and lower bounds of the search.
     * @param address The {@link AccountAddress} to search for.
     * @return {@link Optional} containing {@link FindAccountResponse} if the search was successful, {@link Optional#empty()} otherwise.
     */
    public Optional<FindAccountResponse> findAccountCreation(Range<AbsoluteBlockHeight> range, AccountAddress address) {
        return this.findAtLowestHeight(range, (client, height) -> {
            try {
                AccountInfo info = client.getAccountInfo(height, AccountQuery.from(address));
                BlockInfo blockInfo = client.getBlockInfo(height);
                FindAccountResponse response = FindAccountResponse.builder()
                        .absoluteBlockHeight(AbsoluteBlockHeight.from(blockInfo.getBlockHeight()))
                        .accountInfo(info)
                        .blockHash(blockInfo.getBlockHash())
                        .build();
                return Optional.of(response);
            } catch (io.grpc.StatusRuntimeException e) {
                if (e.getStatus().getCode().equals(Status.Code.NOT_FOUND)) {
                    return Optional.empty();
                }
                throw e;
            }
        });
    }

    /**
     * Find a block in which the {@link AccountAddress} was created, if it exists and is finalized.
     * The returned {@link FindAccountResponse}, if present, contains the absolute block height, corresponding {@link Hash} and the {@link AccountInfo} at the end of the block.
     * The block is the first block in which the account appears.<p>
     * <p>
     * Note that this is not necessarily the initial state of the account since there can be transactions updating it in the same block that it is created.<p>
     * <p>
     * If the account is not found, {@link Optional#empty()} is returned.<p>
     * Note, this searches the entire chain. Use {@link ClientV2#findAccountCreation(Range, AccountAddress)} to only search a given range.
     *
     * @param address The {@link AccountAddress} to search for.
     * @return {@link Optional} containing {@link FindAccountResponse} if the search was successful, {@link Optional#empty()} otherwise.
     */
    public Optional<FindAccountResponse> findAccountCreation(AccountAddress address) {
        return findAccountCreation(Range.newUnbounded(), address);
    }

    /**
     * Get a {@link ImmutableList} of live blocks at a given height.
     *
     * @param height {@link BlocksAtHeightRequest} with the height to query at.
     * @return {@link ImmutableList} of {@link Hash} of live blocks at the specified height.
     */
    public ImmutableList<Hash> getBlocksAtHeight(BlocksAtHeightRequest height) {
        val output = this.server().getBlocksAtHeight(to(height)).getBlocksList();
        val list = new ImmutableList.Builder<Hash>();
        output.forEach(hash -> list.add(to(hash)));
        return list.build();
    }

    /**
     * Get a {@link FinalizedBlockItemIterator} of {@link BlockIdentifier} of finalized blocks starting from a given height.
     * This function starts a {@link Thread} that listens for new finalized blocks.
     * This {@link Thread} is killed when the {@link FinalizedBlockItemIterator} dropped with {@link FinalizedBlockItemIterator#drop()}
     *
     * @param startHeight {@link AbsoluteBlockHeight} to start at.
     * @return {@link FinalizedBlockItemIterator} containing {@link BlockIdentifier}s of finalized blocks.
     */
    public FinalizedBlockItemIterator getFinalizedBlocksFrom(AbsoluteBlockHeight startHeight) {
        return new FinalizedBlockItemIterator(this, startHeight);
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
