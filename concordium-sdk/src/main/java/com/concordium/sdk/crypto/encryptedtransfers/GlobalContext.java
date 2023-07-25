package com.concordium.sdk.crypto.encryptedtransfers;

import com.concordium.sdk.crypto.bulletproof.BulletproofGenerators;
import com.concordium.sdk.crypto.pedersencommitment.PedersenCommitmentKey;
import com.concordium.sdk.responses.cryptographicparameters.CryptographicParameters;
import lombok.Builder;
import lombok.Data;

/**
 * A Set of Cryptographic parameters that are particular to the chain and
 * shared by everybody who interacts with the chain.
 */
@Data
@Builder
class GlobalContext {
    /**
     * A shared commitment key known to the chain and the account holder (and therefore it is public).
     * The account holder uses this commitment key to generate commitments to values in the attribute list.
     */
    private final PedersenCommitmentKey onChainCommitmentKey;

    /**
     * Generators for the bulletproofs. These G & H needed for range proofs of encrypted transfers.
     */
    private final BulletproofGenerators bulletproofGenerators;

    /**
     * A free-form string used to distinguish between different chains even if they share other parameters.
     */
    private final String genesisString;

    /**
     * Converts {@link CryptographicParameters} to {@link GlobalContext}
     *
     * @param cryptographicParameters input {@link CryptographicParameters}
     * @return Instance of {@link GlobalContext}
     */
    public static GlobalContext from(CryptographicParameters cryptographicParameters) {
        return new GlobalContext(
                cryptographicParameters.getOnChainCommitmentKey(),
                cryptographicParameters.getBulletproofGenerators(),
                cryptographicParameters.getGenesisString()
        );
    }
}
