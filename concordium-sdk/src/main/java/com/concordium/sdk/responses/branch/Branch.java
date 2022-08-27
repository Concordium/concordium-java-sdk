package com.concordium.sdk.responses.branch;

import com.concordium.sdk.serializing.JsonMapper;
import com.concordium.sdk.transactions.Hash;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import concordium.ConcordiumP2PRpc;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

/**
 * Represents a Tree of Block {@link Hash}es, The branches above the last finalized block.
 * With head represented by {@link Branch#blockHash} and Children represented by {@link Branch#children}.
 */
@ToString
@Getter
public class Branch {

    /**
     * Hash of the block at the HEAD of this branch.
     */
    private final Hash blockHash;

    /**
     * Children of the {@link Branch#blockHash}.
     */
    private final List<Branch> children;

    @JsonCreator
    public Branch(
            @JsonProperty("blockHash") final Hash blockHash,
            @JsonProperty("children") final List<Branch> children) {
        this.blockHash = blockHash;
        this.children = children;
    }

    /**
     * Parses {@link concordium.ConcordiumP2PRpc.JsonResponse} to {@link Branch}
     *
     * @param branches {@link concordium.ConcordiumP2PRpc.JsonResponse}
     * @return Parsed {@link Branch}
     */
    public static Branch fromJson(ConcordiumP2PRpc.JsonResponse branches) {
        try {
            return JsonMapper.INSTANCE.readValue(branches.getValue(), Branch.class);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Cannot parse Branch JSON", e);
        }
    }
}
