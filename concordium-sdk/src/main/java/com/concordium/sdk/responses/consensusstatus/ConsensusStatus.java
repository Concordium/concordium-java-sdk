package com.concordium.sdk.responses.consensusstatus;

import com.concordium.sdk.serializing.JsonMapper;
import com.concordium.sdk.transactions.Hash;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Getter;
import lombok.ToString;

import java.util.Date;
import java.util.Objects;

@Getter
@ToString
public final class ConsensusStatus {
    private Hash bestBlock;
    private Hash genesisBlock;
    private final Date genesisTime;
    private final int slotDuration;
    private final int epochDuration;
    private Hash lastFinalizedBlock;
    private final int bestBlockHeight;
    private final int lastFinalizedBlockHeight;
    private final int blocksReceivedCount;
    private final String blockLastReceivedTime;
    private final double blockReceiveLatencyEMA;
    private final double blockReceiveLatencyEMSD;
    private final double blockReceivePeriodEMA;
    private final double blockReceivePeriodEMSD;
    private final int blocksVerifiedCount;
    private final String blockLastArrivedTime;
    private final double blockArriveLatencyEMA;
    private final double blockArriveLatencyEMSD;
    private final double blockArrivePeriodEMA;
    private final double blockArrivePeriodEMSD;
    private final double transactionsPerBlockEMA;
    private final double transactionsPerBlockEMSD;
    private final int finalizationCount;
    private final String lastFinalizedTime;
    private final double finalizationPeriodEMA;
    private final double finalizationPeriodEMSD;
    private final int protocolVersion;
    private final int genesisIndex;
    private final String currentEraGenesisBlock;
    private final Date currentEraGenesisTime;

    public static ConsensusStatus fromJson(String json) {
        try {
            return JsonMapper.INSTANCE.readValue(json, ConsensusStatus.class);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Cannot parse ConsensusStatus JSON", e);
        }
    }

    @JsonCreator
    ConsensusStatus(@JsonProperty("bestBlock") String bestBlock,
                    @JsonProperty("genesisBlock") String genesisBlock,
                    @JsonProperty("genesisTime") Date genesisTime,
                    @JsonProperty("slotDuration") int slotDuration,
                    @JsonProperty("epochDuration") int epochDuration,
                    @JsonProperty("lastFinalizedBlock") String lastFinalizedBlock,
                    @JsonProperty("bestBlockHeight") int bestBlockHeight,
                    @JsonProperty("lastFinalizedBlockHeight") int lastFinalizedBlockHeight,
                    @JsonProperty("blocksReceivedCount") int blocksReceivedCount,
                    @JsonProperty("blockLastReceivedTime") String blockLastReceivedTime,
                    @JsonProperty("blockReceiveLatencyEMA") double blockReceiveLatencyEMA,
                    @JsonProperty("blockReceiveLatencyEMSD") double blockReceiveLatencyEMSD,
                    @JsonProperty("blockReceivePeriodEMA") double blockReceivePeriodEMA,
                    @JsonProperty("blockReceivePeriodEMSD") double blockReceivePeriodEMSD,
                    @JsonProperty("blocksVerifiedCount") int blocksVerifiedCount,
                    @JsonProperty("blockLastArrivedTime") String blockLastArrivedTime,
                    @JsonProperty("blockArriveLatencyEMA") double blockArriveLatencyEMA,
                    @JsonProperty("blockArriveLatencyEMSD") double blockArriveLatencyEMSD,
                    @JsonProperty("blockArrivePeriodEMA") double blockArrivePeriodEMA,
                    @JsonProperty("blockArrivePeriodEMSD") double blockArrivePeriodEMSD,
                    @JsonProperty("transactionsPerBlockEMA") double transactionsPerBlockEMA,
                    @JsonProperty("transactionsPerBlockEMSD") double transactionsPerBlockEMSD,
                    @JsonProperty("finalizationCount") int finalizationCount,
                    @JsonProperty("lastFinalizedTime") String lastFinalizedTime,
                    @JsonProperty("finalizationPeriodEMA") double finalizationPeriodEMA,
                    @JsonProperty("finalizationPeriodEMSD") double finalizationPeriodEMSD,
                    @JsonProperty("protocolVersion") int protocolVersion,
                    @JsonProperty("genesisIndex") int genesisIndex,
                    @JsonProperty("currentEraGenesisBlock") String currentEraGenesisBlock,
                    @JsonProperty("currentEraGenesisTime") Date currentEraGenesisTime) {
        if (!Objects.isNull(bestBlock)) {
            this.bestBlock = Hash.from(bestBlock);
        }
        if (!Objects.isNull(genesisBlock)) {
            this.genesisBlock = Hash.from(genesisBlock);
        }
        if (!Objects.isNull(lastFinalizedBlock)) {
            this.lastFinalizedBlock = Hash.from(lastFinalizedBlock);
        }
        this.genesisTime = genesisTime;
        this.slotDuration = slotDuration;
        this.epochDuration = epochDuration;
        this.bestBlockHeight = bestBlockHeight;
        this.lastFinalizedBlockHeight = lastFinalizedBlockHeight;
        this.blocksReceivedCount = blocksReceivedCount;
        this.blockLastReceivedTime = blockLastReceivedTime;
        this.blockReceiveLatencyEMA = blockReceiveLatencyEMA;
        this.blockReceiveLatencyEMSD = blockReceiveLatencyEMSD;
        this.blockReceivePeriodEMA = blockReceivePeriodEMA;
        this.blockReceivePeriodEMSD = blockReceivePeriodEMSD;
        this.blocksVerifiedCount = blocksVerifiedCount;
        this.blockLastArrivedTime = blockLastArrivedTime;
        this.blockArriveLatencyEMA = blockArriveLatencyEMA;
        this.blockArriveLatencyEMSD = blockArriveLatencyEMSD;
        this.blockArrivePeriodEMA = blockArrivePeriodEMA;
        this.blockArrivePeriodEMSD = blockArrivePeriodEMSD;
        this.transactionsPerBlockEMA = transactionsPerBlockEMA;
        this.transactionsPerBlockEMSD = transactionsPerBlockEMSD;
        this.finalizationCount = finalizationCount;
        this.lastFinalizedTime = lastFinalizedTime;
        this.finalizationPeriodEMA = finalizationPeriodEMA;
        this.finalizationPeriodEMSD = finalizationPeriodEMSD;
        this.protocolVersion = protocolVersion;
        this.genesisIndex = genesisIndex;
        this.currentEraGenesisBlock = currentEraGenesisBlock;
        this.currentEraGenesisTime = currentEraGenesisTime;
    }
}
