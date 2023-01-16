package com.concordium.sdk.transactions;

import lombok.Getter;

// Types must match https://github.com/Concordium/concordium-base/blob/main/haskell-src/Concordium/Types/Execution.hs
public enum TransactionType {
    DEPLOY_MODULE((byte) 0),
    INITIALIZE_SMART_CONTRACT_INSTANCE((byte) 1),
    UPDATE_SMART_CONTRACT_INSTANCE((byte) 2),
    SIMPLE_TRANSFER((byte) 3),
    ADD_BAKER((byte) 4),
    REMOVE_BAKER((byte) 5),
    UPDATE_BAKER_STAKE((byte) 6),
    UPDATE_BAKER_RESTAKE_EARNINGS((byte) 7),
    UPDATE_BAKER_KEYS((byte) 8),
    UPDATE_CREDENTIAL_KEYS((byte) 13),
    ENCRYPTED_TRANSFER((byte) 16),
    TRANSFER_TO_ENCRYPTED((byte) 17),
    TRANSFER_TO_PUBLIC((byte) 18),
    TRANSFER_WITH_SCHEDULE((byte) 19),
    UPDATE_CREDENTIALS((byte) 20),
    REGISTER_DATA((byte) 21),
    TRANSFER_WITH_MEMO((byte) 22),
    ENCRYPTED_TRANSFER_WITH_MEMO((byte) 23),
    TRANSFER_WITH_SCHEDULE_AND_MEMO((byte) 24);

    /**
     * Number of Bytes used for Serializing {@link TransactionType}.
     */
    public static final int BYTES = 1;

    @Getter
    private final byte value;

    TransactionType(byte type) {
        this.value = type;
    }

}
