package com.concordium.sdk.responses.nodeInfo;

import lombok.RequiredArgsConstructor;

/**
 * Peer Details in {@link NodeInfo}.
 */
@RequiredArgsConstructor
public class PeerDetails {

    /**
     * Type of Peer. Bootstrapper Or Node.
     */
    private final PeerType type;
}
