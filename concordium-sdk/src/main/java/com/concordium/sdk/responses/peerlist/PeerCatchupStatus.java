package com.concordium.sdk.responses.peerlist;

import concordium.ConcordiumP2PRpc;

/**
 * Represents Catchup Status of a Node.
 */
public enum PeerCatchupStatus {

    /**
     * Node has the latest updates.
     */
    UPTODATE(0),

    /**
     * Node updates are pending.
     */
    PENDING(1),

    /**
     * Node is downloading new updates.
     */
    CATCHINGUP(2);

    private final int value;

    PeerCatchupStatus(int value) {
        this.value = value;
    }

    /**
     * Parses the input {@link ConcordiumP2PRpc.PeerElement.CatchupStatus}.
     * @param catchupStatus input {@link ConcordiumP2PRpc.PeerElement.CatchupStatus}.
     * @return Parsed {@link PeerCatchupStatus}.
     */
    public static PeerCatchupStatus parse(ConcordiumP2PRpc.PeerElement.CatchupStatus catchupStatus) {
        switch (catchupStatus) {
            case UPTODATE: return PeerCatchupStatus.UPTODATE;
            case PENDING: return PeerCatchupStatus.PENDING;
            case CATCHINGUP: return PeerCatchupStatus.CATCHINGUP;
            default:
            throw new IllegalArgumentException(String.format(
                    "Invalid catchup status received : %d",
                    catchupStatus.getNumber()));
        }
    }

    /**
     * Gets the int value of the {@link PeerCatchupStatus}
     * @return
     */
    public int getValue() {
        return this.value;
    }
}
