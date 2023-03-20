package com.concordium.sdk.transactions;

import com.concordium.sdk.types.ContractAddress;
import lombok.val;
import org.junit.Test;

import static org.junit.Assert.*;


public class UpdateContractPayloadTest {

    final static int[] EXPECTED_UPDATE_CONTRACT_PAYLOAD_DATA_BYTES = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 81, 0, 13, 67, 73, 83, 50, 45, 78, 70, 84, 46, 109, 105, 110, 116, 0, 0};

    @Test
    public void testCreatePayload() {
        byte[] emptyArray = new byte[0];
        val payload = UpdateContractPayload.from(
                0,
                ContractAddress.from(81, 0),
                "CIS2-NFT",
                "mint",
                emptyArray);

        assertArrayEquals(EXPECTED_UPDATE_CONTRACT_PAYLOAD_DATA_BYTES, TestUtils.signedByteArrayToUnsigned(payload.getBytes()));
    }
}
