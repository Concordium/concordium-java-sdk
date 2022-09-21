package com.concordium.sdk.responses.ancestors;

import com.concordium.sdk.serializing.JsonMapper;
import com.concordium.sdk.transactions.Hash;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.ImmutableList;
import concordium.ConcordiumP2PRpc;
import lombok.val;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class Ancestors {
    public static Optional<ImmutableList<Hash>> fromJson(ConcordiumP2PRpc.JsonResponse jsonResponse) {
        try {
            val ret = JsonMapper.INSTANCE.readValue(jsonResponse.getValue(), String[].class);
            return Objects.isNull(ret) ? Optional.empty() : Optional.of(toImmutableList(ret));
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Cannot parse BlockAncestors JSON", e);
        }
    }

    private static ImmutableList<Hash> toImmutableList(String[] ret) {
        return ImmutableList.<Hash>builder().addAll(toHashList(ret)).build();
    }

    private static List<Hash> toHashList(String[] ret) {
        return Arrays.stream(ret).map(h -> Hash.from(h)).collect(Collectors.toList());
    }
}
