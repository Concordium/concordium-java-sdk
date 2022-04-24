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
    private Hash blockHash;
    private Integer transactionEnergyCost;
    private Integer blockBaker;
    private Hash blockStateHash;
    private OffsetDateTime blockSlotTime;
    private Hash blockParent;
    private OffsetDateTime blockReceiveTime;
    private Integer genesisIndex;
    private Integer blockSlot;
    private Boolean finalized;
    private Integer eraBlockHeight;
    private Hash blockLastFinalized;
    private Integer transactionsSize;
    private Integer blockHeight;
    private Integer transactionCount;
    private OffsetDateTime blockArriveTime;
    public static BlockInfo fromJson(String blockInfoJsonString) {
        try {
            return JsonMapper.INSTANCE.readValue(blockInfoJsonString, BlockInfo.class);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Cannot parse BlockInfo JSON", e);
        }
    }

}
