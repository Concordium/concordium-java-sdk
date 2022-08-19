package com.concordium.sdk.responses.branch;

import com.concordium.sdk.serializing.JsonMapper;
import com.concordium.sdk.transactions.Hash;
import com.fasterxml.jackson.core.JsonProcessingException;
import concordium.ConcordiumP2PRpc;
import lombok.*;

import java.util.List;

@Data
@ToString
@RequiredArgsConstructor
public class Branch {

    /**
     * Hash of the block at the HEAD of this branch.
     */
    private Hash blockHash;

    /**
     * Children of the {@link Branch#blockHash}.
     */
    private List<Branch> children;

    /**
     * Parses {@link concordium.ConcordiumP2PRpc.JsonResponse} to {@link Branch}
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
