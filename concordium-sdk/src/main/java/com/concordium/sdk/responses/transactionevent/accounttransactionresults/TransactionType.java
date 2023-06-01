package com.concordium.sdk.responses.transactionevent.accounttransactionresults;

public enum TransactionType {
    DEPLOY_MODULE,
    INIT_CONTRACT,
    UPDATE,
    TRANSFER,
    ADD_BAKER,
    REMOVE_BAKER,
    UPDATE_BAKER_STAKE,
    UPDATE_BAKER_RESTAKE_EARNINGS,
    UPDATE_BAKER_KEYS,
    UPDATE_CREDENTIAL_KEYS,
    ENCRYPTED_AMOUNT_TRANSFER,
    TRANSFER_TO_ENCRYPTED,
    TRANSFER_TO_PUBLIC,
    TRANSFER_WITH_SCHEDULE,
    UPDATE_CREDENTIALS,
    REGISTER_DATA,
    TRANSFER_WITH_MEMO,
    ENCRYPTED_AMOUNT_TRANSFER_WITH_MEMO,
    TRANSFER_WITH_SCHEDULE_AND_MEMO,
    CONFIGURE_BAKER,
    CONFIGURE_DELEGATION,

    /**
     * The types bellow used in case where result is {@link com.concordium.grpc.v2.AccountTransactionEffects.None}
      */
    NONE,
    NOT_KNOWN;

    /**
     * Parses {@link com.concordium.grpc.v2.TransactionType} to {@link TransactionType}.
     * @param transactionType {@link com.concordium.grpc.v2.TransactionType} returned by the GRPC V2 API.
     * @return parsed {@link TransactionType}.
     */
    public static TransactionType parse(com.concordium.grpc.v2.TransactionType transactionType) {
        switch (transactionType) {
            case DEPLOY_MODULE: return DEPLOY_MODULE;
            case INIT_CONTRACT: return INIT_CONTRACT;
            case UPDATE: return UPDATE;
            case TRANSFER: return TRANSFER;
            case ADD_BAKER: return ADD_BAKER;
            case REMOVE_BAKER: return REMOVE_BAKER;
            case UPDATE_BAKER_STAKE: return UPDATE_BAKER_STAKE;
            case UPDATE_BAKER_RESTAKE_EARNINGS: return UPDATE_BAKER_RESTAKE_EARNINGS;
            case UPDATE_BAKER_KEYS: return UPDATE_BAKER_KEYS;
            case UPDATE_CREDENTIAL_KEYS: return UPDATE_CREDENTIAL_KEYS;
            case ENCRYPTED_AMOUNT_TRANSFER: return ENCRYPTED_AMOUNT_TRANSFER;
            case TRANSFER_TO_ENCRYPTED: return TRANSFER_TO_ENCRYPTED;
            case TRANSFER_TO_PUBLIC: return TRANSFER_TO_PUBLIC;
            case TRANSFER_WITH_SCHEDULE: return TRANSFER_WITH_SCHEDULE;
            case UPDATE_CREDENTIALS: return UPDATE_CREDENTIALS;
            case REGISTER_DATA: return REGISTER_DATA;
            case TRANSFER_WITH_MEMO: return TRANSFER_WITH_MEMO;
            case ENCRYPTED_AMOUNT_TRANSFER_WITH_MEMO: return ENCRYPTED_AMOUNT_TRANSFER_WITH_MEMO;
            case TRANSFER_WITH_SCHEDULE_AND_MEMO: return TRANSFER_WITH_SCHEDULE_AND_MEMO;
            case CONFIGURE_BAKER: return CONFIGURE_BAKER;
            case CONFIGURE_DELEGATION: return CONFIGURE_DELEGATION;
            default: throw new IllegalArgumentException("Unable to parse TransactionType: " + transactionType);
        }
    }

}
