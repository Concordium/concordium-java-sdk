package com.concordium.sdk.crypto.wallet;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.bitcoinj.crypto.MnemonicCode;
import org.bitcoinj.crypto.MnemonicException;

import com.concordium.sdk.crypto.CryptoJniNative;
import com.concordium.sdk.crypto.NativeResolver;
import com.concordium.sdk.crypto.bls.BLSSecretKey;
import com.concordium.sdk.crypto.ed25519.ED25519PublicKey;
import com.concordium.sdk.crypto.ed25519.ED25519SecretKey;
import com.concordium.sdk.exceptions.CryptoJniException;
import com.concordium.sdk.serializing.JsonMapper;
import com.concordium.sdk.types.ContractAddress;
import com.fasterxml.jackson.core.JsonProcessingException;

public class ConcordiumHdWallet {

    // Static block to load native library.
    static {
        NativeResolver.loadLib();
    }

    // The seed (64 bytes) as a hex string (128 characters). This is a secret
    // value and must be kept safe.
    private final String seedAsHex;

    // The type of network that the wallet will derive keys for. This affects
    // the key derivation paths used so that mainnet keys and testnet keys are
    // not mixed.
    private final Network network;

    private ConcordiumHdWallet(String seedAsHex, Network network) {
        this.seedAsHex = seedAsHex;
        this.network = network;
    }

    /**
     * Create a Concordium hierarchical deterministic wallet from a space separated
     * seed phrase. It is in general safer to use the method that takes a list of strings
     * as input as it is less error prone than using this method.
     * @param seedPhrase the seed phrase with words separated by a single space
     * @param network the network the wallet is for
     * @return a wallet that can derive required Concordium wallet keys
     * @throws IOException
     * @throws MnemonicException
     */
    public static ConcordiumHdWallet fromSeedPhrase(String seedPhrase, Network network)
            throws IOException, MnemonicException {
        List<String> split = Arrays.asList(seedPhrase.split(" "));
        return fromSeedPhrase(split, network);
    }

    /**
     * Create a Concordium hierarchical deterministic wallet from a seed phrase
     * provided as a list of strings.
     * @param seedPhrase the seed phrase with words separated by a single space
     * @param network the network the wallet is for
     * @return a wallet that can derive required Concordium wallet keys
     * @throws IOException
     * @throws MnemonicException
     */
    public static ConcordiumHdWallet fromSeedPhrase(List<String> seedPhrase, Network network) throws IOException, MnemonicException {
        // Validate whether the input seed phrase is valid or not.
        StringBuilder builder = new StringBuilder(WordLists.ENGLISH);
        InputStream in = new ByteArrayInputStream(builder.toString().getBytes("UTF-8"));

        // This hash is taken from MnemonicCode.BIP39_ENGLISH_SHA256, where it is private.
        String BIP39_ENGLISH_SHA256 = "ad90bf3beb7b0eb7e5acd74727dc0da96e0a280a258354e7293fb7e211ac03db";
        MnemonicCode mnemonicCode = new MnemonicCode(in, BIP39_ENGLISH_SHA256);

        // This throws an exception if the seed phrase is not valid.
        mnemonicCode.check(seedPhrase);

        byte[] seed = MnemonicCode.toSeed(seedPhrase, "");
        StringBuilder sb = new StringBuilder();
        for (byte b : seed) {
            sb.append(String.format("%02X", b));
        }
        return new ConcordiumHdWallet(sb.toString(), network);
    }

    /**
     * Create a Concordium hierarchical deterministic wallet from a seed.
     * @param seedAsHex the seed encoded as a hex string. Must be 128 characters.
     * @param network the network the wallet is for.
     * @return a wallet that can derive required Concordium wallet keys
     */
    public static ConcordiumHdWallet fromHex(String seedAsHex, Network network) {
        if (seedAsHex.length() != 128) {
            throw new IllegalArgumentException(
                    "The provided seed " + seedAsHex + " is invalid as its length was not 128.");
        }

        // Validate that the seed is a valid hex string.
        try {
            Hex.decodeHex(seedAsHex);
        } catch (DecoderException e) {
            throw new IllegalArgumentException("The provided seed " + seedAsHex + " is not a valid hexadecimal string", e);
        }
        
        return new ConcordiumHdWallet(seedAsHex, network);
    }

    private interface StringResultExtractor {
        String getResult(String seedAsHex, Network network);
    }
    
    /**
     * Convenience method for forwarding a call to a function in {@link CryptoJniNative} that returns
     * the JSON representation of {@link StringResult}. This functions parses that JSON and unwraps the
     * result, but throws if something went wrong.
     * @param extractor a method from {@link CryptoJniNative} that returns the JSON representation of {@link StringResult}.
     * @return the value of {@link StringResult#getResult()} from JSON received by the supplied 'extractor' method.
     */
    private String getStringResult(StringResultExtractor extractor) {
        StringResult result = null;
        try {
            String jsonStr = extractor.getResult(this.seedAsHex, this.network);
            result = JsonMapper.INSTANCE.readValue(jsonStr, StringResult.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        if (!result.isSuccess()) {
            throw CryptoJniException.from(result.getErr());
        }

        return result.getResult();
    }

    /**
     * Derives an account signing key. This is the key used to sign account transactions.
     * @param identityProviderIndex the index of the identity provider interpreted as a u32.
     * @param identityIndex the index of the identity interpreted as a u32.
     * @param credentialCounter the credential number that the signing key is for interpreted as a u32.
     * @return an account signing key.
     */
    public ED25519SecretKey getAccountSigningKey(int identityProviderIndex, int identityIndex, int credentialCounter) {
        String signingKey = getStringResult((String seedAsHex, Network network) -> {
            return CryptoJniNative.getAccountSigningKey(seedAsHex, network, identityProviderIndex, identityIndex, credentialCounter);
        });

        return ED25519SecretKey.from(signingKey);
    }

    /**
     * Derives an account public key. This is the public key that matches the account signing key.
     * @param identityProviderIndex the index of the identity provider interpreted as a u32.
     * @param identityIndex the index of the identity interpreted as a u32.
     * @param credentialCounter the credential number that the signing key is for interpreted as a u32.
     * @return an account public key.
     */
    public ED25519PublicKey getAccountPublicKey(int identityProviderIndex, int identityIndex, int credentialCounter) {
        String publicKey = getStringResult((String seedAsHex, Network network) -> {
            return CryptoJniNative.getAccountPublicKey(seedAsHex, network, identityProviderIndex, identityIndex, credentialCounter);
        });

        return ED25519PublicKey.from(publicKey);
    }

    /**
     * Derives id cred sec for a specific identity.
     * @param identityProviderIndex the index of the identity provider interpreted as a u32.
     * @param identityIndex the index of the identity interpreted as a u32.
     * @return id cred sec
     */
    public BLSSecretKey getIdCredSec(int identityProviderIndex, int identityIndex) {
        String idCredSec = getStringResult((String seedAsHex, Network network) -> {
            return CryptoJniNative.getIdCredSec(seedAsHex, network, identityProviderIndex, identityIndex);
        });

        return BLSSecretKey.from(idCredSec);
    }

    /**
     * Derives the PRF key for a specific identity.
     * @param identityProviderIndex the index of the identity provider interpreted as a u32.
     * @param identityIndex the index of the identity interpreted as a u32.
     * @return a PRF key
     */
    public BLSSecretKey getPrfKey(int identityProviderIndex, int identityIndex) {
        String prfKey = getStringResult((String seedAsHex, Network network) -> {
            return CryptoJniNative.getPrfKey(seedAsHex, network, identityProviderIndex, identityIndex);
        });

        return BLSSecretKey.from(prfKey);
    }

    /**
     * Gets the credential ID for a specific credential defined by the identity provider index,
     * identity index and the credential counter.
     * @param identityProviderIndex the index of the identity provider interpreted as a u32.
     * @param identityIndex the index of the identity interpreted as a u32.
     * @param credentialCounter the credential number of the credential to get the id for interpreted as a u8.
     * @param commitmentKey the on chain commitment key. This value can be retrieved from a node through its gRPC interface.
     * @return the credential id
     */
    public String getCredentialId(int identityProviderIndex, int identityIndex, int credentialCounter, String commitmentKey) {
        String credentialId = getStringResult((String seedAsHex, Network network) -> {
            return CryptoJniNative.getCredentialId(seedAsHex, network, identityProviderIndex, identityIndex, credentialCounter, commitmentKey);
        });

        return credentialId;
    }

    /**
     * Derives the signature blinding randomness for a specific identity.
     * @param identityProviderIndex the index of the identity provider interpreted as a u32.
     * @param identityIndex the index of the identity interpreted as a u32.
     * @return the signature blinding randomness as a hex encoded string
     */
    public String getSignatureBlindingRandomness(int identityProviderIndex, int identityIndex) {
        String blindingRandomness = getStringResult((String seedAsHex, Network network) -> {
            return CryptoJniNative.getSignatureBlindingRandomness(seedAsHex, network, identityProviderIndex, identityIndex);
        });

        return blindingRandomness;
    }

    /**
     * Derives the attribute commitment randomness for a specific attribute.
     * @param identityProviderIndex the index of the identity provider interpreted as a u32.
     * @param identityIndex the index of the identity interpreted as a u32.
     * @param credentialCounter the credential number that the attribute is randomness is for interpreted as a u32.
     * @param attribute the attribute key index interpreted as a u8.
     * @return the signature blinding randomness as a hex encoded string
     */
    public String getAttributeCommitmentRandomness(int identityProviderIndex, int identityIndex, int credentialCounter, int attribute) {
        String attributeCommitmentRandomness = getStringResult((String seedAsHex, Network network) -> {
            return CryptoJniNative.getAttributeCommitmentRandomness(seedAsHex, network, identityProviderIndex, identityIndex, credentialCounter, attribute);
        });

        return attributeCommitmentRandomness;
    }

    /**
     * Derives a verifiable credential signing key.
     * @param issuer the verifiable credential issuer contract
     * @param verifiableCredentialIndex the index of the verifiable credential to derive a signing key for interpreted as a u32
     * @return a verifiable credential signing key
     */
    public ED25519SecretKey getVerifiableCredentialSigningKey(ContractAddress issuer, int verifiableCredentialIndex) {
        String verifiableCredentialSigningKey = getStringResult((String seedAsHex, Network network) -> {
            return CryptoJniNative.getVerifiableCredentialSigningKey(seedAsHex, network, issuer, verifiableCredentialIndex);
        });

        return ED25519SecretKey.from(verifiableCredentialSigningKey);
    }

    /**
     * Derives a verifiable credential public key.
     * @param issuer the verifiable credential issuer contract
     * @param verifiableCredentialIndex the index of the verifiable credential to derive a public key for interpreted as a u32
     * @return a verifiable credential public key
     */
    public ED25519PublicKey getVerifiableCredentialPublicKey(ContractAddress issuer, int verifiableCredentialIndex) {
        String verifiableCredentialPublicKey = getStringResult((String seedAsHex, Network network) -> {
            return CryptoJniNative.getVerifiableCredentialPublicKey(seedAsHex, network, issuer, verifiableCredentialIndex);
        });

        return ED25519PublicKey.from(verifiableCredentialPublicKey);
    }

    /**
     * Derives the verifiable credential backup encryption key. This key should be used to encrypt
     * the backup file of verifiable credentials. The key is derived from the seed phrase so that
     * a user can access the backup file when only holding the seed phrase.
     * @return the verifiable credential backup encryption key
     */
    public String getVerifiableCredentialBackupEncryptionKey() {
        String verifiableCredentialEncryptionKey = getStringResult((String seedAsHex, Network network) -> {
            return CryptoJniNative.getVerifiableCredentialBackupEncryptionKey(seedAsHex, network);
        });
        return verifiableCredentialEncryptionKey;
    }
}
