package com.concordium.sdk.responses.peerList;

import concordium.ConcordiumP2PRpc;

public enum PeerCatchupStatus {
    UPTODATE(0),
    PENDING(1),
    CATCHINGUP(2),
    UNRECOGNIZED(-1);

    private final int value;

    PeerCatchupStatus(int value) {
        this.value = value;
    }

    public static PeerCatchupStatus parse(ConcordiumP2PRpc.PeerElement.CatchupStatus catchupStatus) {
        switch (catchupStatus) {
            case  UPTODATE: return PeerCatchupStatus.UPTODATE;
            case PENDING: return PeerCatchupStatus.PENDING;
            case CATCHINGUP: return PeerCatchupStatus.CATCHINGUP;
            default:
            case UNRECOGNIZED: return PeerCatchupStatus.UNRECOGNIZED;
        }
    }

    public int getValue() {
        return this.value;
    }
}
