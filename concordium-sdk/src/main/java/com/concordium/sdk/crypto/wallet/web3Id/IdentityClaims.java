package com.concordium.sdk.crypto.wallet.web3Id;

import com.concordium.sdk.crypto.bls.BLSSecretKey;
import com.concordium.sdk.crypto.wallet.Network;
import com.concordium.sdk.crypto.wallet.identityobject.AttributeList;
import com.concordium.sdk.crypto.wallet.identityobject.IdentityObject;
import com.concordium.sdk.crypto.wallet.web3Id.Statement.AtomicStatement;
import com.concordium.sdk.crypto.wallet.web3Id.Statement.AttributeValueStatement;
import com.concordium.sdk.crypto.wallet.web3Id.Statement.RevealStatement;
import com.concordium.sdk.crypto.wallet.web3Id.Statement.did.AccountRequestIdentifier;
import com.concordium.sdk.crypto.wallet.web3Id.Statement.did.IdentityProviderRequestIdentifier;
import com.concordium.sdk.requests.BlockQuery;
import com.concordium.sdk.responses.accountinfo.credential.AttributeType;
import com.concordium.sdk.responses.blocksummary.updates.queues.AnonymityRevokerInfo;
import com.concordium.sdk.responses.blocksummary.updates.queues.IdentityProviderInfo;
import com.concordium.sdk.transactions.CredentialRegistrationId;
import com.concordium.sdk.types.UInt32;
import lombok.*;
import lombok.extern.jackson.Jacksonized;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Statements requesting proofs from identity credentials issued by identity providers.
 * <p>
 * Although it is called "identity claims", either identity credential, or account credential, or both
 * can be used to prove these claims, depending on {@link IdentityClaims#source}.
 */
@Getter
@Builder
@EqualsAndHashCode
@Jacksonized
public class IdentityClaims implements SubjectClaims {
    /**
     * Credential types accepted for this statement (identity credential, account credential, or both).
     *
     * @see IdentityClaims#IDENTITY_CREDENTIAL_SOURCE
     * @see IdentityClaims#ACCOUNT_CREDENTIAL_SOURCE
     */
    private final Set<String> source;

    /**
     * Atomic statements about identity attributes to prove.
     */
    @Singular
    private final List<AtomicStatement> statements;

    /**
     * Valid identity issuers for this statement, in form of identity provider DIDs.
     * For example, <code>did:ccd:testnet:idp:0</code> where <code>0</code> is the provider index (IP identity).
     *
     * @see IdentityProviderRequestIdentifier#fromString(String)
     */
    @Singular
    private final List<IdentityProviderRequestIdentifier> issuers;

    /**
     * @return whether an identity credential is an acceptable source for these claims.
     * @see IdentityClaims#canBeProvedBy(IdentityObject, UInt32)
     * Check if a particular identity can actually prove these claims
     */
    public boolean areIdentitiesAccepted() {
        return source.contains(IDENTITY_CREDENTIAL_SOURCE);
    }

    /**
     * @return whether an account credential is an acceptable source for these claims.
     * @see IdentityClaims#canBeProvedBy(IdentityObject, UInt32)
     * Check if a particular account identity can actually prove these claims
     */
    public boolean areAccountsAccepted() {
        return source.contains(ACCOUNT_CREDENTIAL_SOURCE);
    }

    /**
     * @param idObject   identity itself or a particular account identity
     * @param ipIdentity index of the identity provider that issued this identity
     * @return whether the given identity itself or an account created from it
     * can actually prove these claims.
     * The identity must prove all the {@link IdentityClaims#getStatements() statements}
     * and it must be issued by one of the allowed {@link IdentityClaims#getIssuers() issuers}.
     */
    public boolean canBeProvedBy(IdentityObject idObject,
                                 UInt32 ipIdentity) {
        return issuers.stream().anyMatch(issuerId -> issuerId.getIpIdentity().equals(ipIdentity))
                && statements.stream().allMatch(statement -> statement.canBeProvedBy(idObject));
    }

    /**
     * @param attributeValues attribute values of an identity itself or a particular account identity
     * @param ipIdentity      index of the identity provider that issued this identity
     * @return whether the given identity itself or an account created from it
     * can actually prove these claims.
     * The identity must prove all the {@link IdentityClaims#getStatements() statements}
     * and it must be issued by one of the allowed {@link IdentityClaims#getIssuers() issuers}.
     */
    public boolean canBeProvedBy(Map<AttributeType, String> attributeValues,
                                 UInt32 ipIdentity) {
        val lightweightIdentityObject =
                IdentityObject.builder()
                        .attributeList(
                                AttributeList.builder()
                                        .chosenAttributes(attributeValues)
                                        .build()
                        )
                        .build();
        return canBeProvedBy(lightweightIdentityObject, ipIdentity);
    }

    /**
     * Prove these claims with an identity credential.
     *
     * @param network            network on which the identity is issued
     * @param ipInfo             identity provider that issued the identity, stored in the wallet or fetched from a node
     * @param arsInfos           anonymity revokers of the identity provider, stored in the wallet or fetched from a node
     * @param idObject           identity itself, stored in the wallet after creation or recovery
     * @param idCredSec          cred sec for the identity, derived with the HD wallet
     * @param prfKey             PRF key for the identity, derived with the HD wallet
     * @param blindingRandomness signature blinding randomness for the identity, derived with the HD wallet
     * @return input needed to create a verifiable presentation
     * @see IdentityClaims#areIdentitiesAccepted() Check if identity credentials are even accepted
     * @see IdentityClaims#canBeProvedBy(IdentityObject, UInt32) Check if a particular identity can prove the claims
     * @see com.concordium.sdk.ClientV2#getIdentityProviders(BlockQuery) Fetch identity providers
     * @see com.concordium.sdk.ClientV2#getAnonymityRevokers(BlockQuery) Fetch anonymity revokers
     * @see com.concordium.sdk.crypto.wallet.ConcordiumHdWallet#getIdCredSec(int, int) Derive ID cred sec
     * @see com.concordium.sdk.crypto.wallet.ConcordiumHdWallet#getPrfKey(int, int) Derive PRF key
     * @see com.concordium.sdk.crypto.wallet.ConcordiumHdWallet#getSignatureBlindingRandomness(int, int) Derive signature blinding randomness
     */
    public IdentityClaimsIdentityProofInput getIdentityProofInput(Network network,
                                                                  IdentityProviderInfo ipInfo,
                                                                  Map<String, AnonymityRevokerInfo> arsInfos,
                                                                  IdentityObject idObject,
                                                                  BLSSecretKey idCredSec,
                                                                  BLSSecretKey prfKey,
                                                                  String blindingRandomness) {
        if (!areIdentitiesAccepted()) {
            throw new IllegalStateException("Identities can't prove these claims, allowed sources are: " +
                    String.join(",", source));
        }

        if (!canBeProvedBy(idObject, ipInfo.getIpIdentity())) {
            throw new IllegalArgumentException("This identity can't prove these claims");
        }

        return IdentityClaimsIdentityProofInput.builder()
                .id(new IdentityProviderRequestIdentifier(network, ipInfo.getIpIdentity()))
                .statement(
                        statements
                                .stream()
                                .map(statement ->
                                        (statement instanceof RevealStatement)
                                                ? new AttributeValueStatement((RevealStatement) statement, idObject)
                                                : statement
                                )
                                .collect(Collectors.toList())
                )
                .commitmentInput(new IdentityCommitmentInput(ipInfo, arsInfos, idObject,
                        idCredSec, prfKey, blindingRandomness))
                .build();
    }

    /**
     * Prove these claims with an account credential.
     *
     * @param network             network on which the account is created
     * @param credId              account credential registration ID, stored in the wallet
     * @param ipIdentity          index of the identity provider that issued this account's identity,
     *                            stored in the wallet or fetched from a node
     * @param attributeValues     attribute values of this account's identity, stored in the wallet
     * @param attributeRandomness attribute randomness of the account, stored in the wallet
     * @return input needed to create a verifiable presentation
     * @see IdentityClaims#areAccountsAccepted() Check if account credentials are even accepted
     * @see IdentityClaims#canBeProvedBy(IdentityObject, UInt32) Check if an account's identity can prove the claims
     * @see com.concordium.sdk.ClientV2#getIdentityProviders(BlockQuery) Fetch identity providers
     */
    public IdentityClaimsAccountProofInput getAccountProofInput(Network network,
                                                                UInt32 ipIdentity,
                                                                CredentialRegistrationId credId,
                                                                Map<AttributeType, String> attributeValues,
                                                                Map<AttributeType, String> attributeRandomness) {
        if (!areAccountsAccepted()) {
            throw new IllegalStateException("Accounts can't prove these claims, allowed sources are: " +
                    String.join(",", source));
        }

        if (!canBeProvedBy(attributeValues, ipIdentity)) {
            throw new IllegalArgumentException("This account's identity can't prove these claims");
        }

        return IdentityClaimsAccountProofInput.builder()
                .id(new AccountRequestIdentifier(network, credId))
                .issuer(new IdentityProviderRequestIdentifier(network, ipIdentity))
                .statement(
                        statements
                                .stream()
                                .map(statement ->
                                        (statement instanceof RevealStatement)
                                                ? new AttributeValueStatement((RevealStatement) statement, attributeValues)
                                                : statement
                                )
                                .collect(Collectors.toList())
                )
                .commitmentInput(new AccountCommitmentInput(ipIdentity, attributeValues, attributeRandomness))
                .build();
    }

    public static final String TYPE = "identity";
    public static final String IDENTITY_CREDENTIAL_SOURCE = "identityCredential";
    public static final String ACCOUNT_CREDENTIAL_SOURCE = "accountCredential";
}
