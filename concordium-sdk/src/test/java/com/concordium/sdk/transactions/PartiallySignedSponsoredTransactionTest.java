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
    private final String SIGNED_BY_SENDER_JSON = "{\"senderSignature\":\"01000100004099ecf806f9dbcbba1f7dafa41f029cc342fd630b8ae23f047e87c1e0b7c271a73a5d0a6b6071ab72070055964de66a7bd1b1d417098537cfd4d8920a3c8afd0d\",\"header\":\"0001301d6b1710b5735afc24589801213d13aa6b4478890fdfe8195bca0eaf22614e000000000001343e000000000000025600000004000000000001e240637388bcd6cccb39c324bb96265262cadb806d884b36e8541fdd12f6a1c10ee1\",\"payload\":\"00010203\"}";
    private final String SIGNED_BY_SPONSOR_JSON = "{\"sponsorSignature\":\"0100010000400005d925b3e8cb69693e6940e110004c0c94fb70f94d2cb43b73a2b2a6b935ce81bf0f500fdbffedd5f852be7c0dbca80191fd9ea596cc1875eec7024803190b\",\"header\":\"0001301d6b1710b5735afc24589801213d13aa6b4478890fdfe8195bca0eaf22614e000000000001343e000000000000025600000004000000000001e240637388bcd6cccb39c324bb96265262cadb806d884b36e8541fdd12f6a1c10ee1\",\"payload\":\"00010203\"}";
    private final String COMPLETE_TX_HASH = "68e31a4ef7bd0f53e5ede99e62a9d702981091a0a86097110df70f56a4b2de57";

    @Test
    @SneakyThrows
    public void creationBySender() {
        val transaction = TransactionFactory
                .newPayloadSubmission(new RawPayload(new byte[]{0, 1, 2, 3}), UInt64.from(300))
                .sender(SENDER)
                .nonce(Nonce.from(78910))
                .expiry(Expiry.from(123456))
                .sponsoredBy(SPONSOR)
                .signAsSender(SENDER_SIGNER);
        Assert.assertEquals(
                SIGNED_BY_SENDER_JSON,
                JsonMapper.INSTANCE.writeValueAsString(transaction)
        );
        Assert.assertEquals(
                transaction,
                JsonMapper.INSTANCE.readValue(SIGNED_BY_SENDER_JSON, PartiallySignedSponsoredTransaction.class)
        );
    }

    @Test
    @SneakyThrows
    public void creationBySponsor() {
        val transaction = TransactionFactory
                .newPayloadSubmission(new RawPayload(new byte[]{0, 1, 2, 3}), UInt64.from(300))
                .sender(SENDER)
                .nonce(Nonce.from(78910))
                .expiry(Expiry.from(123456))
                .sponsoredBy(SPONSOR)
                .signAsSponsor(SPONSOR_SIGNER);
        Assert.assertEquals(
                SIGNED_BY_SPONSOR_JSON,
                JsonMapper.INSTANCE.writeValueAsString(transaction)
        );
        Assert.assertEquals(
                transaction,
                JsonMapper.INSTANCE.readValue(SIGNED_BY_SPONSOR_JSON, PartiallySignedSponsoredTransaction.class)
        );
    }

    @Test
    @SneakyThrows
    public void completionBySponsor() {
        val signedBySenderTransaction = JsonMapper.INSTANCE.readValue(SIGNED_BY_SENDER_JSON, PartiallySignedSponsoredTransaction.class);
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
        val signedBySponsorTransaction = JsonMapper.INSTANCE.readValue(SIGNED_BY_SPONSOR_JSON, PartiallySignedSponsoredTransaction.class);
        val completeTransaction = TransactionFactory
                .completeSponsoredTransaction(signedBySponsorTransaction)
                .asSender(SENDER_SIGNER);
        Assert.assertEquals(
                COMPLETE_TX_HASH,
                completeTransaction.getHash().toString()
        );
    }
}
