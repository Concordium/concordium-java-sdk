package com.concordium.sdk.responses.getancestors;

import com.concordium.sdk.serializing.JsonMapper;
import com.concordium.sdk.transactions.Hash;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.ImmutableList;
import concordium.ConcordiumP2PRpc;
import lombok.Builder;
import lombok.Getter;
import lombok.val;

import java.util.Arrays;
import java.util.stream.Collectors;

@Builder
@Getter
/**
 * Represents Ancestors of a Block.
 */
public class BlockAncestors {

    /**
     * Block Hash of the Ancestors for a Block.
     */
    private final ImmutableList<Hash> ancestors;

    /**
     * Parses {@link BlockAncestors} from {@link concordium.ConcordiumP2PRpc.JsonResponse}.
     * @param jsonResponse {@link concordium.ConcordiumP2PRpc.JsonResponse} from Node.
     * @return Parsed {@link BlockAncestors}
     */
    public static BlockAncestors fromJson(ConcordiumP2PRpc.JsonResponse jsonResponse) {
        try {
            val ret = JsonMapper.INSTANCE.readValue(jsonResponse.getValue(), String[].class);
            val items = Arrays.stream(ret).map(h->Hash.from(h)).collect(Collectors.toList());
            val ancestors = ImmutableList.<Hash>builder().addAll(items).build();

            return BlockAncestors.builder().ancestors(ancestors).build();
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Cannot parse BlockAncestors JSON", e);
        }
    }
}
