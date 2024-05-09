package com.concordium.sdk.transactions;

import com.concordium.sdk.types.AccountAddress;
import lombok.SneakyThrows;
import lombok.val;
import org.apache.commons.codec.binary.Hex;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertArrayEquals;

public class MessageSigningDigestTest {
    private static final AccountAddress SIGNER_ADDRESS =
            AccountAddress.from("3WZE6etUvVp1eyhEtTxqZrQaanTAZnZCHEmZmDyCbCwxnmQuPE"); //  Dummy address

    private final Map<String, String> hexTestVectors = new HashMap<>();
    private final Map<String, String> plainTestVectors = new HashMap<>();

    @Before
    @SneakyThrows
    public void setup() {
        hexTestVectors.put(
                "01000000010000000000000000000000000000000000000000000000000000000000000000ffffffff4d04ffff001d0104455468652054696d65732030332f4a616e2f32303039204368616e63656c6c6f72206f6e206272696e6b206f66207365636f6e64206261696c6f757420666f722062616e6b73ffffffff0100f2052a01000000434104678afdb0fe5548271967f1a67130b7105cd6a828e03909a67962e0ea1f61deb649f6bc3f4cef38c4f35504e51ec112de5c384df7ba0b8d578a4c702b6bf11d5fac00000000",
                "e8a1287efd3a0d41e4b7b0a7951c5b67421bd87e659408494c8174c5bef3df09"
        );
        hexTestVectors.put(
                "d090d0b1d180d0b0d0bad0b0d0b4d0b0d0b1d180d0b0",
                "f31c19fa2087a2f04f3cbb91cad10c4081969f29e8407930ce13f50682d9c470"
        );
        hexTestVectors.put(
                "58e7030200000000e2753e1df53f25ed482ed42f66e69651961783f3e2978a8512a68b9408f30db600e2753e1df53f25ed482ed42f66e69651961783f3e2978a8512a68b9408f30db600e2753e1df53f25ed482ed42f66e69651961783f3e2978a8512a68b9408f30db6e2753e1df53f25ed482ed42f66e69651961783f3e2978a8512a68b9408f30db60300000000000000000000000000000037a2a8e52efad975dbf6580e7734e4f249eaa5ea8a763e934a8671cd7e446499632f567c9321405ce201a0a38615da41efe259ede154ff45ad96cdf860718e79bde07cff72c4d119c644552a8c7f0c413f5cf5390b0ea0458993d6d6374bd90437a2a8e52efad975dbf6580e7734e4f249eaa5ea8a763e934a8671cd7e44649920ccb643bd010000000300616263",
                "38b12945d2f06d444b177ba4e4f1038f070646bfd313adbe5b8e1401ece2702c"
        );
        hexTestVectors.put(
                "",
                "c425a3ee7a5706ae8e09884693bd2ba80e843bc7983bac559a3f9edb545acf0c"
        );

        plainTestVectors.put(
                "The Times 03/Jan/2009 Chancellor on brink of second bailout for banks",
                "6d054bda20eb34b96d7e2e43df2238f33d50982d183e2589a520762b1a766f90"
        );
        plainTestVectors.put(
                "Абракадабра",
                "f31c19fa2087a2f04f3cbb91cad10c4081969f29e8407930ce13f50682d9c470"
        );
        plainTestVectors.put(
                "",
                "c425a3ee7a5706ae8e09884693bd2ba80e843bc7983bac559a3f9edb545acf0c"
        );
    }

    @SneakyThrows
    @Test
    public void testDigestHex() {
        for (String messageHex : hexTestVectors.keySet()) {
            val message = Hex.decodeHex(messageHex);
            val expected = Hex.decodeHex(hexTestVectors.get(messageHex));
            val actual = MessageSigningDigest.from(SIGNER_ADDRESS, message);
            assertArrayEquals(expected,actual);
        }
    }

    @SneakyThrows
    @Test
    public void testDigestPlain() {
        for (String message : plainTestVectors.keySet()) {
            val expected = Hex.decodeHex(plainTestVectors.get(message));
            val actual = MessageSigningDigest.from(SIGNER_ADDRESS, message);
            assertArrayEquals(expected,actual);
        }
    }
}
