package com.concordium.sdk.responses.modulesource;

import concordium.ConcordiumP2PRpc;
import lombok.Builder;
import lombok.Getter;

/**
 * Parsed response of
 * {@link concordium.P2PGrpc.P2PBlockingStub#getModuleSource(ConcordiumP2PRpc.GetModuleSourceRequest)}
 */
@Builder
@Getter
public class ModuleSource {

    /**
     * Module source bytes.
     */
    private final byte[] bytes;
}
