package com.concordium.sdk.responses.modulelist;

import com.concordium.sdk.serializing.JsonMapper;
import com.concordium.sdk.transactions.Hash;
import com.fasterxml.jackson.core.JsonProcessingException;
import concordium.ConcordiumP2PRpc;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ModuleRef {
    public static List<Hash> fromJsonArray(ConcordiumP2PRpc.JsonResponse res) {
        try {
            String[] parsed = JsonMapper.INSTANCE.readValue(res.getValue(), String[].class);
            return Arrays.stream(parsed).map(i -> Hash.from(i)).collect(Collectors.toList());
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Cannot parse Module Ref Array JSON", e);
        }
    }
}
