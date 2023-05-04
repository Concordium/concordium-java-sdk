package com.concordium.sdk.responses.transactionevent.updatepayloads;

/**
 * TODO explain better
 * Tagging interface for different kinds of Update Payloads. Payloads must be casted to a concrete type before being used.
 */
public interface UpdatePayload {

    /**
     * Gets the {@link UpdateType} of the payload.
     * @return {@link UpdateType} of the payload
     */
    UpdateType getType();

    /**
     * TODO
     * Parses {@link com.concordium.grpc.v2.UpdatePayload} to {@link UpdatePayload}
     * @param payload {@link com.concordium.grpc.v2.UpdatePayload} returned by the GRPC V2 API
     * @return parsed {@link UpdatePayload}
     */
    static UpdatePayload parse(com.concordium.grpc.v2.UpdatePayload payload) {
        return null;
    }
}
