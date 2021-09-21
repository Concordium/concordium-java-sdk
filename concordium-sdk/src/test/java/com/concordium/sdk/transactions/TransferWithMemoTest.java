package com.concordium.sdk.transactions;

import com.concordium.sdk.crypto.ed25519.ED25519SecretKey;
import com.concordium.sdk.exceptions.TransactionCreationException;
import lombok.val;
import org.junit.Test;

import static com.concordium.sdk.transactions.TestUtils.EXPECTED_BLOCK_ITEM_TRANSFER_WITH_MEMO_VERSIONED_BYTES;
import static org.junit.Assert.*;

public class TransferWithMemoTest {

    @Test
    public void testCreateTransferWithMemo() {
        try {
            val transferWithMemo = TransferWithMemo.createNew(
                            AccountAddress.from("3YeRQYpKSvDRsH8hWY6KTJ6TJuYxM6MUEYW4wCVZ1je4UA8ar9"),
                            GTUAmount.fromMicro(1000000),
                            Memo.from("6b48656c6c6f20576f726c64"))
                    .withHeader(TransactionHeader
                            .builder()
                            .sender(AccountAddress.from("3a3gm7Bd2xDvRN6eMfHDXWXHyxuDaq3CQGEAXiXRTMi7pV4rax"))
                            .accountNonce(UInt64.from(1))
                            .expiry(UInt64.from(1632131765))
                            .build())
                    .signWith(
                            TransactionSigner.from(
                                    SignerEntry.from(Index.from(0), Index.from(0),
                                            ED25519SecretKey.from("b427f9ee55c966805dac37216864348452d42322a25b7ed8b7c56f7926b2e111")),
                                    SignerEntry.from(Index.from(0), Index.from(1),
                                            ED25519SecretKey.from("69ab1478b950175720a199846e114d5a25de0ddca8539744be680cf3702e695a")),
                                    SignerEntry.from(Index.from(0), Index.from(2),
                                            ED25519SecretKey.from("b8ae248d6a18bcd40187f255e9f0ef3e593190a371f7c0fa6ff6814c71b066f7"))
                            )
                    );
            val blockItem = transferWithMemo.toBlockItem();
            assertEquals("dfee5f4aede94657790cc314474c9caff76f836a99cef9ad6688c5644c36d8b0", blockItem.getHash().asHex());
        } catch (TransactionCreationException e) {
            fail("Unexpected error: " + e.getMessage());
        }
    }

    @Test
    public void testCreateTransferWithMemoTwo() {
        try {
            val transferWithMemo = TransferWithMemo.createNew(
                            AccountAddress.from("3hYXYEPuGyhFcVRhSk2cVgKBhzVcAryjPskYk4SecpwGnoHhuM"),
                            GTUAmount.fromMicro(17),
                            Memo.from(new byte[]{1, 2, 3, 4, 5}))
                    .withHeader(TransactionHeader
                            .builder()
                            .sender(AccountAddress.from("3JwD2Wm3nMbsowCwb1iGEpnt47UQgdrtnq2qT6opJc3z2AgCrc"))
                            .accountNonce(UInt64.from(78910))
                            .expiry(UInt64.from(123456))
                            .build())
                    .signWith(
                            TransactionSigner.from(
                                    SignerEntry.from(Index.from(0), Index.from(0),
                                            ED25519SecretKey.from("7100071c835a0a35e86dccba7ee9d10b89e36d1e596771cdc8ee36a17f7abbf2")),
                                    SignerEntry.from(Index.from(0), Index.from(1),
                                            ED25519SecretKey.from("cd20ea0127cddf77cf2c20a18ec4516a99528a72e642ac7deb92131a9d108ae9"))
                            )
                    );

            assertEquals(UInt64.from(608), transferWithMemo.header.getMaxEnergyCost());
            assertEquals(48, transferWithMemo.getBytes().length);

            val blockItem = transferWithMemo.toBlockItem();
            assertEquals(Hash.from("2cf00d7d5064ab6f70102a8bba4082b7d85b9b411f981f00b5994adc0b461083"), blockItem.getHash());
            assertArrayEquals(EXPECTED_BLOCK_ITEM_TRANSFER_WITH_MEMO_VERSIONED_BYTES, TestUtils.signedByteArrayToUnsigned(blockItem.getVersionedBytes()));
        } catch (TransactionCreationException e) {
            fail("Unexpected error: " + e.getMessage());
        }
    }
}
