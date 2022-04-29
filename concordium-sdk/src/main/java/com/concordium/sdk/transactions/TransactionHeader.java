package com.concordium.sdk.transactions;

<<<<<<< HEAD
import com.concordium.sdk.types.Timestamp;
=======
import com.concordium.sdk.types.Nonce;
>>>>>>> 98378411ff3b36ec1911d7b698611798d768e056
import com.concordium.sdk.types.UInt32;
import com.concordium.sdk.types.UInt64;
import lombok.*;

import java.nio.ByteBuffer;

@Getter
@EqualsAndHashCode
@ToString
public class TransactionHeader {
    private final AccountAddress sender;
    private final Nonce accountNonce;
    private final UInt64 expiry;

    @Setter
    private UInt64 maxEnergyCost;
    @Setter
    private UInt32 payloadSize;

    /**
     * Create a {@link TransactionHeader}
     * @param sender the sender {@link AccountAddress}
     * @param accountNonce the nonce.
     *                     Note. this should be the next available account nonce.
     *                     This can e.g. be retrieved via {@link com.concordium.sdk.responses.accountinfo.AccountInfo}
     * @param expiry A Unix timestamp indicating when the transaction should expire.
     */
    @Builder

    TransactionHeader(AccountAddress sender, Nonce accountNonce, UInt64 expiry) {
        this.sender = sender;
        this.accountNonce = accountNonce;
        this.expiry = expiry;
        this.maxEnergyCost = UInt64.from(0); // dummy value used for calculating the energy cost.
    }

    byte[] getBytes() {
        val buffer = ByteBuffer.allocate(AccountAddress.BYTES + UInt64.BYTES + UInt64.BYTES + UInt32.BYTES + UInt64.BYTES);
        buffer.put(sender.getBytes());
        buffer.put(accountNonce.getBytes());
        buffer.put(maxEnergyCost.getBytes());
        buffer.put(payloadSize.getBytes());
        buffer.put(expiry.getBytes());
        return buffer.array();
    }

    public static TransactionHeader fromBytes(ByteBuffer source) {
        AccountAddress sender = AccountAddress.fromBytes(source);
        Nonce accountNonce = Nonce.fromBytes(source);
        UInt64 maxEnergyCost = UInt64.fromBytes(source);
        UInt32 payloadSize = UInt32.fromBytes(source);
        UInt64 expiry = UInt64.fromBytes(source);
        TransactionHeader header = new TransactionHeader(sender, accountNonce, expiry);
        header.setMaxEnergyCost(maxEnergyCost);
        header.setPayloadSize(payloadSize);
        return header;
    }
}
