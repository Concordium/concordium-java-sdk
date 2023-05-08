package com.concordium.sdk.responses.transactionevent.updatepayloads;

import com.concordium.grpc.v2.RootUpdate;

public class RootUpdatePayload implements UpdatePayload {



    /**
     * TODO
     * Parses {@link RootUpdate} to {@link RootUpdatePayload}
     * @param rootUpdate {@link RootUpdate} returned by the GRPC V2 API
     * @return parsed {@link RootUpdate}
     */
    public static RootUpdatePayload parse(RootUpdate rootUpdate) {
        return null;
    }

    @Override
    public UpdateType getType() {
        return UpdateType.ROOT_UPDATE;
    }
}
