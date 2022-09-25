package com.concordium.sdk.transactions;

import com.concordium.sdk.crypto.ed25519.ED25519SecretKey;
import com.concordium.sdk.types.Nonce;
import com.concordium.sdk.types.UInt64;
import lombok.SneakyThrows;
import lombok.val;
import org.apache.commons.codec.binary.Hex;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class InitContractTest {
    final static int[] EXPECTED_BLOCK_ITEM_TRANSFER_WITH_INIT_CONTRACT_VERSIONED_BYTES = new int[]{0, 0, 1, 0, 2, 0, 0, 64, 223, 36, 65, 93, 22, 38, 91, 178, 176, 145, 98, 74, 50, 3, 226, 191, 153, 90, 68, 95, 56, 211, 187, 60, 116, 115, 121, 182, 251, 152, 178, 229, 190, 208, 156, 148, 154, 13, 255, 117, 14, 199, 91, 251, 123, 43, 231, 18, 194, 97, 81, 150, 36, 89, 173, 155, 190, 128, 236, 96, 220, 245, 52, 2, 1, 0, 64, 99, 120, 1, 97, 75, 41, 117, 168, 188, 63, 243, 247, 33, 195, 83, 20, 242, 74, 228, 191, 15, 69, 199, 241, 159, 227, 202, 96, 229, 244, 22, 232, 102, 202, 240, 193, 68, 84, 2, 14, 4, 36, 17, 226, 187, 79, 98, 205, 84, 119, 101, 69, 229, 221, 157, 211, 142, 31, 129, 106, 162, 71, 123, 6, 48, 29, 107, 23, 16, 181, 115, 90, 252, 36, 88, 152, 1, 33, 61, 19, 170, 107, 68, 120, 137, 15, 223, 232, 25, 91, 202, 14, 175, 34, 97, 78, 0, 0, 0, 0, 0, 1, 52, 62, 0, 0, 0, 0, 0, 0, 12, 241, 0, 0, 0, 53, 0, 0, 0, 0, 0, 1, 226, 64, 1, 0, 0, 0, 0, 0, 0, 0, 0, 55, 238, 179, 233, 32, 37, 201, 126, 175, 64, 182, 104, 145, 119, 15, 205, 34, 217, 38, 169, 28, 174, 177, 19, 92, 124, 231, 161, 186, 151, 124, 7, 0, 8, 67, 73, 83, 50, 45, 78, 70, 84, 0, 0};
    final static int[] EXPECTED_INIT_CONTRACT_BLOCK_ITEM_BYTES = new int[]{0, 1, 0, 2, 0, 0, 64, 223, 36, 65, 93, 22, 38, 91, 178, 176, 145, 98, 74, 50, 3, 226, 191, 153, 90, 68, 95, 56, 211, 187, 60, 116, 115, 121, 182, 251, 152, 178, 229, 190, 208, 156, 148, 154, 13, 255, 117, 14, 199, 91, 251, 123, 43, 231, 18, 194, 97, 81, 150, 36, 89, 173, 155, 190, 128, 236, 96, 220, 245, 52, 2, 1, 0, 64, 99, 120, 1, 97, 75, 41, 117, 168, 188, 63, 243, 247, 33, 195, 83, 20, 242, 74, 228, 191, 15, 69, 199, 241, 159, 227, 202, 96, 229, 244, 22, 232, 102, 202, 240, 193, 68, 84, 2, 14, 4, 36, 17, 226, 187, 79, 98, 205, 84, 119, 101, 69, 229, 221, 157, 211, 142, 31, 129, 106, 162, 71, 123, 6, 48, 29, 107, 23, 16, 181, 115, 90, 252, 36, 88, 152, 1, 33, 61, 19, 170, 107, 68, 120, 137, 15, 223, 232, 25, 91, 202, 14, 175, 34, 97, 78, 0, 0, 0, 0, 0, 1, 52, 62, 0, 0, 0, 0, 0, 0, 12, 241, 0, 0, 0, 53, 0, 0, 0, 0, 0, 1, 226, 64, 1, 0, 0, 0, 0, 0, 0, 0, 0, 55, 238, 179, 233, 32, 37, 201, 126, 175, 64, 182, 104, 145, 119, 15, 205, 34, 217, 38, 169, 28, 174, 177, 19, 92, 124, 231, 161, 186, 151, 124, 7, 0, 8, 67, 73, 83, 50, 45, 78, 70, 84, 0, 0};

    @SneakyThrows
    @Test
    public void testInitContract() {
        Hash moduleRef = Hash.from("37eeb3e92025c97eaf40b66891770fcd22d926a91caeb1135c7ce7a1ba977c07");
        val transfer = InitContract.createNew(
                        InitContractPayload.from(
                                0,
                                moduleRef.getBytes(),
                                "CIS2-NFT",
                                new byte[0]),
                        UInt64.from(3000))
                .withHeader(TransactionHeader
                        .builder()
                        .sender(AccountAddress.from("3JwD2Wm3nMbsowCwb1iGEpnt47UQgdrtnq2qT6opJc3z2AgCrc"))
                        .accountNonce(Nonce.from(78910))
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

        val transferBytesLength = transfer.getBytes().length;
        assertEquals(53, transferBytesLength);

        val transferSignData = transfer.getDataToSign();
        assertEquals("9574239a5a4c13c6b59704ea2fc2f4b6dc075e4c6b8c28bf9a5b2e5adb50fa99", Hex.encodeHexString(transferSignData));

        val blockItem = transfer.toAccountTransaction().toBlockItem();

        val blockItemBytes = blockItem.getBytes();
        assertArrayEquals(EXPECTED_INIT_CONTRACT_BLOCK_ITEM_BYTES, TestUtils.signedByteArrayToUnsigned(blockItemBytes));

        val blockItemVersionedBytes = blockItem.getVersionedBytes();
        assertArrayEquals(EXPECTED_BLOCK_ITEM_TRANSFER_WITH_INIT_CONTRACT_VERSIONED_BYTES, TestUtils.signedByteArrayToUnsigned(blockItemVersionedBytes));

        val blockItemHash = blockItem.getHash();
        assertEquals("07499104dbee5b98e23c87b739d61a57493438c3f90f185a24b53902135ea4bc", blockItemHash.asHex());
    }
}
