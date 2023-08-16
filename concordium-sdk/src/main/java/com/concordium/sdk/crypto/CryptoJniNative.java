package com.concordium.sdk.crypto;

import com.concordium.sdk.transactions.SerializeParameterResult;

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
     * Serializes json representation of a parameter for a receive function using a schema of the module.
     * @param parameterJson json representation of the parameter.
     * @param contractName name of the contract.
     * @param methodName name of the method.
     * @param schemaBytes schema of the contract.
     * @param schemaVersion version of the schema.
     * @param verboseErrors whether errors are returned in verbose format or not.
     * @return JSON representing {@link SerializeParameterResult}
     */
    public static native String serializeReceiveParameter(String parameterJson, String contractName, String methodName, byte[] schemaBytes, int schemaVersion, boolean verboseErrors);

    /**
     * Serializes json representation of a parameter for an init function using a schema of the module.
     * @param parameterJson json representation of the parameter.
     * @param contractName name of the contract.
     * @param schemaBytes schema of the contract.
     * @param schemaVersion version of the schema.
     * @param verboseErrors whether errors are returned in verbose format or not.
     * @return JSON representing {@link SerializeParameterResult}
     */
    public static native String serializeInitParameter(String parameterJson, String contractName, byte[] schemaBytes, int schemaVersion, boolean verboseErrors);

}
