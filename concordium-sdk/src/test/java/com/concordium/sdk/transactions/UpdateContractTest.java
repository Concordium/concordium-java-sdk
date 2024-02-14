package com.concordium.sdk.transactions;

import com.concordium.sdk.crypto.ed25519.ED25519SecretKey;
import com.concordium.sdk.types.AccountAddress;
import com.concordium.sdk.types.ContractAddress;
import com.concordium.sdk.types.Nonce;
import com.concordium.sdk.types.UInt64;
import lombok.SneakyThrows;
import lombok.val;
import org.apache.commons.codec.binary.Hex;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class UpdateContractTest {
    final static int[] EXPECTED_BLOCK_ITEM_UPDATE_CONTRACT_DATA_BYTES = new int[]{0, 1, 0, 2, 0, 0, 64, 111, 156, 134, 10, 219, 204, 40, 139, 1, 251, 94, 232, 85, 181, 230, 118, 68, 206, 242, 170, 45, 219, 56, 0, 36, 62, 200, 159, 219, 117, 236, 187, 214, 104, 143, 127, 50, 71, 109, 148, 117, 61, 38, 43, 158, 45, 202, 47, 234, 2, 238, 139, 203, 134, 13, 162, 44, 229, 89, 7, 51, 84, 149, 7, 1, 0, 64, 240, 217, 99, 135, 34, 235, 106, 185, 4, 183, 52, 116, 244, 114, 218, 166, 53, 198, 20, 254, 75, 196, 81, 42, 49, 47, 111, 47, 176, 241, 167, 204, 20, 127, 126, 57, 232, 100, 187, 63, 96, 58, 98, 207, 102, 169, 240, 111, 195, 20, 93, 33, 90, 158, 203, 50, 10, 19, 201, 8, 120, 254, 229, 8, 48, 29, 107, 23, 16, 181, 115, 90, 252, 36, 88, 152, 1, 33, 61, 19, 170, 107, 68, 120, 137, 15, 223, 232, 25, 91, 202, 14, 175, 34, 97, 78, 0, 0, 0, 0, 0, 1, 52, 62, 0, 0, 0, 0, 0, 0, 12, 230, 0, 0, 0, 42, 0, 0, 0, 0, 0, 1, 226, 64, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 81, 0, 0, 0, 0, 0, 0, 0, 0, 0, 13, 67, 73, 83, 50, 45, 78, 70, 84, 46, 109, 105, 110, 116, 0, 0};
    final static int[] EXPECTED_BLOCK_ITEM_UPDATE_CONTRACT_VERSIONED_DATA_BYTES = new int[]{0, 1, 0, 2, 0, 0, 64, 111, 156, 134, 10, 219, 204, 40, 139, 1, 251, 94, 232, 85, 181, 230, 118, 68, 206, 242, 170, 45, 219, 56, 0, 36, 62, 200, 159, 219, 117, 236, 187, 214, 104, 143, 127, 50, 71, 109, 148, 117, 61, 38, 43, 158, 45, 202, 47, 234, 2, 238, 139, 203, 134, 13, 162, 44, 229, 89, 7, 51, 84, 149, 7, 1, 0, 64, 240, 217, 99, 135, 34, 235, 106, 185, 4, 183, 52, 116, 244, 114, 218, 166, 53, 198, 20, 254, 75, 196, 81, 42, 49, 47, 111, 47, 176, 241, 167, 204, 20, 127, 126, 57, 232, 100, 187, 63, 96, 58, 98, 207, 102, 169, 240, 111, 195, 20, 93, 33, 90, 158, 203, 50, 10, 19, 201, 8, 120, 254, 229, 8, 48, 29, 107, 23, 16, 181, 115, 90, 252, 36, 88, 152, 1, 33, 61, 19, 170, 107, 68, 120, 137, 15, 223, 232, 25, 91, 202, 14, 175, 34, 97, 78, 0, 0, 0, 0, 0, 1, 52, 62, 0, 0, 0, 0, 0, 0, 12, 230, 0, 0, 0, 42, 0, 0, 0, 0, 0, 1, 226, 64, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 81, 0, 0, 0, 0, 0, 0, 0, 0, 0, 13, 67, 73, 83, 50, 45, 78, 70, 84, 46, 109, 105, 110, 116, 0, 0};

    @SneakyThrows
    @Test
    public void updateContractTest() {
        byte[] emptyArray = new byte[0];
        UpdateContract updateContractPayload = UpdateContract.from(0, ContractAddress.from(81, 0), "CIS2-NFT", "mint", emptyArray);
        UpdateContractTransaction tx = TransactionFactory.newUpdateContract()
                .payload(updateContractPayload)
                .sender(AccountAddress.from("3JwD2Wm3nMbsowCwb1iGEpnt47UQgdrtnq2qT6opJc3z2AgCrc"))
                .nonce(Nonce.from(78910))
                .expiry(Expiry.from(123456))
                .maxEnergyCost(UInt64.from(3000))
                .signer(TransactionSigner.from(
                        SignerEntry.from(Index.from(0), Index.from(0),
                                ED25519SecretKey.from("7100071c835a0a35e86dccba7ee9d10b89e36d1e596771cdc8ee36a17f7abbf2")),
                        SignerEntry.from(Index.from(0), Index.from(1),
                                ED25519SecretKey.from("cd20ea0127cddf77cf2c20a18ec4516a99528a72e642ac7deb92131a9d108ae9"))
                ))
                .build();
        val payload = tx.getPayload();
        val txLength = payload.getBytes().length;
        assertEquals(42, txLength);

        val transferDataToSign = payload.getDataToSign();
        assertEquals("02b15fc092aa1e63035a18cb1b4f62a9d2a6186bdb6147f85525bcb8771ab5b7", Hex.encodeHexString(transferDataToSign));
        val blockItem = payload.toAccountTransaction();

        val blockItemBytes = blockItem.getBytes();
        assertArrayEquals(EXPECTED_BLOCK_ITEM_UPDATE_CONTRACT_DATA_BYTES, TestUtils.signedByteArrayToUnsigned(blockItemBytes));

        val blockItemVersionedBytes = blockItem.getBytes();
        assertArrayEquals(EXPECTED_BLOCK_ITEM_UPDATE_CONTRACT_VERSIONED_DATA_BYTES, TestUtils.signedByteArrayToUnsigned(blockItemVersionedBytes));

        val blockItemHash = blockItem.getHash();
        assertEquals("0a869928f2491d0652a708eac0231582e24c0cc036481f209862147856531d20", blockItemHash.asHex());
    }
}
