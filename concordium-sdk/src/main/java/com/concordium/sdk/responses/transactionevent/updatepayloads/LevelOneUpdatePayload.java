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
public class LevelOneUpdatePayload implements UpdatePayload {


    /**
     * TODO
     * Parses {@link Level1Update} to {@link LevelOneUpdatePayload}
     * @param level1Update {@link Level1Update} returned by the GRPC V2 API
     * @return parsed {@link LevelOneUpdatePayload}
     */
    public static LevelOneUpdatePayload parse(Level1Update level1Update) {
        return null;
    }

    @Override
    public UpdateType getType() {
        return UpdateType.LEVEL_1_UPDATE;
    }
}
