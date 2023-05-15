package com.concordium.sdk.responses.branch;

import com.concordium.sdk.serializing.JsonMapper;
import com.concordium.sdk.transactions.Hash;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.ImmutableList;
import concordium.ConcordiumP2PRpc;
import lombok.*;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

/**
 * Represents a Tree of Block {@link Hash}es, The branches above the last finalized block.
 * With head represented by {@link Branch#blockHash} and Children represented by {@link Branch#children}.
 */
@ToString
@Getter
@Builder
@Jacksonized
@EqualsAndHashCode
public class Branch {

    /**
     * Hash of the block at the HEAD of this branch.
     */
    @JsonProperty("blockHash")
    private final Hash blockHash;

    /**
     * Children of the {@link Branch#blockHash}.
     */
    @JsonProperty("children")
    @Singular
    private final ImmutableList<Branch> children;

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
