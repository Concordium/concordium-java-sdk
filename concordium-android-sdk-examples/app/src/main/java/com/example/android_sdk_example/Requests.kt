package com.example.android_sdk_example

import com.concordium.sdk.crypto.wallet.ConcordiumHdWallet
import com.concordium.sdk.crypto.wallet.Credential
import com.concordium.sdk.crypto.wallet.Identity
import com.concordium.sdk.crypto.wallet.Identity.createIdentityRecoveryRequest
import com.concordium.sdk.crypto.wallet.IdentityRecoveryRequestInput
import com.concordium.sdk.crypto.wallet.IdentityRequestInput
import com.concordium.sdk.crypto.wallet.UnsignedCredentialInput
import com.concordium.sdk.crypto.wallet.credential.UnsignedCredentialDeploymentInfoWithRandomness
import com.concordium.sdk.crypto.wallet.identityobject.IdentityObject
import com.concordium.sdk.requests.BlockQuery
import com.concordium.sdk.responses.accountinfo.credential.AttributeType
import com.concordium.sdk.responses.cryptographicparameters.CryptographicParameters
import com.concordium.sdk.transactions.CredentialPublicKeys
import com.concordium.sdk.transactions.Index
import com.example.android_sdk_example.services.ConcordiumClientService
import com.example.android_sdk_example.services.wallet_proxy.IdentityProvider
import java.util.Collections
import java.util.EnumMap

object Requests {

    /**
     * Creates the serialized input for requesting an identity recovery.
     * @param wallet the wallet for the seed phrase that the identity should be created with
     * @param provider the chosen identity provider
     * @param global the global cryptographic parameters of the current chain
     * @return returns the recovery request as a JSON string
     */
    fun createRecoveryRequest(
        wallet: ConcordiumHdWallet,
        provider: IdentityProvider,
        global: CryptographicParameters
    ): String {
        val providerIndex = provider.ipInfo.ipIdentity
        val idCredSec = wallet.getIdCredSec(providerIndex.value, Constants.IDENTITY_INDEX)

        val input = IdentityRecoveryRequestInput.builder()
            .globalContext(global)
            .ipInfo(provider.ipInfo)
            .idCredSec(idCredSec)
            .timestamp(java.time.Instant.now().epochSecond)
            .build()

        return createIdentityRecoveryRequest(input)
    }

    /**
     * Creates credential deployment info for creating a new account.
     * @param wallet the wallet for the seed phrase that the account should be created with
     * @param identity the identity object that the credential will be constructed from
     * @param ipIdentity the index of the identity's provider
     * @return returns the unsigned info and the randomness used
     */
    fun createCredentialRequest(
        wallet: ConcordiumHdWallet,
        identity: IdentityObject,
        ipIdentity: Int,
    ): UnsignedCredentialDeploymentInfoWithRandomness {
        val client = ConcordiumClientService.getClient()
        val anonymityRevokers =
            Iterable { client.getAnonymityRevokers(BlockQuery.BEST) }.associateBy { it.arIdentity.toString() }
        val providers = client.getIdentityProviders(BlockQuery.BEST)
        val provider = Iterable { providers }.find { it.ipIdentity.value == ipIdentity }
        val global = client.getCryptographicParameters(BlockQuery.BEST)

        val attributeRandomness: MutableMap<AttributeType, String> =
            EnumMap(AttributeType::class.java)
        for (attrType in AttributeType.entries) {
            attributeRandomness[attrType] =
                wallet.getAttributeCommitmentRandomness(
                    ipIdentity,
                    Constants.IDENTITY_INDEX,
                    Constants.CREDENTIAL_COUNTER,
                    attrType.ordinal
                )
        }

        val input: UnsignedCredentialInput = UnsignedCredentialInput.builder()
            .ipInfo(provider!!)
            .globalContext(global)
            .arsInfos(anonymityRevokers)
            .idObject(identity)
            .credNumber(Constants.CREDENTIAL_COUNTER)
            .attributeRandomness(attributeRandomness)
            .blindingRandomness(
                wallet.getSignatureBlindingRandomness(
                    ipIdentity,
                    Constants.IDENTITY_INDEX
                )
            )
            .credentialPublicKeys(
                CredentialPublicKeys.from(
                    Collections.singletonMap(
                        Index.from(0),
                        wallet.getAccountPublicKey(
                            ipIdentity,
                            Constants.IDENTITY_INDEX,
                            Constants.CREDENTIAL_COUNTER
                        )
                    ), 1
                )
            )
            .idCredSec(wallet.getIdCredSec(ipIdentity, Constants.IDENTITY_INDEX))
            .prfKey(wallet.getPrfKey(ipIdentity, Constants.IDENTITY_INDEX))
            .revealedAttributes(emptyList())
            .build()

        return Credential.createUnsignedCredential(input)
    }

    /**
     * Creates the serialized input for requesting an identity issuance.
     * @param wallet the wallet for the seed phrase that the identity should be created with
     * @param provider the chosen identity provider
     * @param global the global cryptographic parameters of the current chain
     * @return returns the identity issuance request as a JSON string
     */
    fun createIssuanceRequest(
        wallet: ConcordiumHdWallet,
        provider: IdentityProvider,
        global: CryptographicParameters
    ): String {
        val providerIndex = provider.ipInfo.ipIdentity.value

        val idCredSec = wallet.getIdCredSec(providerIndex, Constants.IDENTITY_INDEX)
        val prfKey = wallet.getPrfKey(providerIndex, Constants.IDENTITY_INDEX)
        val blindingRandomness: String =
            wallet.getSignatureBlindingRandomness(providerIndex, Constants.IDENTITY_INDEX)

        val input: IdentityRequestInput = IdentityRequestInput.builder()
            .globalContext(global)
            .ipInfo(provider.ipInfo)
            .arsInfos(provider.arsInfos)
            .arThreshold(Constants.AR_THRESHOLD)
            .idCredSec(idCredSec)
            .prfKey(prfKey)
            .blindingRandomness(blindingRandomness)
            .build()

        return Identity.createIdentityRequest(input)
    }
}
