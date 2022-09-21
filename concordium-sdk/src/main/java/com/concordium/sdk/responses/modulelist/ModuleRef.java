package com.concordium.sdk.responses.modulelist;

import com.concordium.sdk.serializing.JsonMapper;
import com.concordium.sdk.transactions.Hash;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.ImmutableList;
import concordium.ConcordiumP2PRpc;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

public class ModuleRef extends Hash {
    @JsonCreator
    ModuleRef(String encoded) {
        super(encoded);
    }

    public static ModuleRef from(String hexHash) {
        return new ModuleRef(hexHash);
    }

    public static Optional<ImmutableList<ModuleRef>> fromJsonArray(ConcordiumP2PRpc.JsonResponse res) {
        try {
            String[] parsed = JsonMapper.INSTANCE.readValue(res.getValue(), String[].class);

            return Optional.ofNullable(parsed)
                    .map(array -> Arrays.stream(array).map(i -> ModuleRef.from(i)).collect(Collectors.toList()))
                    .map(l -> ImmutableList.copyOf(l));
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Cannot parse Module Ref Array JSON", e);
        }
    }
}
