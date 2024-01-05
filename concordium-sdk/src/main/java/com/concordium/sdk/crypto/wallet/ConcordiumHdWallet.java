package com.concordium.sdk.crypto.wallet;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.bitcoinj.crypto.MnemonicCode;
import org.bitcoinj.crypto.MnemonicException;

import com.concordium.sdk.HexadecimalValidator;
import com.concordium.sdk.crypto.CryptoJniNative;
import com.concordium.sdk.crypto.NativeResolver;
import com.concordium.sdk.crypto.ed25519.ED25519PublicKey;
import com.concordium.sdk.crypto.ed25519.ED25519SecretKey;
import com.concordium.sdk.crypto.wallet.wordlists.English;
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
    private String seedAsHex;

    // The type of network that the wallet will derive keys for. This affects
    // the key derivation paths used so that mainnet keys and testnet keys are
    // not mixed.
    private Network network;

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
        StringBuilder builder = new StringBuilder(English.wordlist);
        InputStream in = new ByteArrayInputStream(builder.toString().getBytes("UTF-8"));
        String BIP39_ENGLISH_SHA256 = "ad90bf3beb7b0eb7e5acd74727dc0da96e0a280a258354e7293fb7e211ac03db";
        MnemonicCode mnemonicCode = new MnemonicCode(in, BIP39_ENGLISH_SHA256);

        // This throws an exception if the seed phrase is not valid.
        mnemonicCode.check(seedPhrase);

        byte[] seed = MnemonicCode.toSeed(seedPhrase, "");
        StringBuilder sb = new StringBuilder();
        for (byte b : seed) {
            sb.append(String.format("%02X ", b));
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
        } else if (!HexadecimalValidator.isHexadecimal(seedAsHex)) {
            throw new IllegalArgumentException("The provided seed " + seedAsHex + " is not a valid hexadecimal string");
        }

        return new ConcordiumHdWallet(seedAsHex, network);
    }

    private enum UnsignedIntegerType {
        U8,
        U32,
        U64;
    }

    private void checkUnsignedIntegerValues(UnsignedIntegerType type, long ...values) {
        long maxValue;
        switch (type) {
            case U8: 
                maxValue = 255;
                break;
            case U32:
                maxValue = 4294967295l;
                break;
            case U64:
                // Note that a long does not allow for a full u64 as it is a i64.
                // The types in the SDK would need to be updated to fully accomodate a u64,
                // and at that point this should also be changed.
                maxValue = Long.MAX_VALUE;
                break;
            default:
                throw new IllegalArgumentException("Uknown unsigned integer type was provided");
        }

        for (long value : values) {
            if (value > maxValue || value < 0) {
                throw new IllegalArgumentException("The value " + value + " is not a valid " + type);
            }
        }
    }

    private interface ExtractKey {
        String getKey(String seedAsHex, String network);
     }
    
    private String getKeyResult(ExtractKey extractor) {
        KeyResult result = null;
        try {
            String jsonStr = extractor.getKey(this.seedAsHex, this.network.getValue());
            result = JsonMapper.INSTANCE.readValue(jsonStr, KeyResult.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        if (!result.isSuccess()) {
            throw CryptoJniException.from(result.getErr());
        }

        return result.getOk();
    }

    /**
     * Derives an account signing key. This is the key used to sign account transactions.
     * @param identityProviderIndex the index of the identity provider. Must be a u32.
     * @param identityIndex the index of the identity. Must be a u32.
     * @param credentialCounter the credential number that the signing key is for. Must be a u32.
     * @return an account signing key.
     */
    public ED25519SecretKey getAccountSigningKey(long identityProviderIndex, long identityIndex, long credentialCounter) {
        checkUnsignedIntegerValues(UnsignedIntegerType.U32, identityProviderIndex, identityIndex, credentialCounter);

        String signingKey = getKeyResult((String seedAsHex, String network) -> {
            return CryptoJniNative.getAccountSigningKey(seedAsHex, network, identityProviderIndex, identityIndex, credentialCounter);
        });

        return ED25519SecretKey.from(signingKey);
    }

    /**
     * Derives an account public key. This is the public key that matches the account signing key.
     * @param identityProviderIndex the index of the identity provider. Must be a u32.
     * @param identityIndex the index of the identity. Must be a u32.
     * @param credentialCounter the credential number that the public key is for. Must be a u32.
     * @return an account public key.
     */
    public ED25519PublicKey getAccountPublicKey(long identityProviderIndex, long identityIndex, long credentialCounter) {
        checkUnsignedIntegerValues(UnsignedIntegerType.U32, identityProviderIndex, identityIndex, credentialCounter);

        String publicKey = getKeyResult((String seedAsHex, String network) -> {
            return CryptoJniNative.getAccountPublicKey(seedAsHex, network, identityProviderIndex, identityIndex, credentialCounter);
        });

        return ED25519PublicKey.from(publicKey);
    }

    /**
     * Derives id cred sec for a specific identity.
     * @param identityProviderIndex the index of the identity provider. Must be a u32.
     * @param identityIndex the index of the identity. Must be a u32.
     * @return id cred sec encoded as a hex string
     */
    public String getIdCredSec(long identityProviderIndex, long identityIndex) {
        checkUnsignedIntegerValues(UnsignedIntegerType.U32, identityProviderIndex, identityIndex);

        String idCredSec = getKeyResult((String seedAsHex, String network) -> {
            return CryptoJniNative.getIdCredSec(seedAsHex, network, identityProviderIndex, identityIndex);
        });

        return idCredSec;
    }

    /**
     * Derives the PRF key for a specific identity.
     * @param identityProviderIndex the index of the identity provider. Must be a u32.
     * @param identityIndex the index of the identity. Must be a u32.
     * @return a PRF key encoded as a hex string
     */
    public String getPrfKey(long identityProviderIndex, long identityIndex) {
        checkUnsignedIntegerValues(UnsignedIntegerType.U32, identityProviderIndex, identityIndex);

        String prfKey = getKeyResult((String seedAsHex, String network) -> {
            return CryptoJniNative.getPrfKey(seedAsHex, network, identityProviderIndex, identityIndex);
        });

        return prfKey;
    }

    /**
     * Gets the credential ID for a specific credential defined by the identity provider index,
     * identity index and the credential counter.
     * @param identityProviderIndex the index of the identity provider. Must be a u32.
     * @param identityIndex the index of the identity. Must be a u32.
     * @param credentialCounter the credential number of the credential to get the id for. Must be a u8.
     * @param commitmentKey the on chain commitment key. This value can be retrieved from a node through its gRPC interface.
     * @return the credential id
     */
    public String getCredentialId(long identityProviderIndex, long identityIndex, int credentialCounter, String commitmentKey) {
        checkUnsignedIntegerValues(UnsignedIntegerType.U32, identityProviderIndex, identityIndex);
        checkUnsignedIntegerValues(UnsignedIntegerType.U8, credentialCounter);

        String credentialId = getKeyResult((String seedAsHex, String network) -> {
            return CryptoJniNative.getCredentialId(seedAsHex, network, identityProviderIndex, identityIndex, credentialCounter, commitmentKey);
        });

        return credentialId;
    }

    /**
     * Derives the signature blinding randomness for a specific identity.
     * @param identityProviderIndex the index of the identity provider. Must be a u32.
     * @param identityIndex the index of the identity. Must be a u32.
     * @return the signature blinding randomness as a hex encoded string
     */
    public String getSignatureBlindingRandomness(long identityProviderIndex, long identityIndex) {
        checkUnsignedIntegerValues(UnsignedIntegerType.U32, identityProviderIndex, identityIndex);

        String blindingRandomness = getKeyResult((String seedAsHex, String network) -> {
            return CryptoJniNative.getSignatureBlindingRandomness(seedAsHex, network, identityProviderIndex, identityIndex);
        });

        return blindingRandomness;
    }

    /**
     * Derives the attribute commitment randomness for a specific attribute.
     * @param identityProviderIndex the index of the identity provider. Must be a u32.
     * @param identityIndex the index of the identity. Must be a u32.
     * @param credentialCounter the credential number that the attribute is randomness is for. Must be a u32.
     * @param attribute the attribute key index. Must be a u8.
     * @return the signature blinding randomness as a hex encoded string
     */
    public String getAttributeCommitmentRandomness(long identityProviderIndex, long identityIndex, long credentialCounter, int attribute) {
        checkUnsignedIntegerValues(UnsignedIntegerType.U32, identityProviderIndex, identityIndex, credentialCounter);
        checkUnsignedIntegerValues(UnsignedIntegerType.U8, attribute);

        String attributeCommitmentRandomness = getKeyResult((String seedAsHex, String network) -> {
            return CryptoJniNative.getAttributeCommitmentRandomness(seedAsHex, network, identityProviderIndex, identityIndex, credentialCounter, attribute);
        });

        return attributeCommitmentRandomness;
    }

    /**
     * Derives a verifiable credential signing key.
     * @param issuer the verifiable credential issuer contract
     * @param verifiableCredentialIndex the index of the verifiable credential to derive a signing key for
     * @return a verifiable credential signing key
     */
    public ED25519SecretKey getVerifiableCredentialSigningKey(ContractAddress issuer, long verifiableCredentialIndex) {
        long index = issuer.getIndex();
        long subindex = issuer.getSubIndex();

        checkUnsignedIntegerValues(UnsignedIntegerType.U64, index, subindex);
        checkUnsignedIntegerValues(UnsignedIntegerType.U32, verifiableCredentialIndex);

        String verifiableCredentialSigningKey = getKeyResult((String seedAsHex, String network) -> {
            return CryptoJniNative.getVerifiableCredentialSigningKey(seedAsHex, network, index, subindex, verifiableCredentialIndex);
        });

        return ED25519SecretKey.from(verifiableCredentialSigningKey);
    }

    /**
     * Derives a verifiable credential public key.
     * @param issuer the verifiable credential issuer contract
     * @param verifiableCredentialIndex the index of the verifiable credential to derive a public key for
     * @return a verifiable credential public key
     */
    public ED25519PublicKey getVerifiableCredentialPublicKey(ContractAddress issuer, long verifiableCredentialIndex) {
        long index = issuer.getIndex();
        long subindex = issuer.getSubIndex();

        checkUnsignedIntegerValues(UnsignedIntegerType.U64, index, subindex);
        checkUnsignedIntegerValues(UnsignedIntegerType.U32, verifiableCredentialIndex);

        String verifiableCredentialPublicKey = getKeyResult((String seedAsHex, String network) -> {
            return CryptoJniNative.getVerifiableCredentialPublicKey(seedAsHex, network, index, subindex, verifiableCredentialIndex);
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
        String verifiableCredentialEncryptionKey = getKeyResult((String seedAsHex, String network) -> {
            return CryptoJniNative.getVerifiableCredentialBackupEncryptionKey(seedAsHex, network);
        });
        return verifiableCredentialEncryptionKey;
    }
}
