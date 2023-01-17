package com.concordium.sdk.responses.cryptographicparameters;

import com.concordium.sdk.crypto.bulletproof.BulletproofGenerators;
import com.concordium.sdk.crypto.pedersencommitment.PedersenCommitmentKey;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

//exists for json parsing only
@Data
@Builder
@Jacksonized
final class Value {
    private final BulletproofGenerators bulletproofGenerators;
    private final PedersenCommitmentKey onChainCommitmentKey;
    private final String genesisString;
}
