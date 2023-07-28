package com.concordium.sdk.responses.modulelist;

import com.concordium.sdk.serializing.JsonMapper;
import com.concordium.sdk.transactions.Hash;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.ImmutableList;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * A reference to a module deployed on chain.
 * In particular the {@link ModuleRef} is a {@link Hash} of the deployed module.
 */
public class ModuleRef extends Hash {
    @JsonCreator
    ModuleRef(String encoded) {
        super(encoded);
    }

    ModuleRef(final byte[] bytes) {
        super(bytes);
    }

    public static ModuleRef from(String hexHash) {
        return new ModuleRef(hexHash);
    }

    public static ModuleRef from(final byte[] bytes) {
        return new ModuleRef(bytes);
    }

    public static Optional<ImmutableList<ModuleRef>> moduleRefsFromJsonArray(String jsonValue) {
        try {
            String[] parsed = JsonMapper.INSTANCE.readValue(jsonValue, String[].class);

            return Optional.ofNullable(parsed)
                    .map(array -> Arrays.stream(array).map(ModuleRef::from).collect(Collectors.toList()))
                    .map(ImmutableList::copyOf);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Cannot parse Module Ref Array JSON", e);
        }
    }

    public static ModuleRef from(com.concordium.grpc.v2.ModuleRef originRef) {
        return ModuleRef.from(originRef.getValue().toByteArray());
    }
}
