package com.concordium.sdk.crypto.wallet;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.bitcoinj.crypto.MnemonicException;
import org.bitcoinj.crypto.MnemonicException.MnemonicLengthException;
import org.junit.Test;

public class ConcordiumHdWalletTest {
    
    @Test(expected = IllegalArgumentException.class)
    public void testWalletFromInvalidSeedHexLength() {
        String invalidSeedAsHex = "abababab";

        ConcordiumHdWallet.fromHex(invalidSeedAsHex, Network.Mainnet);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWalletFromInvalidHexadecimalSeed() {
        String invalidSeedAsHex = "abababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababa:";

        ConcordiumHdWallet.fromHex(invalidSeedAsHex, Network.Mainnet);
    }

    @Test
    public void testWalletFromValidSeedHex() {
        String validSeedAsHex = "5f9649eb1aeb049e095324f5a012188f3a0eebb56ed622b683686b0d603f114b421a86bbfa2d60ac64c3841d0a0b944abc510b50546645083a7ac9acbee27d25";

        ConcordiumHdWallet.fromHex(validSeedAsHex, Network.Mainnet);
    }

    @Test(expected = MnemonicLengthException.class)
    public void testWalletFailsFromInvalidSeedPhraseLength() throws IOException, MnemonicException{
        String invalidSeedPhrase = "hello hello hello hello";

        ConcordiumHdWallet.fromSeedPhrase(invalidSeedPhrase, Network.Mainnet);
    }

    @Test(expected = MnemonicException.MnemonicWordException.class)
    public void testWalletFailsFromInvalidSeedPhraseWord() throws IOException, MnemonicException{
        String invalidSeedPhrase = "hello hello johnson";

        ConcordiumHdWallet.fromSeedPhrase(invalidSeedPhrase, Network.Mainnet);
    }

    @Test
    public void testWalletFromValidSeedPhrase() throws IOException, MnemonicException {
        String validSeedPhrase = "plug pipe now victory right then canvas monkey treat weasel bacon skull that shaft rookie sell adjust chase trumpet depth asthma traffic code castle";

        ConcordiumHdWallet.fromSeedPhrase(validSeedPhrase, Network.Mainnet);
    }

    private static String TEST_SEED = "efa5e27326f8fa0902e647b52449bf335b7b605adc387015ec903f41d95080eb71361cbc7fb78721dcd4f3926a337340aa1406df83332c44c1cdcfe100603860";

    @Test
    public void testMainnetSigningKey() {
        ConcordiumHdWallet wallet = ConcordiumHdWallet.fromHex(TEST_SEED, Network.Mainnet);

        String signingKey = wallet.getAccountSigningKey(0, 55, 7);

        assertEquals("e4d1693c86eb9438feb9cbc3d561fbd9299e3a8b3a676eb2483b135f8dbf6eb1", signingKey);
    }
}

