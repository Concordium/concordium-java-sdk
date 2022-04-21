package com.concordium.sdk.responses;

import com.concordium.sdk.serializing.JsonMapper;
import com.concordium.sdk.transactions.Hash;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

/**
 * BlocksAtHeight
 */
@Getter
@ToString
public class BlocksAtHeight {
    private final List<Hash> blocks;

    @JsonCreator
    BlocksAtHeight(List<Hash> blocks) {
        this.blocks = blocks;
    }

    public static BlocksAtHeight fromJson(String blocksAtHeightJsonString) {
        try {
            return JsonMapper.INSTANCE.readValue(blocksAtHeightJsonString, BlocksAtHeight.class);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Cannot parse AccountInfo JSON", e);
        }
    }
}
