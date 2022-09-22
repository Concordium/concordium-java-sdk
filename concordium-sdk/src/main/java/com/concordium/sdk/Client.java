package com.concordium.sdk;

import com.concordium.sdk.exceptions.*;
import com.concordium.sdk.requests.getaccountinfo.AccountRequest;
import com.concordium.sdk.responses.AccountIndex;
import com.concordium.sdk.responses.BakerId;
import com.concordium.sdk.responses.accountinfo.AccountInfo;
import com.concordium.sdk.responses.ancestors.Ancestors;
import com.concordium.sdk.responses.birkparamsters.BirkParameters;
import com.concordium.sdk.responses.blockinfo.BlockInfo;
import com.concordium.sdk.responses.blocksatheight.BlocksAtHeight;
import com.concordium.sdk.responses.blocksatheight.BlocksAtHeightRequest;
import com.concordium.sdk.responses.blocksummary.BlockSummary;
import com.concordium.sdk.responses.blocksummary.updates.queues.AnonymityRevokerInfo;
import com.concordium.sdk.responses.blocksummary.updates.queues.IdentityProviderInfo;
import com.concordium.sdk.responses.branch.Branch;
import com.concordium.sdk.responses.consensusstatus.ConsensusStatus;
import com.concordium.sdk.responses.cryptographicparameters.CryptographicParameters;
import com.concordium.sdk.responses.peerStats.PeerStatistics;
import com.concordium.sdk.responses.peerlist.Peer;
import com.concordium.sdk.responses.poolstatus.PoolStatus;
import com.concordium.sdk.responses.intanceinfo.InstanceInfo;
import com.concordium.sdk.responses.modulelist.ModuleRef;
import com.concordium.sdk.responses.modulesource.ModuleSource;
import com.concordium.sdk.responses.nodeinfo.NodeInfo;
import com.concordium.sdk.responses.peerStats.PeerStatistics;
import com.concordium.sdk.responses.peerlist.Peer;
import com.concordium.sdk.responses.rewardstatus.RewardsOverview;
import com.concordium.sdk.responses.transactionstatus.ContractAddress;
import com.concordium.sdk.responses.transactionstatus.TransactionStatus;
import com.concordium.sdk.transactions.AccountAddress;
import com.concordium.sdk.transactions.AccountNonce;
import com.concordium.sdk.transactions.Hash;
import com.concordium.sdk.transactions.Transaction;
import com.google.common.collect.ImmutableList;
import com.google.protobuf.ByteString;
import com.concordium.sdk.types.UInt16;
import com.google.common.collect.ImmutableList;
import com.google.protobuf.ByteString;
import com.google.protobuf.Int32Value;
import concordium.ConcordiumP2PRpc;
import concordium.P2PGrpc;
import io.grpc.ManagedChannel;
import lombok.val;
import org.semver4j.Semver;

import java.io.IOException;
import java.net.UnknownHostException;
import java.time.Duration;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

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
     * @param blockHash      the block hash
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
     *
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
     * Gets the Node information.
     * @return Parsed {@link NodeInfo}
     */
    public NodeInfo getNodeInfo() {
        val value = server().nodeInfo(ConcordiumP2PRpc.Empty.newBuilder().build());

        return NodeInfo.parse(value);
    }

    /**
     * Gets the Peer uptime.
     *
     * @return Peer Uptime {@link Duration}.
     */
    public Duration getUptime() {
        val res = server().peerUptime(ConcordiumP2PRpc.Empty.newBuilder().build()).getValue();
        return Duration.ofMillis(res);
    }

    /**
     * Gets the total number of packets sent.
     *
     * @return Total number of packets sent.
     */
    public long getTotalSent() {
        return server().peerTotalSent(ConcordiumP2PRpc.Empty.newBuilder().build()).getValue();
    }

    /**
     * Query for the total number of packets that the node has received thus far.
     *
     * @return Total number of received packets.
     */
    public long getTotalReceived() {
        return server().peerTotalReceived(ConcordiumP2PRpc.Empty.newBuilder().build()).getValue();
    }

    /**
     * Gets Peers list connected to the Node
     *
     * @param includeBootstrappers if true will include Bootstrapper nodes in the response.
     * @return An {@link ImmutableList} of {@link Peer}
     * @throws UnknownHostException When the returned IP address of Peer is Invalid.
     */
    public ImmutableList<Peer> getPeerList(boolean includeBootstrappers) throws UnknownHostException {
        val req = ConcordiumP2PRpc.PeersRequest.newBuilder()
                .setIncludeBootstrappers(includeBootstrappers)
                .build();
        val value = server().peerList(req).getPeersList();

        return Peer.toList(value);
    }

    /**
     * Gets {@link PeerStatistics} of the node.
     *
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
     * Gets the Semantic Version of the Peer Software / Node
     *
     * @return Version of the Peer / Node
     */
    public Semver getVersion() {
        val versionString = server().peerVersion(ConcordiumP2PRpc.Empty.newBuilder().build()).getValue();
        return new Semver(versionString);
    }

    /**
     * Get the IDs of the bakers registered in the given block.
     *
     * @param blockHash {@link Hash} of the block bakers are to be retrieved.
     * @return Parsed {@link ImmutableList} of {@link BakerId}
     * @throws BlockNotFoundException When the returned JSON is null.
     */
    public ImmutableList<BakerId> getBakerList(Hash blockHash) throws BlockNotFoundException {
        val res = server()
                .getBakerList(ConcordiumP2PRpc.BlockHash.newBuilder().setBlockHash(blockHash.asHex()).build());

        BakerId[] bakerIdArray = BakerId.fromJsonArray(res);
        if (Objects.isNull(bakerIdArray)) {
            throw BlockNotFoundException.from(blockHash);
        }

        return ImmutableList.copyOf(bakerIdArray);
    }

    /**
     * Get the status of a given baker pool or passive delegation at the given block.
     *
     * @param blockHash {@link Hash} of the block Pool Status is to be retrieved at.
     * @param bakerId   {@link Optional<BakerId>} of the Baker of which pool is to be retrieved.
     * @return Parsed {@link PoolStatus}.
     */
    public PoolStatus getPoolStatus(
            final Hash blockHash,
            final Optional<BakerId> bakerId) throws PoolNotFoundException {
        final BakerId parsedBakerId = bakerId.orElse(BakerId.from(0));
        val req = ConcordiumP2PRpc.GetPoolStatusRequest.newBuilder()
                .setBlockHash(blockHash.asHex())
                .setPassiveDelegation(!bakerId.isPresent())
                .setBakerId(parsedBakerId.getId().getIndex().getValue())
                .build();
        val res = server().getPoolStatus(req);

        return PoolStatus.fromJson(res).orElseThrow(() -> PoolNotFoundException.from(parsedBakerId, blockHash));
    }
    
    /**
     * Get the source of a smart contract module.
     *
     * @param moduleRef {@link ModuleRef} of module to retrieve.
     * @param blockHash {@link Hash} of the Block at which the module source is to be retrieved.
     * @return Parsed {@link ModuleSource}.
     * @throws ModuleNotFoundException When module cannot be found.
     */
    public ModuleSource getModuleSource(ModuleRef moduleRef, Hash blockHash) throws ModuleNotFoundException {
        val res = server()
                .getModuleSource(ConcordiumP2PRpc.GetModuleSourceRequest.newBuilder()
                        .setBlockHash(blockHash.asHex())
                        .setModuleRef(moduleRef.asHex())
                        .build());

        Optional<ModuleSource> moduleSource = res.getValue().isEmpty()
                ? Optional.empty()
                : Optional.of(ModuleSource.from(res.getValue().toByteArray()));

        return moduleSource.orElseThrow(() -> ModuleNotFoundException.from(blockHash, moduleRef));
    }

    /**
     * Get the list of smart contract modules in the given block.
     *
     * @param blockHash {@link Hash} of block at which the modules list is being retrieved.
     * @return Parsed {@link ImmutableList} of {@link Hash}
     * @throws Exception When the returned JSON is null.
     */
    public ImmutableList<ModuleRef> getModuleList(final Hash blockHash) throws BlockNotFoundException {
        val res = server().getModuleList(ConcordiumP2PRpc.BlockHash.newBuilder()
                .setBlockHash(blockHash.asHex())
                .build());

        return ModuleRef.fromJsonArray(res)
                .orElseThrow(() -> BlockNotFoundException.from(blockHash));
    }

    /**
     * Get an overview of the parameters used for baking for the specified block.
     *
     * @param blockHash {@link Hash} of the block at which the parameters need to be retrived.
     * @return Parsed {@link BirkParameters}
     * @throws Exception When the returned response is null.
     */
    public BirkParameters getBirkParameters(Hash blockHash) throws BlockNotFoundException {
        val res = server()
                .getBirkParameters(ConcordiumP2PRpc.BlockHash.newBuilder().setBlockHash(blockHash.asHex()).build());

        return BirkParameters.fromJson(res)
                .orElseThrow(() -> BlockNotFoundException.from(blockHash));
    }

    /**
     * Shut down the node.
     *
     * @return whether it was shutdown or not.
     */
    public boolean shutdown() {
        return server().shutdown(ConcordiumP2PRpc.Empty.newBuilder().build()).getValue();
    }

    /**
     * Ask the node to join the specified network.
     *
     * @param networkId {@link UInt16} Network ID.
     * @return true if network has been joined successfully. False otherwise.
     */
    public boolean joinNetwork(final UInt16 networkId) {
        return server().joinNetwork(ConcordiumP2PRpc.NetworkChangeRequest.newBuilder()
                .setNetworkId(Int32Value.newBuilder().setValue(networkId.getValue()).build())
                .build()).getValue();
    }

    /**
     * Ask the node to leave the specified network.
     *
     * @param networkId {@link UInt16} Network ID.
     * @return true if network has been left successfully. False otherwise.
     */
    public boolean leaveNetwork(final UInt16 networkId) {
        return server().leaveNetwork(ConcordiumP2PRpc.NetworkChangeRequest.newBuilder()
                .setNetworkId(Int32Value.newBuilder().setValue(networkId.getValue()).build())
                .build()).getValue();
    }

    /**
     * Get the smart contract instance information given the {@link ContractAddress} for the provided block {@link Hash}.
     *
     * @param contractAddress {@link ContractAddress}
     * @param blockHash       {@link Hash} of the block
     * @return The {@link InstanceInfo}.
     * @throws ContractInstanceNotFoundException When the contract instance could not be found for the provided block hash.
     */
    public InstanceInfo getInstanceInfo(final ContractAddress contractAddress, final Hash blockHash)
            throws ContractInstanceNotFoundException {
        val grpcReq = ConcordiumP2PRpc.GetAddressInfoRequest
                .newBuilder()
                .setAddress(contractAddress.toJson())
                .setBlockHash(blockHash.asHex())
                .build();
        val res = server().getInstanceInfo(grpcReq);

        return InstanceInfo.fromJson(res)
                .orElseThrow(() -> ContractInstanceNotFoundException.from(contractAddress, blockHash));
    }

    /**
     * Get the list of smart contract instances in a given block at the time of commitment.
     *
     * @param blockHash {@link Hash} at which the instances need to be fetched.
     * @return {@link ImmutableList} of {@link ContractAddress}.
     * @throws BlockNotFoundException when no block could be found with the provided block {@link Hash}.
     */
    public ImmutableList<ContractAddress> getInstances(Hash blockHash) throws BlockNotFoundException {
        val req = ConcordiumP2PRpc.BlockHash.newBuilder()
                .setBlockHash(blockHash.asHex()).build();
        val res = server().getInstances(req);

        return ContractAddress.toList(res)
                .orElseThrow(() -> BlockNotFoundException.from(blockHash));
    }

    /**
     * Get the list of accounts in the given block.
     *
     * @param blockHash Hash of the block at which to retrieve the accounts.
     * @return An {@link ImmutableList} of {@link AccountAddress}.
     * @throws BlockNotFoundException if an invalid block hash was provided.
     */
    public ImmutableList<AccountAddress> getAccountList(Hash blockHash) throws BlockNotFoundException {
        val req = ConcordiumP2PRpc.BlockHash.newBuilder()
                .setBlockHash(blockHash.asHex())
                .build();
        val res = server().getAccountList(req);

        return AccountAddress.toList(res)
                .orElseThrow(() -> BlockNotFoundException.from(blockHash));
    }

    /**
     * Get a list of banned peers.
     *
     * @return An {@link ImmutableList} of {@link Peer}
     * @throws UnknownHostException When the returned IP address of Peer is Invalid.
     */
    public ImmutableList<Peer> getBannedPeers() throws UnknownHostException {
        val req = ConcordiumP2PRpc.Empty.newBuilder().build();
        final List<ConcordiumP2PRpc.PeerElement> value = server().getBannedPeers(req).getPeersList();

        return Peer.toList(value);
    }

    /**
     * Gets Block Ancestor Blocks.
     *
     * @param blockHash {@link Hash} of the block.
     * @param num       Total no of Ancestor blocks to get.
     * @return {@link ImmutableList} of {@link Hash}
     * @throws BlockNotFoundException When the returned response from Node is invalid or null.
     */
    public ImmutableList<Hash> getAncestors(Hash blockHash, long num) throws BlockNotFoundException {
        val jsonResponse = server().getAncestors(
                ConcordiumP2PRpc.BlockHashAndAmount
                        .newBuilder()
                        .setBlockHash(blockHash.asHex())
                        .setAmount(num)
                        .build());

        return Ancestors
                .fromJson(jsonResponse)
                .orElseThrow(() -> BlockNotFoundException.from(blockHash));
    }

    /**
     * Get the information about total amount of CCD and the state of various special accounts in the provided block.
     *
     * @param blockHash Block at which the reward status is to be retrieved.
     * @return Parsed {@link RewardsOverview}.
     * @throws BlockNotFoundException When the returned response is null.
     */
    public RewardsOverview getRewardStatus(final Hash blockHash) throws BlockNotFoundException {
        val req = ConcordiumP2PRpc.BlockHash.newBuilder()
                .setBlockHash(blockHash.asHex()).build();
        val res = server().getRewardStatus(req);

        return RewardsOverview.fromJson(res)
                .orElseThrow(() -> BlockNotFoundException.from(blockHash));
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
        return Branch.fromJson(server().getBranches(ConcordiumP2PRpc.Empty.newBuilder().build()));
    }

    /**
     * Get the list of identity providers in the given block.
     *
     * @param blockHash Block {@link Hash}.
     * @return {@link ImmutableList} of {@link IdentityProviderInfo}.
     * @throws BlockNotFoundException When an invalid block hash was provided
     */
    public ImmutableList<IdentityProviderInfo> getIdentityProviders(Hash blockHash) throws BlockNotFoundException {
        val req = ConcordiumP2PRpc.BlockHash.newBuilder().setBlockHash(blockHash.asHex()).build();
        val res = server().getIdentityProviders(req);

        return IdentityProviderInfo.fromJsonArray(res.getValue())
                .orElseThrow(() -> BlockNotFoundException.from(blockHash));
    }

    /**
     * Get the list of anonymity revokers in the given block.
     *
     * @param blockHash Block {@link Hash}.
     * @return {@link ImmutableList} of {@link AnonymityRevokerInfo}.
     * @throws BlockNotFoundException When an invalid block hash was provided
     */
    public ImmutableList<AnonymityRevokerInfo> getAnonymityRevokers(Hash blockHash) throws BlockNotFoundException {
        val req = ConcordiumP2PRpc.BlockHash.newBuilder().setBlockHash(blockHash.asHex()).build();
        val res = server().getAnonymityRevokers(req);

        return AnonymityRevokerInfo.fromJsonArray(res.getValue())
                .orElseThrow(() -> BlockNotFoundException.from(blockHash));
    }

    /**
     * Closes the underlying grpc channel
     *
     * This should only be done when the {@link Client}
     * is of no more use as creating a new {@link Client} (and the associated)
     * channel is rather expensive.
     * <p>
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
