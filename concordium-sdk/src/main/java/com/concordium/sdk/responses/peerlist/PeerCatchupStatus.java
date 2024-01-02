package com.concordium.sdk.responses.peerlist;

import com.concordium.grpc.v2.PeersInfo;

/**
 * Represents Catchup Status of a Node.
 */
public enum PeerCatchupStatus {

    /**
     * Node has the latest updates.
     */
    UP_TO_DATE(0),

    /**
     * Node updates are pending.
     */
    PENDING(1),

    /**
     * Node is downloading new updates.
     */
    CATCHING_UP(2),

    /**
     * Node is a {@link com.concordium.sdk.responses.nodeinfo.PeerType#BOOTSTRAPPER} BOOTSTRAPPER and is not running consensus
     */
    NOT_AVAILABLE(3);

    private final int value;

    PeerCatchupStatus(int value) {
        this.value = value;
    }


    /**
     * Parses the input {@link com.concordium.grpc.v2.PeersInfo.Peer.CatchupStatus}.
     *
     * @param catchupStatus input {@link com.concordium.grpc.v2.PeersInfo.Peer.CatchupStatus}.
     * @return Parsed {@link PeerCatchupStatus}.
     */
    public static PeerCatchupStatus parse(PeersInfo.Peer.CatchupStatus catchupStatus) {
        switch (catchupStatus) {
            case UPTODATE:
                return PeerCatchupStatus.UP_TO_DATE;
            case PENDING:
                return PeerCatchupStatus.PENDING;
            case CATCHINGUP:
                return PeerCatchupStatus.CATCHING_UP;
            default:
                throw new IllegalArgumentException(String.format(
                        "Invalid catchup status received: %d",
                        catchupStatus.getNumber()));
        }
    }

    /**
     * Gets the int value of the {@link PeerCatchupStatus}
     *
     * @return
     */
    public int getValue() {
        return this.value;
    }
}
