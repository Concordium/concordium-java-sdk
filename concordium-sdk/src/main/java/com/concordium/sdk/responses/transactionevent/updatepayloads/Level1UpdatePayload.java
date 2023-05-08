package com.concordium.sdk.responses.transactionevent.updatepayloads;

import com.concordium.grpc.v2.Level1Update;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@EqualsAndHashCode
@ToString
public class Level1UpdatePayload implements UpdatePayload {


    /**
     * TODO
     * Parses {@link Level1Update} to {@link Level1UpdatePayload}.
     * @param level1Update {@link Level1Update} returned by the GRPC V2 API.
     * @return parsed {@link Level1UpdatePayload}.
     */
    public static Level1UpdatePayload parse(Level1Update level1Update) {
        return null;
    }

    @Override
    public UpdateType getType() {
        return UpdateType.LEVEL_1_UPDATE;
    }
}
