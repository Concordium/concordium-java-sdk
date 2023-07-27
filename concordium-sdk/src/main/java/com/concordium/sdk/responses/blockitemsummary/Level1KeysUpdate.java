package com.concordium.sdk.responses.blockitemsummary;

import com.concordium.grpc.v2.Level1Update;
import com.concordium.sdk.responses.chainparameters.Authorizations;
import com.concordium.sdk.responses.chainparameters.AuthorizationsV0;
import com.concordium.sdk.responses.chainparameters.AuthorizationsV1;
import com.concordium.sdk.responses.chainparameters.HigherLevelKeys;
import lombok.*;

/**
 * The root keys update.
 * This can alter level 1 keys and level 2 keys.
 */
@ToString
@EqualsAndHashCode
@Getter
@Builder
public class Level1KeysUpdate {
    private final HigherLevelKeys level1Keys;
    private final Authorizations level2Keys;

    public static Level1KeysUpdate from(Level1Update level1Update) {
        val builder = Level1KeysUpdate.builder();
        builder.level1Keys(HigherLevelKeys.from(level1Update.getLevel1KeysUpdate()));
        if (level1Update.hasLevel2KeysUpdateV0()) {
            builder.level2Keys(AuthorizationsV0.from(level1Update.getLevel2KeysUpdateV0()));
        }else {
            builder.level2Keys(AuthorizationsV1.from(level1Update.getLevel2KeysUpdateV1()));
        }
        return builder.build();
    }
}
