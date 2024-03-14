package com.concordium.sdk.crypto;

import com.concordium.sdk.crypto.wallet.IdentityRecoveryRequestInput;
import com.concordium.sdk.crypto.wallet.IdentityRequestInput;
import com.concordium.sdk.crypto.wallet.Network;
import com.concordium.sdk.crypto.wallet.StringResult;
import com.concordium.sdk.crypto.wallet.UnsignedCredentialInput;
import com.concordium.sdk.crypto.wallet.credential.CredentialDeploymentDetails;
import com.concordium.sdk.crypto.wallet.credential.CredentialDeploymentSerializationContext;
import com.concordium.sdk.crypto.wallet.web3Id.AcceptableRequest;
import com.concordium.sdk.crypto.wallet.web3Id.AttributeCheck;
import com.concordium.sdk.exceptions.JNIError;
import com.concordium.sdk.transactions.InitName;
import com.concordium.sdk.transactions.ReceiveName;
import com.concordium.sdk.transactions.smartcontracts.SchemaParameter;
import com.concordium.sdk.transactions.smartcontracts.SchemaVersion;
import com.concordium.sdk.transactions.smartcontracts.SerializeParameterResult;
import com.concordium.sdk.types.ContractAddress;

import lombok.SneakyThrows;

public class CryptoJniNative {

    /**
     * Signs a message using the provided private key.
     *
     * @param privateKey The private key to use for signing.
     * @param message    The message to sign.
     * @param out        The buffer to write the signature to.
     * @return 0 on success, non-zero on error.
     */
    public static native int sign(byte[] privateKey, byte[] message, byte[] out);

    /**
     * Verifies a signature for a message using the provided public key.
     *
     * @param publicKey The public key to use for verification.
     * @param message   The message to verify the signature for.
     * @param signature The signature to verify.
     * @return 0 if the signature is valid, non-zero on error or if the signature is invalid.
     */
    public static native int verify(byte[] publicKey, byte[] message, byte[] signature);

    /**
     * Generates a new secret key and stores it in the provided buffer.
     *
     * @param buffer The buffer to write the secret key to.
     * @return 0 on success, non-zero on error.
     */
    public static native int generateSecretKey(byte[] buffer);

    /**
     * Generates the public key corresponding to the given secret key, and stores it in the provided buffer.
     *
     * @param secretKey The secret key to generate the public key for.
     * @param buffer    The buffer to write the public key to.
     * @return 0 on success, non-zero on error.
     */
    public static native int generatePublicKey(byte[] secretKey, byte[] buffer);

    /**
     * Creates a transfer from the encrypted amount to a public account payload, using the provided input string.
     *
     * @param input The input string to use for creating the transfer.
     * @return The transfer data as a JSON string.
     */
    public static native String createSecToPubTransfer(String input);

    /**
     * Generates an encrypted transfer payload, using the provided input string.
     *
     * @param input The input string to use for generating the encrypted transfer.
     * @return The encrypted transfer data as a JSON string.
     */
    public static native String generateEncryptedTransfer(String input);

    /**
     * Generates baker keys.
     *
     * @return The baker keys data as a JSON string.
     */

    public static native String generateBakerKeys();

    /**
     * Generates a configure baker keys payload, using the provided input string.
     *
     * @param input The input string to use for generating the configure baker keys payload.
     * @return The configure baker keys data as a JSON string.
     */
    public static native String generateConfigureBakerKeysPayload(String input);

    /**
     * Serializes a parameter for a receive function using a schema of the module.
     * @param parameter the parameter.
     * @param receiveName the name of the contract and the method.
     * @param schemaBytes schema of the contract.
     * @param schemaVersion version of the schema.
     * @param verboseErrors whether errors are returned in verbose format or not, can be useful when debugging why serialization fails.
     * @return JSON representing {@link SerializeParameterResult}. If the serialization was successful, the field 'serializedParameter' contains the serialized parameter as hex encoded bytes.
     * If not successful, the 'err' field contains a {@link JNIError} detailing what went wrong.
     */
    @SneakyThrows
    public static String serializeReceiveParameter(SchemaParameter parameter, ReceiveName receiveName, byte[] schemaBytes, SchemaVersion schemaVersion, boolean verboseErrors) {
        return serializeReceiveParameter(parameter.toJson(), receiveName.getContractName(), receiveName.getMethod(), schemaBytes, schemaVersion.getVersion(), verboseErrors);
    }
    private static native String serializeReceiveParameter(String parameterJson, String contractName, String methodName, byte[] schemaBytes, int schemaVersion, boolean verboseErrors);

    /**
     * Serializes a parameter for an init function using a schema of the module.
     * @param parameter the parameter.
     * @param initName name of the contract.
     * @param schemaBytes schema of the contract.
     * @param schemaVersion version of the schema.
     * @param verboseErrors whether errors are returned in verbose format or not, can be useful when debugging why serialization fails.
     * @return JSON representing {@link SerializeParameterResult}. If the serialization was successful, the field 'serializedParameter' contains the serialized parameter as hex encoded bytes.
     * If not successful, the 'err' field contains a {@link JNIError} detailing what went wrong.
     */
    public static String serializeInitParameter(SchemaParameter parameter, InitName initName, byte[] schemaBytes, SchemaVersion schemaVersion, boolean verboseErrors) {
        return serializeInitParameter(parameter.toJson(), initName.getName(), schemaBytes, schemaVersion.getVersion(), verboseErrors);
    }
    private static native String serializeInitParameter(String parameterJson, String contractName, byte[] schemaBytes, int schemaVersion, boolean verboseErrors);

    /**
     * Derives an account signing key from the provided seed and network for the given indices.
     * @param seedAsHex the seed derived from a seed phrase as a hex string
     * @param network the network that the derived key is for
     * @param identityProviderIndex the index of the identity provider that the account is associated with interpreted as a u32.
     * @param identityIndex the index of the identity that the account is associated with interpreted as a u32.
     * @param credentialCounter the number of the credential interpreted as a u32.
     * @return JSON representing {@link StringResult}. If successful the field 'result' contains the hex encoded account signing key.
     * If not successful, the 'err' field contains a {@link JNIError} detailing what went wrong.
     */
    public static String getAccountSigningKey(String seedAsHex, Network network, int identityProviderIndex, int identityIndex, int credentialCounter) {
        return getAccountSigningKey(seedAsHex, network.getValue(), identityProviderIndex, identityIndex, credentialCounter);
    }
    private static native String getAccountSigningKey(String seedAsHex, String network, int identityProviderIndex, int identityIndex, int credentialCounter);

    /**
     * Derives an account public key from the provided seed and network for the given indices.
     * @param seedAsHex the seed derived from a seed phrase as a hex string
     * @param network the network that the derived key is for
     * @param identityProviderIndex the index of the identity provider that the account is associated with interpreted as a u32.
     * @param identityIndex the index of the identity that the account is associated with interpreted as a u32.
     * @param credentialCounter the number of the credential interpreted as a u32.
     * @return JSON representing {@link StringResult}. If successful the field 'result' contains the hex encoded account public key.
     * If not successful, the 'err' field contains a {@link JNIError} detailing what went wrong.
     */
    public static String getAccountPublicKey(String seedAsHex, Network network, int identityProviderIndex, int identityIndex, int credentialCounter) {
        return getAccountPublicKey(seedAsHex, network.getValue(), identityProviderIndex, identityIndex, credentialCounter);
    }
    private static native String getAccountPublicKey(String seedAsHex, String netAsStr, int identityProviderIndex, int identityIndex, int credentialCounter);

    /**
     * Derives id cred sec from the provided seed and network for the given indices.
     * @param seedAsHex the seed derived from a seed phrase as a hex string
     * @param network the network that the derived key is for
     * @param identityProviderIndex the index of the identity provider that the account is associated with interpreted as a u32.
     * @param identityIndex the index of the identity that the account is associated with interpreted as a u32.
     * @return JSON representing {@link StringResult}. If successful the field 'result' contains the hex id cred sec.
     * If not successful, the 'err' field contains a {@link JNIError} detailing what went wrong.
     */
    public static String getIdCredSec(String seedAsHex, Network network, int identityProviderIndex, int identityIndex) {
        return getIdCredSec(seedAsHex, network.getValue(), identityProviderIndex, identityIndex);
    }
    private static native String getIdCredSec(String seedAsHex, String netAsStr, int identityProviderIndex, int identityIndex);

    /**
     * Derives a PRF-key from the provided seed and network for the given indices.
     * @param seedAsHex the seed derived from a seed phrase as a hex string
     * @param network the network that the derived key is for
     * @param identityProviderIndex the index of the identity provider that the account is associated with interpreted as a u32.
     * @param identityIndex the index of the identity that the account is associated with interpreted as a u32.
     * @return JSON representing {@link StringResult}. If successful the field 'result' contains the hex encoded PRF-key.
     * If not successful, the 'err' field contains a {@link JNIError} detailing what went wrong.
     */
    public static String getPrfKey(String seedAsHex, Network network, int identityProviderIndex, int identityIndex) {
        return getPrfKey(seedAsHex, network.getValue(), identityProviderIndex, identityIndex);
    }
    private static native String getPrfKey(String seedAsHex, String netAsStr, int identityProviderIndex, int identityIndex);

    /**
     * Gets the credential ID for a specific credential defined by the identity provider index,
     * identity index and the credential counter.
     * @param seedAsHex the seed derived from a seed phrase as a hex string
     * @param network the network that the derived key is for
     * @param identityProviderIndex the index of the identity provider that the account is associated with interpreted as a u32.
     * @param identityIndex the index of the identity that the account is associated with interpreted as a u32.
     * @param credentialCounter the credential number of the credential to get the id for interpreted as a u8.
     * @param commitmentKey the on chain commitment key. This value can be retrieved from a node through its gRPC interface.
     * @return JSON representing {@link StringResult}. If successful the field 'result' contains the hex encoded credential id.
     * If not successful, the 'err' field contains a {@link JNIError} detailing what went wrong.
     */
    public static String getCredentialId(String seedAsHex, Network network, int identityProviderIndex, int identityIndex, int credentialCounter, String commitmentKey) {
        return getCredentialId(seedAsHex, network.getValue(), identityProviderIndex, identityIndex, credentialCounter, commitmentKey);
    }
    private static native String getCredentialId(String seedAsHex, String netAsStr, int identityProviderIndex, int identityIndex, int credentialCounter, String commitmentKey);
    
    /**
     * Derives the signature blinding randomness for a specific identity.
     * @param seedAsHex the seed derived from a seed phrase as a hex string
     * @param network the network that the derived randomness is for
     * @param identityProviderIndex the index of the identity provider interpreted as a u32.
     * @param identityIndex the index of the identity interpreted as a u32.
     * @return JSON representing {@link StringResult}. If successful the field 'result' contains the hex encoded signature blinding randomness.
     * If not successful, the 'err' field contains a {@link JNIError} detailing what went wrong.
     */
    public static String getSignatureBlindingRandomness(String seedAsHex, Network network, int identityProviderIndex, int identityIndex) {
        return getSignatureBlindingRandomness(seedAsHex, network.getValue(), identityProviderIndex, identityIndex);
    }
    private static native String getSignatureBlindingRandomness(String seedAsHex, String netAsStr, int identityProviderIndex, int identityIndex);

    /**
     * Derives the attribute commitment randomness for a specific attribute.
     * @param seedAsHex the seed derived from a seed phrase as a hex string
     * @param network the network that the derived randomness is for
     * @param identityProviderIndex the index of the identity provider interpreted as a u32.
     * @param identityIndex the index of the identity interpreted as a u32.
     * @param credentialCounter the credential number that the attribute is randomness is for interpreted as a u32.
     * @param attribute the attribute key index interpreted as a u8.
     * @return JSON representing {@link StringResult}. If successful the field 'result' contains the hex encoded attribute commitment randomness.
     * If not successful, the 'err' field contains a {@link JNIError} detailing what went wrong.
     */
    public static String getAttributeCommitmentRandomness(String seedAsHex, Network network, int identityProviderIndex, int identityIndex, int credentialCounter, int attribute) {
        return getAttributeCommitmentRandomness(seedAsHex, network.getValue(), identityProviderIndex, identityIndex, credentialCounter, attribute);
    }
    private static native String getAttributeCommitmentRandomness(String seedAsHex, String netAsStr, int identityProviderIndex, int identityIndex, int credentialCounter, int attribute);

    /**
     * Derives a verifiable credential signing key.
     * @param seedAsHex the seed derived from a seed phrase as a hex string
     * @param network the network that the derived randomness is for
     * @param issuer the verifiable credential issuer contract
     * @param verifiableCredentialIndex the index of the verifiable credential to derive a signing key for interpreted as a u32
     * @return JSON representing {@link StringResult}. If successful the field 'result' contains the hex encoded verifiable credential signing key.
     * If not successful, the 'err' field contains a {@link JNIError} detailing what went wrong.
     */
    public static String getVerifiableCredentialSigningKey(String seedAsHex, Network network, ContractAddress issuer, int verifiableCredentialIndex) {
        return getVerifiableCredentialSigningKey(seedAsHex, network.getValue(), issuer.getIndex(), issuer.getSubIndex(), verifiableCredentialIndex);
    }
    private static native String getVerifiableCredentialSigningKey(String seedAsHex, String netAsStr, long issuerIndex, long issuerSubindex, int verifiableCredentialIndex);

    /**
     * Derives a verifiable credential public key.
     * @param seedAsHex the seed derived from a seed phrase as a hex string
     * @param network the network that the derived randomness is for
     * @param issuer the verifiable credential issuer contract
     * @param verifiableCredentialIndex the index of the verifiable credential to derive a public key for interpreted as a u32
     * @return JSON representing {@link StringResult}. If successful the field 'result' contains the hex encoded verifiable credential public key.
     * If not successful, the 'err' field contains a {@link JNIError} detailing what went wrong.
     */
    public static String getVerifiableCredentialPublicKey(String seedAsHex, Network network, ContractAddress issuer, int verifiableCredentialIndex) {
        return getVerifiableCredentialPublicKey(seedAsHex, network.getValue(), issuer.getIndex(), issuer.getSubIndex(), verifiableCredentialIndex);
    }
    private static native String getVerifiableCredentialPublicKey(String seedAsHex, String netAsStr, long issuerIndex, long issuerSubindex, int verifiableCredentialIndex);

    /**
     * Derives the verifiable credential backup encryption key. This key should be used to encrypt
     * the backup file of verifiable credentials. The key is derived from the seed phrase so that
     * a user can access the backup file when only holding the seed phrase.
     * @return JSON representing {@link StringResult}. If successful the field 'result' contains the hex encoded verifiable credential backup encryption key.
     * If not successful, the 'err' field contains a {@link JNIError} detailing what went wrong.
     */
    public static String getVerifiableCredentialBackupEncryptionKey(String seedAsHex, Network network) {
        return getVerifiableCredentialBackupEncryptionKey(seedAsHex, network.getValue());
    }
    private static native String getVerifiableCredentialBackupEncryptionKey(String seedAsHex, String netAsStr);

    /**
     * Creates an identity request that is to be sent to an identity provider when
     * creating a new identity.
     * @param input {@link IdentityRequestInput} serialized as JSON
     * @return JSON representing {@link StringResult}. If successful the field 'result' contains the identity request as JSON.
     * If not successful, the 'err' field contains a {@link JNIError} detailing what went wrong.
     */
    public static native String createIdentityRequestV1(String input);

    /**
     * Creates an identity recovery request that is to be sent to an identity provider when
     * recovering a previously created identity.
     * @param input {@link IdentityRecoveryRequestInput} serialized as JSON
     * @return JSON representing {@link StringResult}. If successful the field 'result' contains the identity recovery request as JSON.
     * If not successful, the 'err' field contains a {@link JNIError} detailing what went wrong.
     */
    public static native String createIdentityRecoveryRequest(String input);

    /**
     * Creates an unsigned credential.
     * @param input {@link UnsignedCredentialInput} serialized as JSON.
     * @return JSON representing {@link StringResult}. If successful the field 'result' contains the unsigned credential as JSON.
     * If not successful, the 'err' field contains a {@link JNIError} detailing what went wrong.
     */
    public static native String createUnsignedCredentialV1(String input);

    /**
     * Computes the sign digest of a new credential deployment. This digest is the one that needs
     * to be signed to be able to submit the transaction.
     * @param input {@link CredentialDeploymentDetails} serialized as JSON.
     * @return JSON representing {@link StringResult}. If successful the field 'result' contains the sign digest of
     * {@link CredentialDeploymentDetails} as a hex encoded string.
     * If not successful, the 'err' field contains a {@link JNIError} detailing what went wrong.
     */
    public static native String computeCredentialDeploymentSignDigest(String input);
    
    /**
     * Serializes a credential deployment for submission, i.e. it includes the signatures.
     * @param input {@link CredentialDeploymentSerializationContext} serialized as JSON.
     * @return JSON representing {@link StringResult}. If successful the field 'result' contains the serialization of
     * {@link CredentialDeploymentSerializationContext} as a hex encoded string.
     * If not successful, the 'err' field contains a {@link JNIError} detailing what went wrong.
     */
    public static native String serializeCredentialDeploymentForSubmission(String input);

    /**
     * Creates a web3Id presentation for the given statement.
     * @param input {@link Web3IdProofInput} serialized as JSON.
     * @return JSON representing {@link StringResult}. If successful the field 'result' contains the
     * {@link Web3IdProof} as JSON.
     */
    public static native String createWeb3IdProof(String input);

    /**
     * Check that a request is acceptable
     *
     * @param input {@link Request} serialized as JSON.
     * @return JSON representing {@link VoidResult}. If successful the field
     *         'result' is empty,
     *         but if not acceptable the reason will be contained in the error.
     */
    public static native String isAcceptableRequest(String input);

    /**
     * Check that an atomic is acceptable, with the given restrictions
     *
     * @param input     {@link Request} serialized as JSON.
     * @param rangeTags the list of tags, which may be used for range statements,
     *                  serialized as JSON.
     * @param setTags   the list of tags, which may be used for membership
     *                  statements, serialized as JSON.
     * @param check     provides the function to check whether the statement value
     *                  is acceptable.
     * @return JSON representing {@link VoidResult}. If successful the field
     *         'result' is empty,
     *         but if not acceptable the reason will be contained in the error.
     */
    public static native String isAcceptableAtomicStatement(String input, String rangeTags, String setTags,
            AcceptableRequest.RawAttributeCheck check);
}
