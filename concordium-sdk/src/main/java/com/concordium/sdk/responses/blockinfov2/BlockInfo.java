package com.concordium.sdk.responses.blockinfov2;

import com.concordium.sdk.responses.BakerId;
import com.concordium.sdk.responses.ProtocolVersion;
import com.concordium.sdk.transactions.Hash;
import com.concordium.sdk.types.Timestamp;
import com.concordium.sdk.types.UInt32;
import com.concordium.sdk.types.UInt64;
import lombok.*;

/**
 * Contains information about a specific block, such as height, timings and transaction counts.
 * Response for getBlockInfo.
 */
@Getter
@EqualsAndHashCode
@ToString
@Builder
public class BlockInfo {

    /**
     * Hash of the block.
     */
    private Hash hash;
    /**
     * Absolute height of the block, height 0 is the genesis block.
     */
    private UInt64 height;
    /**
     * The parent block hash. For a re-genesis block, this will be the terminal block of the
     * previous chain. For the initial genesis block, this will be the hash of the block itself.
     */
    private Hash parentBlock;
    /**
     * The last finalized block when this block was baked.
     */
    private Hash lastFinalizedBlock;
    /**
     * The genesis index for this block. This counts the number of protocol updates that have
     * preceded this block, and defines the era of the block.
     */
    private UInt32 genesisIndex;
    /**
     * The height of this block relative to the (re)genesis block of its era.
     */
    private UInt64 eraBlockHeight;
    /**
     * The time the block was received.
     */
    private Timestamp receiveTime;
    /**
     * The time the block was verified.
     */
    private Timestamp arriveTime;
    /**
     * The slot number in which the block was baked. Present in protocol versions 1-5.
     * TODO handle optional.
     */
    private UInt64 slotNumber;
    /**
     * The time of the slot in which the block was baked.
     */
    private Timestamp slotTime;
    /**
     * The baker id of account baking this block. Not provided for a genesis block.
     * TODO handle optional.
     */
    private BakerId baker;
    /**
     * Whether the block is finalized.
     */
    private boolean finalized;
    /**
     * The number of transactions in the block.
     */
    private UInt32 transactionCount;
    /**
     * The energy cost of the transactions in the block.
     * TODO change to Energy wrapper when available.
     */
    private UInt64 transactionEnergyCost;
    /**
     * The total byte size of all transactions in the block.
     */
    private UInt32 transactionSize;
    /**
     * The hash of the block state after this block.
     */
    private Hash stateHash;
    /**
     * Protocol version to which the block belongs.
     */
    private ProtocolVersion protocolVersion;
    /**
     * Block round. Present from protocol version 6.
     * TODO handle optional.
     */
    private UInt64 round;
    /**
     * Block epoch. Present from protocol version 6.
     * TODO handle optional.
     */
    private UInt64 epoch;

    /**
     * Parses {@link com.concordium.grpc.v2.BlockInfo} to {@link BlockInfo}.
     * @param info {@link com.concordium.grpc.v2.BlockInfo} returned by the GRPC V2 API.
     * @return parsed {@link BlockInfo}.
     */
    public static BlockInfo parse(com.concordium.grpc.v2.BlockInfo info) {
        val builder = getDefaultBuilder(info);
        return null;
    }

    /**
     * Helper method for parse method. Creates builder with always present fields set.
     * @param info {@link com.concordium.grpc.v2.BlockInfo} returned by the GRPC V2 API.
     * @return {@link BlockInfoBuilder} with always present fields set.
     */
    private static BlockInfoBuilder getDefaultBuilder(com.concordium.grpc.v2.BlockInfo info) {
        return BlockInfo.builder()
                .hash(Hash.from(info.getHash().getValue().toByteArray()))
                .height(UInt64.from(info.getHeight().getValue()))
                .parentBlock(Hash.from(info.getParentBlock().getValue().toByteArray()))
                .lastFinalizedBlock(Hash.from(info.getLastFinalizedBlock().getValue().toByteArray()))
                .genesisIndex(UInt32.from(info.getGenesisIndex().getValue()))
                .eraBlockHeight(UInt64.from(info.getEraBlockHeight().getValue()))
                .receiveTime(Timestamp.newMillis(info.getReceiveTime().getValue()))
                .arriveTime(Timestamp.newMillis(info.getArriveTime().getValue()))
                .slotTime(Timestamp.newMillis(info.getSlotTime().getValue()))
                .finalized(info.getFinalized())
                .transactionCount(UInt32.from(info.getTransactionCount()))
                .transactionEnergyCost(UInt64.from(info.getTransactionsEnergyCost().getValue()))
                .transactionSize(UInt32.from(info.getTransactionsSize()))
                .stateHash(Hash.from(info.getStateHash().getValue().toByteArray()))
                .protocolVersion(ProtocolVersion.parse(info.getProtocolVersion()));
    }
}
