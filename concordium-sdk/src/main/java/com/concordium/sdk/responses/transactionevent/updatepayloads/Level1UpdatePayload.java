package com.concordium.sdk.responses.transactionevent.updatepayloads;

import com.concordium.grpc.v2.Level1Update;
import com.concordium.sdk.responses.blocksummary.updates.keys.Level1KeysUpdates;
import com.concordium.sdk.responses.blocksummary.updates.keys.Level2KeysUpdates;
import lombok.*;

@Builder
@EqualsAndHashCode
@ToString
public class Level1UpdatePayload implements UpdatePayload {


    /**
     * The level 1 keys were updated.
     * Note that this is null if {@link KeysUpdatedType} is not {@link KeysUpdatedType#LEVEL_1_KEYS_UPDATE}.
     */
    private Level1KeysUpdates level1KeysUpdate;

    /**
     * The level 2 keys were updated.
     * Note that this is null if {@link KeysUpdatedType} is not {@link KeysUpdatedType#LEVEL_2_KEYS_UPDATE}.
     */
    private Level2KeysUpdates level2KeysUpdate;

    @Getter
    private KeysUpdatedType keysUpdatedType;

    /**
     * Parses {@link Level1Update} to {@link Level1UpdatePayload}.
     * @param level1Update {@link Level1Update} returned by the GRPC V2 API.
     * @return parsed {@link Level1UpdatePayload}.
     */
    public static Level1UpdatePayload parse(Level1Update level1Update) {
        val res = Level1UpdatePayload.builder();
        switch (level1Update.getUpdateTypeCase()) {
            case LEVEL_1_KEYS_UPDATE:
                res.keysUpdatedType(KeysUpdatedType.LEVEL_1_KEYS_UPDATE);
                res.level1KeysUpdate(Level1KeysUpdates.parse(level1Update.getLevel1KeysUpdate()));
            case LEVEL_2_KEYS_UPDATE_V0:
                res.keysUpdatedType(KeysUpdatedType.LEVEL_2_KEYS_UPDATE);
                res.level2KeysUpdate(Level2KeysUpdates.parse(level1Update.getLevel2KeysUpdateV0()));
            case LEVEL_2_KEYS_UPDATE_V1:
                res.keysUpdatedType(KeysUpdatedType.LEVEL_2_KEYS_UPDATE);
                res.level2KeysUpdate(Level2KeysUpdates.parse(level1Update.getLevel2KeysUpdateV1()));
            case UPDATETYPE_NOT_SET:
                throw new IllegalArgumentException("Cannot parse " + Level1Update.UpdateTypeCase.UPDATETYPE_NOT_SET + " as Level1UpdatePayload");
        }
        return res.build();
    }

    @Override
    public UpdateType getType() {
        return UpdateType.LEVEL_1_UPDATE;
    }

    public Level1KeysUpdates getLevel1KeysUpdate() {
        if (this.keysUpdatedType != KeysUpdatedType.LEVEL_1_KEYS_UPDATE) {
            throw new IllegalStateException("Level 1 keys not present for KeysUpdatedType: " + this.keysUpdatedType);
        }
        return this.level1KeysUpdate;
    }

    public Level2KeysUpdates getLevel2KeysUpdate() {
        if (this.keysUpdatedType != KeysUpdatedType.LEVEL_2_KEYS_UPDATE) {
            throw new IllegalStateException("Level 2 keys not present for KeysUpdatedType: " + this.keysUpdatedType);
        }
        return this.level2KeysUpdate;
    }
}
