package com.concordium.sdk.transactions;

import com.concordium.sdk.crypto.SHA256;
import com.concordium.sdk.types.AccountAddress;
import com.concordium.sdk.types.UInt64;
import lombok.SneakyThrows;
import lombok.val;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class MessageSigningDigest {

    /**
     * Creates a digest for signing a regular message with {@link TransactionSigner}.
     *
     * @param address address of the account signing the message.
     * @param message message contents.
     * @return 32-byte digest which can be signed by {@link TransactionSigner}.
     */
    public static byte[] from(AccountAddress address, byte[] message) {
        // When signing a transaction, the 32 bytes of the account address
        // gets followed by the account nonce, which is uint64 and by design >= 1.
        // In order to sign a regular message and ensure that the user
        // does not accidentally sign a transaction, 0 is used as certainly invalid nonce.
        // This results in the following sequence: [32 bytes of the address, 8 zero bytes, message].
        val finalMessage = ByteBuffer
                .allocate(AccountAddress.BYTES + UInt64.BYTES + message.length)
                .put(address.getBytes())
                .put(new UInt64(0).getBytes())
                .put(message)
                .array();
        return SHA256.hash(finalMessage);
    }

    /**
     * Creates a digest for signing a regular plain-text message with {@link TransactionSigner}.
     *
     * @param address address of the account signing the message.
     * @param message plain-text message.
     * @return 32-byte digest which can be signed by {@link TransactionSigner}.
     */
    @SneakyThrows
    public static byte[] from(AccountAddress address, String message) {
        return from(address, message.getBytes(StandardCharsets.UTF_8));
    }
}
