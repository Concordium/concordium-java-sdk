package com.concordium.sdk.transactions;

import com.concordium.sdk.crypto.bakertransactions.BakerKeys;
import com.concordium.sdk.responses.BakerId;
import com.concordium.sdk.responses.transactionstatus.OpenStatus;
import com.concordium.sdk.responses.transactionstatus.PartsPerHundredThousand;
import com.concordium.sdk.serializing.JsonMapper;
import com.concordium.sdk.types.AccountAddress;
import com.concordium.sdk.types.Nonce;
import lombok.SneakyThrows;
import lombok.val;
import org.bouncycastle.util.encoders.Hex;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.StringWriter;

import static org.junit.Assert.assertEquals;

public class ConfigureBakerTest {
    private static final AccountAddress ACCOUNT_ADDRESS =
            AccountAddress.from("48x2Uo8xCMMxwGuSQnwbqjzKtVqK5MaUud4vG7QEUgDmYkV85e");
    private static ConfigureBakerKeysPayload KEYS_WITH_PROOFS;

    @BeforeClass
    @SneakyThrows
    public static void before() {
        KEYS_WITH_PROOFS = JsonMapper.INSTANCE.readValue(
                "{\"electionVerifyKey\":\"46a5c70ad0a02a7a66548456ef945941c31d0cf4ab3015fe47b5f3a914c6ef3c\",\"signatureVerifyKey\":\"3433daaa0751039554a46e40d1d74f2332c142fd993ce61dd6037ecc2f79a10e\",\"aggregationVerifyKey\":\"b85ff29315181992ad8a8b6bf5aa67060bbd04679467e091da1a83de8d2a2123b9b6dd2819c57a1917ca17d88b022b58135cf933ea39349fe1726bb8133a7b5d5ef5a172d7d44cd8c51fa3a28df9af380b4fe33609f1d606dcfaa9fcc9c32deb\",\"proofSig\":\"2c5d2d6482dc2350b9f86f8e88fe07c9667cca0a21835680c32b5df497df89098cc39eee65b7fab238511c5bfd30c2b3a577672e9f64b0d2e3e02eebea52d907\",\"proofElection\":\"20456b948cba01a2cd23b34210ec1e5b3014caafc6a07c52b0f9ed5f9b5f6206e2b1185eae92fa0ded89ca20d27ff07f2e454d339cc8484877a6de5a70be560e\",\"proofAggregation\":\"0666df3880aa0f4f1a3a19fbd4de24d7ed10cde655feb9c7ec16473fd59d53561691e211344bf23cdbef36fb25a70a4dd4296f8b4330d06bf9b8c67daeb0521e\",\"bytes\":\"RqXHCtCgKnpmVIRW75RZQcMdDPSrMBX+R7XzqRTG7zwgRWuUjLoBos0js0IQ7B5bMBTKr8agfFKw+e1fm19iBuKxGF6ukvoN7YnKINJ/8H8uRU0znMhISHem3lpwvlYONDPaqgdRA5VUpG5A0ddPIzLBQv2ZPOYd1gN+zC95oQ4sXS1kgtwjULn4b46I/gfJZnzKCiGDVoDDK130l9+JCYzDnu5lt/qyOFEcW/0wwrOld2cun2Sw0uPgLuvqUtkHuF/ykxUYGZKtiotr9apnBgu9BGeUZ+CR2hqD3o0qISO5tt0oGcV6GRfKF9iLAitYE1z5M+o5NJ/hcmu4Ezp7XV71oXLX1EzYxR+joo35rzgLT+M2CfHWBtz6qfzJwy3rBmbfOICqD08aOhn71N4k1+0QzeZV/rnH7BZHP9WdU1YWkeIRNEvyPNvvNvslpwpN1Clvi0Mw0Gv5uMZ9rrBSHg==\"}",
                ConfigureBakerKeysPayload.class
        );
    }

    @SneakyThrows
    @Test
    public void shouldAddBakerKeysTest() {
        val payload = ConfigureBaker.builder()
                .capital(CCDAmount.fromMicro("14000000000"))
                .restakeEarnings(true)
                .openForDelegation(OpenStatus.OPEN_FOR_ALL)
                .keysWithProofs(KEYS_WITH_PROOFS)
                .metadataUrl("abc@xyz.com")
                .transactionFeeCommission(PartsPerHundredThousand.from(10000))
                .bakingRewardCommission(PartsPerHundredThousand.from(10000))
                .finalizationRewardCommission(PartsPerHundredThousand.from(100000))
                .build();


        val transaction = TransactionFactory
                .newConfigureBaker(payload)
                .sender(ACCOUNT_ADDRESS)
                .nonce(Nonce.from(123))
                .expiry(Expiry.from(413223))
                .sign(TransactionTestHelper.getValidSigner());

        assertEquals(589, transaction.getVersionedBytes().length);
        assertEquals(4700, transaction.getHeader().getMaxEnergyCost().getValue());
        assertEquals(
                "1900ff0000000342770c00010046a5c70ad0a02a7a66548456ef945941c31d0cf4ab3015fe47b5f3a914c6ef3c20456b948cba01a2cd23b34210ec1e5b3014caafc6a07c52b0f9ed5f9b5f6206e2b1185eae92fa0ded89ca20d27ff07f2e454d339cc8484877a6de5a70be560e3433daaa0751039554a46e40d1d74f2332c142fd993ce61dd6037ecc2f79a10e2c5d2d6482dc2350b9f86f8e88fe07c9667cca0a21835680c32b5df497df89098cc39eee65b7fab238511c5bfd30c2b3a577672e9f64b0d2e3e02eebea52d907b85ff29315181992ad8a8b6bf5aa67060bbd04679467e091da1a83de8d2a2123b9b6dd2819c57a1917ca17d88b022b58135cf933ea39349fe1726bb8133a7b5d5ef5a172d7d44cd8c51fa3a28df9af380b4fe33609f1d606dcfaa9fcc9c32deb0666df3880aa0f4f1a3a19fbd4de24d7ed10cde655feb9c7ec16473fd59d53561691e211344bf23cdbef36fb25a70a4dd4296f8b4330d06bf9b8c67daeb0521e000b6162634078797a2e636f6d0000271000002710000186a0",
                Hex.toHexString(transaction.getPayload().getBytes())
        );
    }


    @SneakyThrows
    @Test
    public void shouldConfigureEmptyBakerTest() {
        val payload = ConfigureBaker.builder()
                .build();

        val transaction = TransactionFactory
                .newConfigureBaker(payload)
                .sender(ACCOUNT_ADDRESS)
                .nonce(Nonce.from(123))
                .expiry(Expiry.from(413223))
                .sign(TransactionTestHelper.getValidSigner());

        assertEquals(202, transaction.getVersionedBytes().length);
        assertEquals(
                "190000",
                Hex.toHexString(transaction.getPayload().getBytes())
        );
    }

    @SneakyThrows
    @Test
    public void shouldAddBakerKeysWithoutMetadataUrlTest() {
        val payload = ConfigureBaker.builder()
                .capital(CCDAmount.fromMicro("14000000000"))
                .restakeEarnings(true)
                .openForDelegation(OpenStatus.OPEN_FOR_ALL)
                .keysWithProofs(KEYS_WITH_PROOFS)
                .transactionFeeCommission(PartsPerHundredThousand.from(10000))
                .bakingRewardCommission(PartsPerHundredThousand.from(10000))
                .finalizationRewardCommission(PartsPerHundredThousand.from(100000))
                .build();


        val transaction = TransactionFactory
                .newConfigureBaker(payload)
                .sender(ACCOUNT_ADDRESS)
                .nonce(Nonce.from(123))
                .expiry(Expiry.from(413223))
                .sign(TransactionTestHelper.getValidSigner());

        assertEquals(576, transaction.getVersionedBytes().length);
        assertEquals(
                "1900ef0000000342770c00010046a5c70ad0a02a7a66548456ef945941c31d0cf4ab3015fe47b5f3a914c6ef3c20456b948cba01a2cd23b34210ec1e5b3014caafc6a07c52b0f9ed5f9b5f6206e2b1185eae92fa0ded89ca20d27ff07f2e454d339cc8484877a6de5a70be560e3433daaa0751039554a46e40d1d74f2332c142fd993ce61dd6037ecc2f79a10e2c5d2d6482dc2350b9f86f8e88fe07c9667cca0a21835680c32b5df497df89098cc39eee65b7fab238511c5bfd30c2b3a577672e9f64b0d2e3e02eebea52d907b85ff29315181992ad8a8b6bf5aa67060bbd04679467e091da1a83de8d2a2123b9b6dd2819c57a1917ca17d88b022b58135cf933ea39349fe1726bb8133a7b5d5ef5a172d7d44cd8c51fa3a28df9af380b4fe33609f1d606dcfaa9fcc9c32deb0666df3880aa0f4f1a3a19fbd4de24d7ed10cde655feb9c7ec16473fd59d53561691e211344bf23cdbef36fb25a70a4dd4296f8b4330d06bf9b8c67daeb0521e0000271000002710000186a0",
                Hex.toHexString(transaction.getPayload().getBytes())
        );
    }

    @SneakyThrows
    @Test
    public void testCreateBakerCredentials() {
        StringWriter stringWriter = new StringWriter();
        BakerKeys.createBakerKeys().createBakerCredentials(stringWriter, BakerId.from(0));
    }

    @SneakyThrows
    @Test
    public void shouldConfigureSuspendedBakerTest() {
        val payload = ConfigureBaker.builder()
                .suspended(true)
                .build();

        val transaction = TransactionFactory
                .newConfigureBaker(payload)
                .sender(ACCOUNT_ADDRESS)
                .nonce(Nonce.from(123))
                .expiry(Expiry.from(413223))
                .sign(TransactionTestHelper.getValidSigner());

        assertEquals(203, transaction.getVersionedBytes().length);
        assertEquals(
                "19010001",
                Hex.toHexString(transaction.getPayload().getBytes())
        );
    }

    @SneakyThrows
    @Test
    public void shouldRemoveBaker() {
        val transaction = TransactionFactory
                .newRemoveBaker()
                .sender(ACCOUNT_ADDRESS)
                .nonce(Nonce.from(123))
                .expiry(Expiry.from(413223))
                .sign(TransactionTestHelper.getValidSigner());

        assertEquals(210, transaction.getVersionedBytes().length);
        assertEquals(
                "1900010000000000000000",
                Hex.toHexString(transaction.getPayload().getBytes())
        );
    }

    @SneakyThrows
    @Test
    public void shouldUpdateBakerKeys() {
        val transaction = TransactionFactory
                .newUpdateBakerKeys(KEYS_WITH_PROOFS)
                .sender(ACCOUNT_ADDRESS)
                .nonce(Nonce.from(123))
                .expiry(Expiry.from(413223))
                .sign(TransactionTestHelper.getValidSigner());

        assertEquals(554, transaction.getVersionedBytes().length);
        assertEquals(4665, transaction.getHeader().getMaxEnergyCost().getValue());
        assertEquals(
                "19000846a5c70ad0a02a7a66548456ef945941c31d0cf4ab3015fe47b5f3a914c6ef3c20456b948cba01a2cd23b34210ec1e5b3014caafc6a07c52b0f9ed5f9b5f6206e2b1185eae92fa0ded89ca20d27ff07f2e454d339cc8484877a6de5a70be560e3433daaa0751039554a46e40d1d74f2332c142fd993ce61dd6037ecc2f79a10e2c5d2d6482dc2350b9f86f8e88fe07c9667cca0a21835680c32b5df497df89098cc39eee65b7fab238511c5bfd30c2b3a577672e9f64b0d2e3e02eebea52d907b85ff29315181992ad8a8b6bf5aa67060bbd04679467e091da1a83de8d2a2123b9b6dd2819c57a1917ca17d88b022b58135cf933ea39349fe1726bb8133a7b5d5ef5a172d7d44cd8c51fa3a28df9af380b4fe33609f1d606dcfaa9fcc9c32deb0666df3880aa0f4f1a3a19fbd4de24d7ed10cde655feb9c7ec16473fd59d53561691e211344bf23cdbef36fb25a70a4dd4296f8b4330d06bf9b8c67daeb0521e",
                Hex.toHexString(transaction.getPayload().getBytes())
        );
    }

    @SneakyThrows
    @Test
    public void shouldUpdateRestakeEarnings() {
        val transaction = TransactionFactory
                .newUpdateBakerRestakeEarnings(false)
                .sender(ACCOUNT_ADDRESS)
                .nonce(Nonce.from(123))
                .expiry(Expiry.from(413223))
                .sign(TransactionTestHelper.getValidSigner());

        assertEquals(203, transaction.getVersionedBytes().length);
        assertEquals(
                "19000200",
                Hex.toHexString(transaction.getPayload().getBytes())
        );
    }
}
