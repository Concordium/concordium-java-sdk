package com.concordium.sdk.responses.nodeinfov2;

import com.concordium.sdk.responses.nodeinfo.PeerType;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
public class NodeInfo  {

    private ConsensusStatus consensusStatus;
    @Getter
    private PeerType type;
    @Getter
    private NetworkInfo networkInfo;

    public ConsensusStatus getConsensusStatus() {
        switch (this.type) {
            case BOOTSTRAPPER: throw new IllegalStateException("Bootstrapper does not have a consensus status");
            case NODE: return this.consensusStatus;
        }
        throw new IllegalStateException("Unexpected PeerType: " + this.type);
    }

}
