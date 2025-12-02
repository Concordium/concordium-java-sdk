package com.concordium.sdk.transactions;

import com.concordium.sdk.serializing.CborMapper;
import com.concordium.sdk.serializing.JsonMapper;
import lombok.SneakyThrows;
import lombok.val;
import org.apache.commons.codec.binary.Hex;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class HashTest {
    @Test
    public void createHashFromBlockHash() {
        try {
            val hexHash = "3d52e63350bfd21676ecbf6ce29688e3be6bff86cbacfe138aac107b64d29ba1";
            val blockHash = Hash.from(hexHash);
            val hash = blockHash.asHex();
            assertEquals(hexHash, hash);
        } catch (Exception e) {
            fail("Unexpected error: " + e.getMessage());
        }
    }

    @Test
    public void createHashFromTransactionHash() {
        try {
            val transactionHash = "78674107c228958752170db61c5a74929e990440d5da25975c6c6853f98db674";
            val txHash = Hash.from(transactionHash);
            val hash = txHash.asHex();
            assertEquals(transactionHash, hash);
        } catch (Exception e) {
            fail("Unexpected error: " + e.getMessage());
        }
    }

    @Test
    @SneakyThrows
    public void jsonSerialization() {
        val hexHash = "3d52e63350bfd21676ecbf6ce29688e3be6bff86cbacfe138aac107b64d29ba1";
        val blockHash = Hash.from(hexHash);
        val expected = "\"3d52e63350bfd21676ecbf6ce29688e3be6bff86cbacfe138aac107b64d29ba1\"";
        Assert.assertEquals(
                expected,
                JsonMapper.INSTANCE.writeValueAsString(blockHash)
        );
        Assert.assertEquals(
                blockHash,
                JsonMapper.INSTANCE.readValue(expected, Hash.class)
        );
    }

    @Test
    @SneakyThrows
    public void cborSerialization() {
        val hexHash = "3d52e63350bfd21676ecbf6ce29688e3be6bff86cbacfe138aac107b64d29ba1";
        val blockHash = Hash.from(hexHash);
        val expectedHex = "58203d52e63350bfd21676ecbf6ce29688e3be6bff86cbacfe138aac107b64d29ba1";
        Assert.assertEquals(
                expectedHex,
                Hex.encodeHexString(CborMapper.INSTANCE.writeValueAsBytes(blockHash))
        );
        Assert.assertEquals(
                blockHash,
                CborMapper.INSTANCE.readValue(Hex.decodeHex(expectedHex), Hash.class)
        );
    }
}
