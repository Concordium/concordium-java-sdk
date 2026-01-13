package com.concordium.sdk.transactions;

import com.concordium.sdk.crypto.ed25519.ED25519SecretKey;
import com.concordium.sdk.types.AccountAddress;
import com.concordium.sdk.types.Nonce;
import com.concordium.sdk.types.UInt64;
import lombok.SneakyThrows;
import lombok.val;
import org.apache.commons.codec.binary.Hex;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class InitContractTest {
    final static int[] EXPECTED_BLOCK_ITEM_TRANSFER_WITH_INIT_CONTRACT_VERSIONED_BYTES =
            new int[]{0, 0, 1, 0, 2, 0, 0, 64, 208, 15, 41, 133, 150, 59, 194, 128, 109, 109, 213, 49, 112, 78, 234, 29, 74, 11, 212, 39, 122, 99, 181, 37, 30, 146, 43, 84, 196, 18, 251, 106, 14, 101, 76, 3, 182, 113, 52, 13, 216, 168, 3, 192, 235, 108, 214, 117, 37, 27, 170, 222, 219, 63, 54, 167, 161, 214, 190, 79, 147, 88, 58, 4, 1, 0, 64, 47, 217, 145, 51, 79, 34, 0, 180, 95, 144, 58, 60, 227, 236, 139, 91, 18, 191, 17, 23, 47, 38, 118, 173, 217, 149, 132, 90, 230, 3, 139, 42, 139, 195, 254, 237, 188, 136, 188, 207, 64, 23, 61, 238, 230, 14, 114, 128, 11, 102, 155, 33, 217, 28, 35, 33, 157, 128, 74, 61, 204, 124, 6, 0, 48, 29, 107, 23, 16, 181, 115, 90, 252, 36, 88, 152, 1, 33, 61, 19, 170, 107, 68, 120, 137, 15, 223, 232, 25, 91, 202, 14, 175, 34, 97, 78, 0, 0, 0, 0, 0, 1, 52, 62, 0, 0, 0, 0, 0, 0, 12, 246, 0, 0, 0, 58, 0, 0, 0, 0, 0, 1, 226, 64, 1, 0, 0, 0, 0, 0, 0, 0, 0, 55, 238, 179, 233, 32, 37, 201, 126, 175, 64, 182, 104, 145, 119, 15, 205, 34, 217, 38, 169, 28, 174, 177, 19, 92, 124, 231, 161, 186, 151, 124, 7, 0, 13, 105, 110, 105, 116, 95, 67, 73, 83, 50, 45, 78, 70, 84, 0, 0};
    final static int[] EXPECTED_INIT_CONTRACT_BLOCK_ITEM_BYTES =
            new int[]{0, 1, 0, 2, 0, 0, 64, 208, 15, 41, 133, 150, 59, 194, 128, 109, 109, 213, 49, 112, 78, 234, 29, 74, 11, 212, 39, 122, 99, 181, 37, 30, 146, 43, 84, 196, 18, 251, 106, 14, 101, 76, 3, 182, 113, 52, 13, 216, 168, 3, 192, 235, 108, 214, 117, 37, 27, 170, 222, 219, 63, 54, 167, 161, 214, 190, 79, 147, 88, 58, 4, 1, 0, 64, 47, 217, 145, 51, 79, 34, 0, 180, 95, 144, 58, 60, 227, 236, 139, 91, 18, 191, 17, 23, 47, 38, 118, 173, 217, 149, 132, 90, 230, 3, 139, 42, 139, 195, 254, 237, 188, 136, 188, 207, 64, 23, 61, 238, 230, 14, 114, 128, 11, 102, 155, 33, 217, 28, 35, 33, 157, 128, 74, 61, 204, 124, 6, 0, 48, 29, 107, 23, 16, 181, 115, 90, 252, 36, 88, 152, 1, 33, 61, 19, 170, 107, 68, 120, 137, 15, 223, 232, 25, 91, 202, 14, 175, 34, 97, 78, 0, 0, 0, 0, 0, 1, 52, 62, 0, 0, 0, 0, 0, 0, 12, 246, 0, 0, 0, 58, 0, 0, 0, 0, 0, 1, 226, 64, 1, 0, 0, 0, 0, 0, 0, 0, 0, 55, 238, 179, 233, 32, 37, 201, 126, 175, 64, 182, 104, 145, 119, 15, 205, 34, 217, 38, 169, 28, 174, 177, 19, 92, 124, 231, 161, 186, 151, 124, 7, 0, 13, 105, 110, 105, 116, 95, 67, 73, 83, 50, 45, 78, 70, 84, 0, 0};

    private static final Hash moduleRef = Hash.from("37eeb3e92025c97eaf40b66891770fcd22d926a91caeb1135c7ce7a1ba977c07");

    @SneakyThrows
    @Test
    public void testInitContract() {
        val initPayload = InitContract.from(
                0,
                moduleRef.getBytes(),
                "init_CIS2-NFT",
                new byte[0]);

        val tx = TransactionFactory.newInitContract()
                .sender(AccountAddress.from("3JwD2Wm3nMbsowCwb1iGEpnt47UQgdrtnq2qT6opJc3z2AgCrc"))
                .nonce(Nonce.from(78910))
                .expiry(Expiry.from(123456))
                .maxContractExecutionEnergy(UInt64.from(3000))
                .payload(initPayload)
                .signer(TransactionSigner.from(
                        SignerEntry.from(Index.from(0), Index.from(0),
                                ED25519SecretKey.from("7100071c835a0a35e86dccba7ee9d10b89e36d1e596771cdc8ee36a17f7abbf2")),
                        SignerEntry.from(Index.from(0), Index.from(1),
                                ED25519SecretKey.from("cd20ea0127cddf77cf2c20a18ec4516a99528a72e642ac7deb92131a9d108ae9"))
                ))
                .build();
        val payload = tx.getPayload();

        val transferBytesLength = payload.getBytes().length;
        assertEquals(58, transferBytesLength);

        val transferSignData = AccountTransaction.getDataToSign(tx.getHeader(), payload);
        assertEquals("2786cf40125041fece64a1ed27b0cf6d7c0a5274ae8e2a850279fa324149e06f", Hex.encodeHexString(transferSignData));

        val blockItemBytes = tx.getBytes();
        assertArrayEquals(EXPECTED_INIT_CONTRACT_BLOCK_ITEM_BYTES, TestUtils.signedByteArrayToUnsigned(blockItemBytes));

        val blockItemVersionedBytes = tx.getVersionedBytes();
        assertArrayEquals(EXPECTED_BLOCK_ITEM_TRANSFER_WITH_INIT_CONTRACT_VERSIONED_BYTES, TestUtils.signedByteArrayToUnsigned(blockItemVersionedBytes));

        val blockItemHash = tx.getHash();
        assertEquals("111becf05b32822cb6a10bb6dfe8bee35a03f199320acb37a6bb05a804ac0a19", blockItemHash.asHex());
    }
}
