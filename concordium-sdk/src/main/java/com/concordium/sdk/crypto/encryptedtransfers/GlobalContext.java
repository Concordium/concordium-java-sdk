package com.concordium.sdk.crypto.encryptedtransfers;

import com.concordium.sdk.crypto.bulletproof.BulletproofGenerators;
import com.concordium.sdk.crypto.pedersencommitment.PedersenCommitmentKey;
import com.concordium.sdk.responses.cryptographicparameters.CryptographicParameters;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
class GlobalContext {
    private final PedersenCommitmentKey onChainCommitmentKey;
    private final BulletproofGenerators bulletproofGenerators;
    private final String genesisString;

    public static GlobalContext from(CryptographicParameters cryptographicParameters) {
        return new GlobalContext(
                cryptographicParameters.getOnChainCommitmentKey(),
                cryptographicParameters.getBulletproofGenerators(),
                cryptographicParameters.getGenesisString()
        );
    }
}
