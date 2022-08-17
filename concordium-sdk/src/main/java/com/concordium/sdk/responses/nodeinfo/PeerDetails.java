package com.concordium.sdk.responses.nodeinfo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Peer Details in {@link NodeInfo}.
 */
@RequiredArgsConstructor
@Getter
public class PeerDetails {

    /**
     * Type of Peer. Bootstrapper Or Node.
     */
    private final PeerType type;
}
