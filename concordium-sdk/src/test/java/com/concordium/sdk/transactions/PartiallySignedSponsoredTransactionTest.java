package com.concordium.sdk.transactions;

import com.concordium.sdk.crypto.ed25519.ED25519SecretKey;
import com.concordium.sdk.serializing.JsonMapper;
import com.concordium.sdk.types.AccountAddress;
import com.concordium.sdk.types.Nonce;
import com.concordium.sdk.types.UInt64;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.Assert;
import org.junit.Test;

public class PartiallySignedSponsoredTransactionTest {
    private final AccountAddress SENDER = AccountAddress.from("3JwD2Wm3nMbsowCwb1iGEpnt47UQgdrtnq2qT6opJc3z2AgCrc");
    private final TransactionSigner SENDER_SIGNER = TransactionSigner.from(
            SignerEntry.from(Index.from(0), Index.from(0),
                    ED25519SecretKey.from("7100071c835a0a35e86dccba7ee9d10b89e36d1e596771cdc8ee36a17f7abbf2"))
    );
    private final AccountAddress SPONSOR = AccountAddress.from("3hYXYEPuGyhFcVRhSk2cVgKBhzVcAryjPskYk4SecpwGnoHhuM");
    private final TransactionSigner SPONSOR_SIGNER = TransactionSigner.from(
            SignerEntry.from(Index.from(0), Index.from(0),
                    ED25519SecretKey.from("cd20ea0127cddf77cf2c20a18ec4516a99528a72e642ac7deb92131a9d108ae9"))
    );
    private final String COMPLETE_TX_HASH = "5eb451836222ac9b68974fa50a06dfc36bd795e3ee832b61681d7adb887a60d8";

    @Test
    @SneakyThrows
    public void serializationSignedBySender() {
        val transaction = TransactionFactory
                .newPayloadSubmission(new RawPayload(new byte[]{0, 1, 2, 3}), UInt64.from(300))
                .sender(SENDER)
                .nonce(Nonce.from(78910))
                .expiry(Expiry.from(123456))
                .sponsoredBy(SPONSOR)
                .signAsSender(SENDER_SIGNER);
        val expectedJson = "{\"senderSignature\":\"010001000040c61f531faaeef7e4d461076fdf7e1df6db48fbf00b1cebab23f3f42e9a84a40a32d44d656e7fce40c8a7a1bc9fdf0e69e2b9cdba78e1dc5a469d1412a724b30e\",\"header\":\"0001301d6b1710b5735afc24589801213d13aa6b4478890fdfe8195bca0eaf22614e000000000001343e000000000000025600000004000000000001e240637388bcd6cccb39c324bb96265262cadb806d884b36e8541fdd12f6a1c10ee1\",\"payload\":\"00010203\"}";

        Assert.assertEquals(
                expectedJson,
                JsonMapper.INSTANCE.writeValueAsString(transaction)
        );
        Assert.assertEquals(
                transaction,
                JsonMapper.INSTANCE.readValue(expectedJson, PartiallySignedSponsoredTransaction.class)
        );
    }

    @Test
    @SneakyThrows
    public void serializationSignedBySponsor() {
        val transaction = TransactionFactory
                .newPayloadSubmission(new RawPayload(new byte[]{0, 1, 2, 3}), UInt64.from(300))
                .sender(SENDER)
                .nonce(Nonce.from(78910))
                .expiry(Expiry.from(123456))
                .sponsoredBy(SPONSOR)
                .signAsSponsor(SPONSOR_SIGNER);
        val expectedJson = "{\"sponsorSignature\":\"0100010000401663d5cfd87beb9bd6a90f37251d616a08d03dc9ba26d26776c11227e75d95a3524602e05c5aecef01dc08c282e5db383fabcabcc3b4bd134a7f11c8d3ef2b09\",\"header\":\"0001301d6b1710b5735afc24589801213d13aa6b4478890fdfe8195bca0eaf22614e000000000001343e000000000000025600000004000000000001e240637388bcd6cccb39c324bb96265262cadb806d884b36e8541fdd12f6a1c10ee1\",\"payload\":\"00010203\"}";

        Assert.assertEquals(
                expectedJson,
                JsonMapper.INSTANCE.writeValueAsString(transaction)
        );
        Assert.assertEquals(
                transaction,
                JsonMapper.INSTANCE.readValue(expectedJson, PartiallySignedSponsoredTransaction.class)
        );
    }

    @Test
    @SneakyThrows
    public void completionBySponsor() {
        val signedBySenderJson = "{\"senderSignature\":\"010001000040c61f531faaeef7e4d461076fdf7e1df6db48fbf00b1cebab23f3f42e9a84a40a32d44d656e7fce40c8a7a1bc9fdf0e69e2b9cdba78e1dc5a469d1412a724b30e\",\"header\":\"0001301d6b1710b5735afc24589801213d13aa6b4478890fdfe8195bca0eaf22614e000000000001343e000000000000025600000004000000000001e240637388bcd6cccb39c324bb96265262cadb806d884b36e8541fdd12f6a1c10ee1\",\"payload\":\"00010203\"}";
        val signedBySenderTransaction = JsonMapper.INSTANCE.readValue(signedBySenderJson, PartiallySignedSponsoredTransaction.class);
        val completeTransaction = TransactionFactory
                .completeSponsoredTransaction(signedBySenderTransaction)
                .asSponsor(SPONSOR_SIGNER);
        Assert.assertTrue(completeTransaction.getSignatures().getSponsorSignature().isPresent());
        Assert.assertEquals(
                COMPLETE_TX_HASH,
                completeTransaction.getHash().toString()
        );
    }

    @Test
    @SneakyThrows
    public void completionBySender() {
        val signedBySponsorJson = "{\"sponsorSignature\":\"0100010000401663d5cfd87beb9bd6a90f37251d616a08d03dc9ba26d26776c11227e75d95a3524602e05c5aecef01dc08c282e5db383fabcabcc3b4bd134a7f11c8d3ef2b09\",\"header\":\"0001301d6b1710b5735afc24589801213d13aa6b4478890fdfe8195bca0eaf22614e000000000001343e000000000000025600000004000000000001e240637388bcd6cccb39c324bb96265262cadb806d884b36e8541fdd12f6a1c10ee1\",\"payload\":\"00010203\"}";
        val signedBySponsorTransaction = JsonMapper.INSTANCE.readValue(signedBySponsorJson, PartiallySignedSponsoredTransaction.class);
        val completeTransaction = TransactionFactory
                .completeSponsoredTransaction(signedBySponsorTransaction)
                .asSender(SENDER_SIGNER);
        Assert.assertEquals(
                COMPLETE_TX_HASH,
                completeTransaction.getHash().toString()
        );
    }
}
