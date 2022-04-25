package com.concordium.sdk.responses.blockinfo;

import com.concordium.sdk.responses.accountinfo.AccountInfo;
import com.concordium.sdk.serializing.JsonMapper;
import com.concordium.sdk.transactions.Hash;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.*;

import java.time.OffsetDateTime;

@ToString
@Data
@RequiredArgsConstructor
@Getter
public class BlockInfo {
    /**
     *  Hash of the block.
     */
    private Hash blockHash;
    /**
     *  The total energy consumption of transactions in the block.
     */
    private Integer transactionEnergyCost;
    /**
     *  Identity of the baker of the block. For non-genesis blocks the value is
     *  non-null.
     */
    private Integer blockBaker;
    /**
     *  Hash of the block state at the end of the given block.
     */
    private Hash blockStateHash;
    /**
     *  Slot time of the slot the block is in. In contrast to
     *  {@link BlockInfo#blockArriveTime} this is an objective value, all nodes
     *  agree on it.
     */
    private OffsetDateTime blockSlotTime;
    /**
     *  Parent block pointer.
     */
    private Hash blockParent;
    /**
     *  Time when the block was first received by the node. This can be in
     *  principle quite different from the arrive time if, e.g., block execution
     *  takes a long time, or the block must wait for the arrival of its parent.
     */
    private OffsetDateTime blockReceiveTime;
    /**
     *  The genesis index for this block. This counts the number of protocol
     *  updates that have preceded this block, and defines the era of the
     *  block.
     */
    private Integer genesisIndex;
    /**
     *  Slot number of the slot the block is in.
     */
    private Integer blockSlot;
    /**
     *  Whether the block is finalized or not.
     */
    private Boolean finalized;
    /**
     *  The height of this block relative to the (re)genesis block of its era.
     */
    private Integer eraBlockHeight;
    /**
     *  Pointer to the last finalized block. Each block has a pointer to a
     *  specific finalized block that existed at the time the block was
     *  produced.
     */
    private Hash blockLastFinalized;
    /**
     * Size of all the transactions in the block in bytes.
     */
    private Integer transactionsSize;
    /**
     *  Height of the block from genesis.
     */
    private Integer blockHeight;
    /**
     *  The number of transactions in the block.
     */
    private Integer transactionCount;
    /**
     *  Time when the block was added to the node's tree. This is a subjective
     *  (i.e., node specific) value.
     */
    private OffsetDateTime blockArriveTime;
    public static BlockInfo fromJson(String blockInfoJsonString) {
        try {
            return JsonMapper.INSTANCE.readValue(blockInfoJsonString, BlockInfo.class);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Cannot parse BlockInfo JSON", e);
        }
    }

}
