package com.concordium.sdk.crypto;

import com.concordium.sdk.exceptions.JNIError;
import com.concordium.sdk.transactions.InitName;
import com.concordium.sdk.transactions.ReceiveName;
import com.concordium.sdk.transactions.smartcontracts.SchemaParameter;
import com.concordium.sdk.transactions.smartcontracts.SchemaVersion;
import com.concordium.sdk.transactions.smartcontracts.SerializeParameterResult;
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

    public static native String getAccountSigningKey(String seedAsHex, String netAsStr, long identityProviderIndex, long identityIndex, long credentialCounter);
    public static native String getAccountPublicKey(String seedAsHex, String netAsStr, long identityProviderIndex, long identityIndex, long credentialCounter);
    public static native String getIdCredSec(String seedAsHex, String netAsStr, long identityProviderIndex, long identityIndex);
    public static native String getPrfKey(String seedAsHex, String netAsStr, long identityProviderIndex, long identityIndex);
    public static native String getCredentialId(String seedAsHex, String netAsStr, long identityProviderIndex, long identityIndex, long credentialCounter, String commitmentKey);
    public static native String getSignatureBlindingRandomness(String seedAsHex, String netAsStr, long identityProviderIndex, long identityIndex);
    public static native String getAttributeCommitmentRandomness(String seedAsHex, String netAsStr, long identityProviderIndex, long identityIndex, long credentialCounter, int attribute);
    public static native String getVerifiableCredentialSigningKey(String seedAsHex, String netAsStr, long issuerIndex, long issuerSubindex, long verifiableCredentialIndex);
    public static native String getVerifiableCredentialPublicKey(String seedAsHex, String netAsStr, long issuerIndex, long issuerSubindex, long verifiableCredentialIndex);
    public static native String getVerifiableCredentialBackupEncryptionKey(String seedAsHex, String netAsStr);
    
    public static native String createIdRequestWithKeysV1(String input);
}
