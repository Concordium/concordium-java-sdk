package com.concordium.sdk.responses.blockitemsummary;

import com.concordium.grpc.v2.RootUpdate;
import com.concordium.sdk.responses.chainparameters.Authorizations;
import com.concordium.sdk.responses.chainparameters.AuthorizationsV0;
import com.concordium.sdk.responses.chainparameters.AuthorizationsV1;
import com.concordium.sdk.responses.chainparameters.HigherLevelKeys;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * The root keys update.
 * This can alter root keys, level 1 keys and level 2 keys.
 */
@Builder
@ToString
@Getter
@EqualsAndHashCode
public class RootKeysUpdate {
    private final HigherLevelKeys rootKeys;
    private final HigherLevelKeys level1Keys;
    private final Authorizations level2Keys;

    public static RootKeysUpdate from(RootUpdate rootUpdate) {
        RootKeysUpdateBuilder builder = RootKeysUpdate.builder();
        builder.rootKeys(HigherLevelKeys.from(rootUpdate.getRootKeysUpdate()));
        builder.level1Keys(HigherLevelKeys.from(rootUpdate.getLevel1KeysUpdate()));
        if (rootUpdate.hasLevel2KeysUpdateV0()) {
            builder.level2Keys(AuthorizationsV0.from(rootUpdate.getLevel2KeysUpdateV0()));
        }else {
            builder.level2Keys(AuthorizationsV1.from(rootUpdate.getLevel2KeysUpdateV1()));
        }
        return builder.build();
    }
}
