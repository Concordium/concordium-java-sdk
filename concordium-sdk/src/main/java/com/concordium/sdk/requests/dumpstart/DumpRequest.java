package com.concordium.sdk.requests.dumpstart;

import lombok.Builder;
import lombok.Getter;

import java.nio.file.Path;

/**
 * The dump request used when requesting dumping of packages.
 * See {@link com.concordium.sdk.ClientV2#dumpStart(DumpRequest)}
 */
@Builder
@Getter
public class DumpRequest {

    /**
     * Which file to dump the packages into
     * Requires a valid path
     */
    private final Path path;

    /**
     * Whether the node should dump raw packages.
     */
    private boolean raw;
}
