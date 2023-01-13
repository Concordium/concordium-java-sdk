package com.concordium.sdk.v2.types;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nullable;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class BlockHashInput {
    public static final BlockHashInput BEST = new BlockHashInput(Type.BEST, null);
    public static final BlockHashInput LAST_FINAL = new BlockHashInput(Type.LAST_FINAL, null);

    public static BlockHashInput GIVEN(BlockHash hash) {
        return new BlockHashInput(Type.GIVEN, hash);
    }

    @Getter
    private final Type type;

    @Getter
    @Nullable
    private final BlockHash blockHash;
}

enum Type {
    BEST,
    LAST_FINAL,
    GIVEN
}
