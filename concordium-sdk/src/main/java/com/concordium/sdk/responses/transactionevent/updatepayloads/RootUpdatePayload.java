package com.concordium.sdk.responses.transactionevent.updatepayloads;

import com.concordium.grpc.v2.RootUpdate;
import com.concordium.sdk.responses.blocksummary.updates.keys.Level1KeysUpdates;
import com.concordium.sdk.responses.blocksummary.updates.keys.Level2KeysUpdates;
import com.concordium.sdk.responses.blocksummary.updates.keys.RootKeysUpdates;
import lombok.*;

/**
 * Root updates are the highest kind of key updates. They can update every other set of keys, even themselves.
 * They can only be performed by Root level keys.
 */
@Builder
@ToString
@EqualsAndHashCode
public class RootUpdatePayload implements UpdatePayload {

    /**
     * The root keys were updated.
     * Note that this is null if {@link KeysUpdatedType} is not {@link KeysUpdatedType#ROOT_KEYS_UPDATE}.
     */
    private RootKeysUpdates rootKeysUpdate;

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

    /**
     * The type of keys updated. Determines which field is populated.
     */
    @Getter
    private KeysUpdatedType keysUpdatedType;

    /**
     * Parses {@link RootUpdate} to {@link RootUpdatePayload}.
     * @param rootUpdate {@link RootUpdate} returned by the GRPC V2 API.
     * @return parsed {@link RootUpdate}.
     */
    public static RootUpdatePayload parse(RootUpdate rootUpdate) {
        val res = RootUpdatePayload.builder();
        switch (rootUpdate.getUpdateTypeCase()) {
            case ROOT_KEYS_UPDATE:
                res.keysUpdatedType(KeysUpdatedType.ROOT_KEYS_UPDATE);
                res.rootKeysUpdate(RootKeysUpdates.parse(rootUpdate.getRootKeysUpdate()));
            case LEVEL_1_KEYS_UPDATE:
                res.keysUpdatedType(KeysUpdatedType.LEVEL_1_KEYS_UPDATE);
                res.level1KeysUpdate(Level1KeysUpdates.parse(rootUpdate.getLevel1KeysUpdate()));
            case LEVEL_2_KEYS_UPDATE_V0:
                res.keysUpdatedType(KeysUpdatedType.LEVEL_2_KEYS_UPDATE);
                res.level2KeysUpdate(Level2KeysUpdates.parse(rootUpdate.getLevel2KeysUpdateV0()));
            case LEVEL_2_KEYS_UPDATE_V1:
                res.keysUpdatedType(KeysUpdatedType.LEVEL_2_KEYS_UPDATE);
                res.level2KeysUpdate(Level2KeysUpdates.parse(rootUpdate.getLevel2KeysUpdateV1()));
            case UPDATETYPE_NOT_SET:
                throw new IllegalArgumentException("Cannot parse " + RootUpdate.UpdateTypeCase.UPDATETYPE_NOT_SET + " as RootUpdatePayload");
        }
        return res.build();
    }

    @Override
    public UpdateType getType() {
        return UpdateType.ROOT_UPDATE;
    }

    public RootKeysUpdates getRootKeysUpdate() {
        if (this.keysUpdatedType != KeysUpdatedType.ROOT_KEYS_UPDATE) {
            throw new IllegalStateException("Root keys not present for KeysUpdatedType: " + this.keysUpdatedType);
        }
        return this.rootKeysUpdate;
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
