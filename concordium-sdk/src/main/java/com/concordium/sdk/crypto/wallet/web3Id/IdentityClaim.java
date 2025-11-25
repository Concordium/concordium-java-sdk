package com.concordium.sdk.crypto.wallet.web3Id;

import com.concordium.sdk.crypto.bls.BLSSecretKey;
import com.concordium.sdk.crypto.wallet.Network;
import com.concordium.sdk.crypto.wallet.identityobject.IdentityObject;
import com.concordium.sdk.crypto.wallet.web3Id.Statement.AtomicStatement;
import com.concordium.sdk.crypto.wallet.web3Id.Statement.did.AccountRequestIdentifier;
import com.concordium.sdk.crypto.wallet.web3Id.Statement.did.IdentityProviderRequestIdentifier;
import com.concordium.sdk.requests.BlockQuery;
import com.concordium.sdk.responses.accountinfo.credential.AttributeType;
import com.concordium.sdk.responses.blocksummary.updates.queues.AnonymityRevokerInfo;
import com.concordium.sdk.responses.blocksummary.updates.queues.IdentityProviderInfo;
import com.concordium.sdk.transactions.CredentialRegistrationId;
import com.concordium.sdk.types.UInt32;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Singular;
import lombok.extern.jackson.Jacksonized;

import java.util.List;
import java.util.Map;

/**
 * Statement requesting proofs from identity credentials issued by identity providers.
 * Can specify whether to accept proofs from identity credentials, account credentials, or both.
 */
@Getter
@Builder
@EqualsAndHashCode
@Jacksonized
public class IdentityClaim implements SubjectClaim {

    /**
     * Credential types accepted for this statement (identity credential, account credential, or both).
     *
     * @see IdentityClaim#IDENTITY_CREDENTIAL_SOURCE
     * @see IdentityClaim#ACCOUNT_CREDENTIAL_SOURCE
     */
    private final List<String> source;

    /**
     * Atomic statements about identity attributes to prove.
     */
    @Singular
    private final List<AtomicStatement> statements;

    /**
     * Valid identity issuers for this statement, in form of identity provider DIDs.
     * For example, <code>did:ccd:testnet:idp:0</code>
     *
     * @see IdentityProviderRequestIdentifier#fromString(String)
     */
    @Singular
    private final List<IdentityProviderRequestIdentifier> issuers;

    /**
     * Qualifies for this claim with an identity credential.
     *
     * @param network            network on which the identity is issued
     * @param ipInfo             identity provider that issued the identity, stored in the wallet or fetched from a node
     * @param arsInfos           anonymity revokers of the identity provider, stored in the wallet or fetched from a node
     * @param idObject           identity itself, stored in the wallet after creation or recovery
     * @param idCredSec          cred sec for the identity, derived with the HD wallet
     * @param prfKey             PRF key for the identity, derived with the HD wallet
     * @param blindingRandomness signature blinding randomness for the identity, derived with the HD wallet
     * @see com.concordium.sdk.ClientV2#getIdentityProviders(BlockQuery)
     * @see com.concordium.sdk.ClientV2#getAnonymityRevokers(BlockQuery)
     * @see com.concordium.sdk.crypto.wallet.ConcordiumHdWallet#getIdCredSec(int, int)
     * @see com.concordium.sdk.crypto.wallet.ConcordiumHdWallet#getPrfKey(int, int)
     * @see com.concordium.sdk.crypto.wallet.ConcordiumHdWallet#getSignatureBlindingRandomness(int, int)
     */
    public IdentityQualifyingIdentityClaim qualify(Network network,
                                                   IdentityProviderInfo ipInfo,
                                                   Map<String, AnonymityRevokerInfo> arsInfos,
                                                   IdentityObject idObject,
                                                   BLSSecretKey idCredSec,
                                                   BLSSecretKey prfKey,
                                                   String blindingRandomness) {
        if (!source.contains(IDENTITY_CREDENTIAL_SOURCE)) {
            throw new IllegalStateException("Identity can't qualify for this claim, allowed sources are: " +
                    String.join(",", source));
        }

        return IdentityQualifyingIdentityClaim.builder()
                .id(new IdentityProviderRequestIdentifier(network, ipInfo.getIpIdentity()))
                .statements(statements)
                .commitmentInput(new IdentityCommitmentInput(ipInfo, arsInfos, idObject,
                        idCredSec, prfKey, blindingRandomness))
                .build();
    }

    /**
     * Qualifies for this claim with an account credential.
     *
     * @param network             network on which the account is created
     * @param credId              account credential registration ID, stored in the wallet
     * @param ipIdentity          index of the identity provider that issued this account's identity,
     *                            stored in the wallet or fetched from a node
     * @param attributeValues     attribute values of this account's identity, stored in the wallet
     * @param attributeRandomness attribute randomness of the account, stored in the wallet
     * @see com.concordium.sdk.ClientV2#getIdentityProviders(BlockQuery)
     */
    public AccountQualifyingIdentityClaim qualify(Network network,
                                                  UInt32 ipIdentity,
                                                  CredentialRegistrationId credId,
                                                  Map<AttributeType, String> attributeValues,
                                                  Map<AttributeType, String> attributeRandomness) {
        if (!source.contains(ACCOUNT_CREDENTIAL_SOURCE)) {
            throw new IllegalStateException("Account can't qualify for this claim, allowed sources are: " +
                    String.join(",", source));
        }

        return AccountQualifyingIdentityClaim.builder()
                .id(new AccountRequestIdentifier(network, credId))
                .statements(statements)
                .commitmentInput(new AccountCommitmentInput(ipIdentity, attributeValues, attributeRandomness))
                .build();
    }

    public static final String TYPE = "identity";
    public static final String IDENTITY_CREDENTIAL_SOURCE = "identityCredential";
    public static final String ACCOUNT_CREDENTIAL_SOURCE = "accountCredential";
}
