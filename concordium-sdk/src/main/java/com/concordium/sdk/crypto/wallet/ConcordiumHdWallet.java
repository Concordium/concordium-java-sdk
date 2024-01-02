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
import com.concordium.sdk.crypto.wallet.wordlists.English;
import com.concordium.sdk.exceptions.CryptoJniException;
import com.concordium.sdk.serializing.JsonMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

public class ConcordiumHdWallet {

    //static block to load native library
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
     * Create a Concordium hierachical deterministic wallet from a space separated seed phrase.
     * @param seedPhrase
     * @param network
     * @return
     * @throws IOException
     * @throws MnemonicException
     */
    public static ConcordiumHdWallet fromSeedPhrase(String seedPhrase, Network network) throws IOException, MnemonicException {
        List<String> split = Arrays.asList(seedPhrase.split(" "));
        return fromSeedPhrase(split, network);
    }

    private static final String BIP39_ENGLISH_SHA256 = "ad90bf3beb7b0eb7e5acd74727dc0da96e0a280a258354e7293fb7e211ac03db";

    public static ConcordiumHdWallet fromSeedPhrase(List<String> seedPhrase, Network network) throws IOException, MnemonicException {
        // Validate whether the input seed phrase is valid or not.
        StringBuilder builder = new StringBuilder(English.wordlist);
        InputStream in = new ByteArrayInputStream(builder.toString().getBytes("UTF-8"));
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

    public static ConcordiumHdWallet fromHex(String seedAsHex, Network network) {
        if (seedAsHex.length() != 128) {
            throw new IllegalArgumentException("The provided seed " + seedAsHex + " is invalid as its length was not 128.");
        } else if (!HexadecimalValidator.isHexadecimal(seedAsHex)) {
            throw new IllegalArgumentException("The provided seed " + seedAsHex + " is not a valid hexadecimal string");
        }

        return new ConcordiumHdWallet(seedAsHex, network);
    }

    public String getAccountSigningKey(int identityProviderIndex, int identityIndex, int credentialCounter) {
        KeyResult result = null;
        try {
            String jsonStr = CryptoJniNative.getAccountSigningKey(this.seedAsHex, this.network.getValue(), identityProviderIndex, identityIndex, credentialCounter);
            result = JsonMapper.INSTANCE.readValue(jsonStr, KeyResult.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        if (!result.isSuccess()) {
            throw CryptoJniException.from(result.getErr());
        }
        
        return result.getOk();
    }
}
