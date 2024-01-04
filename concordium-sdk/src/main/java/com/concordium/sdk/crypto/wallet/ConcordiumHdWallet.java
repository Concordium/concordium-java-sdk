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

    private static long MAX_U32 = 4294967295l;

    /**
     * Validates that any provided value is a valid uint32.
     * @param values the list of values to validate as being uint32.
     */
    private void checkU32(long ...values) {
        for (long value : values) {
            if (value > MAX_U32 || value < 0) {
                throw new IllegalArgumentException("The value must be a valid uint32 value but was " + value);
            }
        }
    }

    public interface ExtractKey {
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
        checkU32(identityProviderIndex, identityIndex, credentialCounter);

        String signingKey = getKeyResult((String seedAsHex, String network) -> {
            return CryptoJniNative.getAccountSigningKey(seedAsHex, network, identityProviderIndex, identityIndex, credentialCounter);
        });

        return ED25519SecretKey.from(signingKey);
    }

    /**
     * Derives an account public key.
     * @param identityProviderIndex the index of the identity provider. Must be a u32.
     * @param identityIndex the index of the identity. Must be a u32.
     * @param credentialCounter the credential number that the public key is for. Must be a u32.
     * @return an account public key.
     */
    public ED25519PublicKey getAccountPublicKey(long identityProviderIndex, long identityIndex, long credentialCounter) {
        checkU32(identityProviderIndex, identityIndex, credentialCounter);

        String publicKey = getKeyResult((String seedAsHex, String network) -> {
            return CryptoJniNative.getAccountPublicKey(seedAsHex, network, identityProviderIndex, identityIndex, credentialCounter);
        });

        return ED25519PublicKey.from(publicKey);
    }

    public String getIdCredSec(long identityProviderIndex, long identityIndex) {
        checkU32(identityProviderIndex, identityIndex);

        String idCredSec = getKeyResult((String seedAsHex, String network) -> {
            return CryptoJniNative.getIdCredSec(seedAsHex, network, identityProviderIndex, identityIndex);
        });

        return idCredSec;
    }

    public String getPrfKey(long identityProviderIndex, long identityIndex) {
        checkU32(identityProviderIndex, identityIndex);

        String prfKey = getKeyResult((String seedAsHex, String network) -> {
            return CryptoJniNative.getPrfKey(seedAsHex, network, identityProviderIndex, identityIndex);
        });

        return prfKey;
    }

    public String getCredentialId(long identityProviderIndex, long identityIndex, long credentialCounter, String commitmentKey) {
        // TODO Credential counter has to be u8 here. Check that.
        checkU32(identityProviderIndex, identityIndex, credentialCounter);

        String credentialId = getKeyResult((String seedAsHex, String network) -> {
            return CryptoJniNative.getCredentialId(seedAsHex, network, identityProviderIndex, identityIndex, credentialCounter, commitmentKey);
        });

        return credentialId;
    }

    public String getSignatureBlindingRandomness(long identityProviderIndex, long identityIndex) {
        // TODO Credential counter has to be u8 here. Check that.
        checkU32(identityProviderIndex, identityIndex);

        String blindingRandomness = getKeyResult((String seedAsHex, String network) -> {
            return CryptoJniNative.getSignatureBlindingRandomness(seedAsHex, network, identityProviderIndex, identityIndex);
        });

        return blindingRandomness;
    }

    public String getAttributeCommitmentRandomness(long identityProviderIndex, long identityIndex, long credentialCounter, int attribute) {
        // TODO Credential counter has to be u8 here. Check that.
        checkU32(identityProviderIndex, identityIndex);

        String attributeCommitmentRandomness = getKeyResult((String seedAsHex, String network) -> {
            return CryptoJniNative.getAttributeCommitmentRandomness(seedAsHex, network, identityProviderIndex, identityIndex, credentialCounter, attribute);
        });

        return attributeCommitmentRandomness;
    }

    public ED25519SecretKey getVerifiableCredentialSigningKey(ContractAddress issuer, long verifiableCredentialIndex) {
        checkU32(verifiableCredentialIndex);

        String verifiableCredentialSigningKey = getKeyResult((String seedAsHex, String network) -> {
            return CryptoJniNative.getVerifiableCredentialSigningKey(seedAsHex, network, issuer.getIndex(), issuer.getSubIndex(), verifiableCredentialIndex);
        });

        return ED25519SecretKey.from(verifiableCredentialSigningKey);
    }

    public ED25519PublicKey getVerifiableCredentialPublicKey(ContractAddress issuer, long verifiableCredentialIndex) {
        checkU32(verifiableCredentialIndex);

        String verifiableCredentialPublicKey = getKeyResult((String seedAsHex, String network) -> {
            return CryptoJniNative.getVerifiableCredentialPublicKey(seedAsHex, network, issuer.getIndex(), issuer.getSubIndex(), verifiableCredentialIndex);
        });

        return ED25519PublicKey.from(verifiableCredentialPublicKey);
    }

    public String getVerifiableCredentialBackupEncryptionKey() {
        String verifiableCredentialEncryptionKey = getKeyResult((String seedAsHex, String network) -> {
            return CryptoJniNative.getVerifiableCredentialBackupEncryptionKey(seedAsHex, network);
        });
        return verifiableCredentialEncryptionKey;
    }
}
