package com.concordium.sdk.crypto.wallet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.bitcoinj.crypto.MnemonicException;
import org.bitcoinj.crypto.MnemonicException.MnemonicLengthException;
import org.junit.Test;

import com.concordium.sdk.crypto.ed25519.ED25519PublicKey;
import com.concordium.sdk.crypto.ed25519.ED25519SecretKey;
import com.concordium.sdk.types.ContractAddress;

import org.apache.commons.codec.binary.Hex;

public class ConcordiumHdWalletTest {
    
    @Test(expected = IllegalArgumentException.class)
    public void testWalletFromInvalidSeedHexLength() {
        String invalidSeedAsHex = "abababab";

        ConcordiumHdWallet.fromHex(invalidSeedAsHex, Network.MAINNET);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWalletFromInvalidHexadecimalSeed() {
        String invalidSeedAsHex = "abababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababa:";

        ConcordiumHdWallet.fromHex(invalidSeedAsHex, Network.MAINNET);
    }

    @Test
    public void testWalletFromValidSeedHex() {
        String validSeedAsHex = "5f9649eb1aeb049e095324f5a012188f3a0eebb56ed622b683686b0d603f114b421a86bbfa2d60ac64c3841d0a0b944abc510b50546645083a7ac9acbee27d25";

        ConcordiumHdWallet.fromHex(validSeedAsHex, Network.MAINNET);
    }

    @Test(expected = MnemonicLengthException.class)
    public void testWalletFailsFromInvalidSeedPhraseLength() throws IOException, MnemonicException{
        String invalidSeedPhrase = "hello hello hello hello";

        ConcordiumHdWallet.fromSeedPhrase(invalidSeedPhrase, Network.MAINNET);
    }

    @Test(expected = MnemonicException.MnemonicWordException.class)
    public void testWalletFailsFromInvalidSeedPhraseWord() throws IOException, MnemonicException{
        String invalidSeedPhrase = "hello hello johnson";

        ConcordiumHdWallet.fromSeedPhrase(invalidSeedPhrase, Network.MAINNET);
    }

    @Test
    public void testWalletFromValidSeedPhrase() throws IOException, MnemonicException {
        String validSeedPhrase = "plug pipe now victory right then canvas monkey treat weasel bacon skull that shaft rookie sell adjust chase trumpet depth asthma traffic code castle";

        ConcordiumHdWallet.fromSeedPhrase(validSeedPhrase, Network.MAINNET);
    }

    private static String TEST_SEED = "efa5e27326f8fa0902e647b52449bf335b7b605adc387015ec903f41d95080eb71361cbc7fb78721dcd4f3926a337340aa1406df83332c44c1cdcfe100603860";

    @Test
    public void testMainnetSigningKey() {
        ConcordiumHdWallet wallet = ConcordiumHdWallet.fromHex(TEST_SEED, Network.MAINNET);

        byte[] signingKeyBytes = wallet.getAccountSigningKey(0, 55, 7).getRawBytes();

        assertEquals("e4d1693c86eb9438feb9cbc3d561fbd9299e3a8b3a676eb2483b135f8dbf6eb1", new String(Hex.encodeHex(signingKeyBytes)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMainnetSigningKeyFailsIfU32OutOfBounds() {
        ConcordiumHdWallet wallet = ConcordiumHdWallet.fromHex(TEST_SEED, Network.MAINNET);

        wallet.getAccountSigningKey(Integer.parseUnsignedInt("4294967296"), 55, 7);
    }

    @Test
    public void testMainnetPublicKey() {
        ConcordiumHdWallet wallet = ConcordiumHdWallet.fromHex(TEST_SEED, Network.MAINNET);

        byte[] signingKeyBytes = wallet.getAccountPublicKey(1, 341, 9).getRawBytes();

        assertEquals("d54aab7218fc683cbd4d822f7c2b4e7406c41ae08913012fab0fa992fa008e98", new String(Hex.encodeHex(signingKeyBytes)));
    }

    @Test
    public void testMainnetPublicAndSigningKeyMatch() throws UnsupportedEncodingException {
        ConcordiumHdWallet wallet = ConcordiumHdWallet.fromHex(TEST_SEED, Network.MAINNET);

        ED25519SecretKey signingKey = wallet.getAccountSigningKey(0, 0, 0);
        ED25519PublicKey publicKey = wallet.getAccountPublicKey(0, 0, 0);

        byte[] message = "abcd1234abcd5678".getBytes("UTF-8");

        byte[] signature = signingKey.sign(message);

        assertTrue(publicKey.verify(message, signature));
    }

    @Test
    public void testMainnetIdCredSec() {
        ConcordiumHdWallet wallet = ConcordiumHdWallet.fromHex(TEST_SEED, Network.MAINNET);

        String idCredSec = wallet.getIdCredSec(2, 115);

        assertEquals("33b9d19b2496f59ed853eb93b9d374482d2e03dd0a12e7807929d6ee54781bb1", idCredSec);
    }

    @Test
    public void testMainnetPrfKey() {
        ConcordiumHdWallet wallet = ConcordiumHdWallet.fromHex(TEST_SEED, Network.MAINNET);

        String prfKey = wallet.getPrfKey(3, 35);

        assertEquals("4409e2e4acffeae641456b5f7406ecf3e1e8bd3472e2df67a9f1e8574f211bc5", prfKey);
    }

    @Test
    public void testMainnetCredentialId() {
        ConcordiumHdWallet wallet = ConcordiumHdWallet.fromHex(TEST_SEED, Network.MAINNET);

        String credentialId = wallet.getCredentialId(10, 50, 5, "b14cbfe44a02c6b1f78711176d5f437295367aa4f2a8c2551ee10d25a03adc69d61a332a058971919dad7312e1fc94c5a8d45e64b6f917c540eee16c970c3d4b7f3caf48a7746284878e2ace21c82ea44bf84609834625be1f309988ac523fac");

        assertEquals("8a3a87f3f38a7a507d1e85dc02a92b8bcaa859f5cf56accb3c1bc7c40e1789b4933875a38dd4c0646ca3e940a02c42d8", credentialId);
    }

    @Test
    public void testMainnetBlindingRandomness() {
        ConcordiumHdWallet wallet = ConcordiumHdWallet.fromHex(TEST_SEED, Network.MAINNET);

        String blindingRandomness = wallet.getSignatureBlindingRandomness(4, 5713);

        assertEquals("1e3633af2b1dbe5600becfea0324bae1f4fa29f90bdf419f6fba1ff520cb3167", blindingRandomness);
    }

    @Test
    public void testMainnetAttributeCommitmentRandomness() {
        ConcordiumHdWallet wallet = ConcordiumHdWallet.fromHex(TEST_SEED, Network.MAINNET);

        String attributeCommitmentRandomness = wallet.getAttributeCommitmentRandomness(5, 0, 4, 0);

        assertEquals("6ef6ba6490fa37cd517d2b89a12b77edf756f89df5e6f5597440630cd4580b8f", attributeCommitmentRandomness);
    }

    @Test
    public void testMainnetVerifiableCredentialSigningKey() {
        ConcordiumHdWallet wallet = ConcordiumHdWallet.fromHex(TEST_SEED, Network.MAINNET);
        ContractAddress issuer = new ContractAddress(2, 1);

        ED25519SecretKey verifiableCredentialSigningKey = wallet.getVerifiableCredentialSigningKey(issuer, 1);

        assertEquals("670d904509ce09372deb784e702d4951d4e24437ad3879188d71ae6db51f3301", Hex.encodeHexString(verifiableCredentialSigningKey.getRawBytes()));
    }

    @Test
    public void testMainnetVerifiableCredentialPublicKey() {
        ConcordiumHdWallet wallet = ConcordiumHdWallet.fromHex(TEST_SEED, Network.MAINNET);
        ContractAddress issuer = new ContractAddress(1232, 3);

        ED25519PublicKey verifiableCredentialPublicKey = wallet.getVerifiableCredentialPublicKey(issuer, 341);

        assertEquals("16afdb3cb3568b5ad8f9a0fa3c741b065642de8c53e58f7920bf449e63ff2bf9", Hex.encodeHexString(verifiableCredentialPublicKey.getRawBytes()));
    }

    @Test
    public void testMainnetVerifiableCredentialBackupEncryptionKey() {
        ConcordiumHdWallet wallet = ConcordiumHdWallet.fromHex(TEST_SEED, Network.MAINNET);

        String verifiableCredentialBackupEncryptionKey = wallet.getVerifiableCredentialBackupEncryptionKey();

        assertEquals("5032086037b639f116642752460bf2e2b89d7278fe55511c028b194ba77192a1", verifiableCredentialBackupEncryptionKey);
    }
}
