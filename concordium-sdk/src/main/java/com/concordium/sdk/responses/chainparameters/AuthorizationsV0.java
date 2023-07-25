package com.concordium.sdk.responses.chainparameters;

import com.concordium.grpc.v2.UpdatePublicKey;
import com.concordium.sdk.crypto.ed25519.ED25519PublicKey;
import com.google.protobuf.ByteString;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@EqualsAndHashCode
@ToString
@SuperBuilder
public class AuthorizationsV0 {

    /**
     * Keys allowed to do chain parameters.
     */
    private final Set<ED25519PublicKey> keys;

    /**
     * Emergency keys.
     */
    private final AccessStructure emergency;

    /**
     * Keys that can sign protocol updates.
     */
    private final AccessStructure protocol;

    /**
     * Keys that can update the consensus parameters.
     */
    private final AccessStructure consensusParameters;

    /**
     * Keys that can update the euro per energy parameter.
     */
    private final AccessStructure euroPerEnergy;

    /**
     * Keys that can update the micro CCD per euro parameter.
     */
    private final AccessStructure microCCDPerEuro;

    /**
     * Keys that can update the foundation account parameter.
     */
    private final AccessStructure foundationAccount;

    /**
     * Keys that can update the mint distribution parameters.
     */
    private final AccessStructure mintDistribution;


    /**
     * Keys that can update the transaction fee distribution parameters.
     */
    private final AccessStructure transactionFeeDistribution;

    /**
     * Keys that can update the gas rewards parameters.
     */
    private final AccessStructure gasRewards;

    /**
     * Keys that can update the pool parameters.
     */
    private final AccessStructure poolParameters;

    /**
     * Keys that can add identity providers.
     */
    private final AccessStructure addIdentityProvider;

    /**
     * Keys that can add anonymity revokers.
     */
    private final AccessStructure addAnonymityRevoker;

    public static AuthorizationsV0 from(com.concordium.grpc.v2.AuthorizationsV0 value) {
        val keys = value.getKeysList()
                .stream()
                .map(UpdatePublicKey::getValue)
                .map(ByteString::toByteArray)
                .map(ED25519PublicKey::from)
                .collect(Collectors.toSet());
        return AuthorizationsV0.builder()
                .addAnonymityRevoker(com.concordium.sdk.responses.chainparameters.AccessStructure.from(value.getAddAnonymityRevoker()))
                .addIdentityProvider(com.concordium.sdk.responses.chainparameters.AccessStructure.from(value.getAddIdentityProvider()))
                .emergency(com.concordium.sdk.responses.chainparameters.AccessStructure.from(value.getEmergency()))
                .consensusParameters(com.concordium.sdk.responses.chainparameters.AccessStructure.from(value.getParameterConsensus()))
                .mintDistribution(com.concordium.sdk.responses.chainparameters.AccessStructure.from(value.getParameterMintDistribution()))
                .euroPerEnergy(com.concordium.sdk.responses.chainparameters.AccessStructure.from(value.getParameterEuroPerEnergy()))
                .poolParameters(com.concordium.sdk.responses.chainparameters.AccessStructure.from(value.getPoolParameters()))
                .protocol(com.concordium.sdk.responses.chainparameters.AccessStructure.from(value.getProtocol()))
                .gasRewards(com.concordium.sdk.responses.chainparameters.AccessStructure.from(value.getParameterGasRewards()))
                .foundationAccount(com.concordium.sdk.responses.chainparameters.AccessStructure.from(value.getParameterFoundationAccount()))
                .microCCDPerEuro(com.concordium.sdk.responses.chainparameters.AccessStructure.from(value.getParameterMicroCCDPerEuro()))
                .transactionFeeDistribution(com.concordium.sdk.responses.chainparameters.AccessStructure.from(value.getParameterTransactionFeeDistribution()))
                .keys(keys)
                .build();
    }
}
