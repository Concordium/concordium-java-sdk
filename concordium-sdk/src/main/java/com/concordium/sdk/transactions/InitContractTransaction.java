package com.concordium.sdk.transactions;


import com.concordium.sdk.exceptions.TransactionCreationException;
import com.concordium.sdk.types.UInt64;
import lombok.Builder;
import lombok.Getter;
import lombok.val;

import java.util.Objects;

@Getter
public class InitContractTransaction extends AbstractTransaction {

    private final InitContractPayload payload;
    private final AccountAddress sender;
    private final AccountNonce nonce;
    private final Expiry expiry;
    private final TransactionSigner signer;

    private BlockItem blockItem;
    private final UInt64 maxEnergyCost;

    @Builder
    public InitContractTransaction(InitContractPayload payload,
                                   AccountAddress sender,
                                   AccountNonce nonce,
                                   Expiry expiry,
                                   TransactionSigner signer,
                                   UInt64 maxEnergyCost) throws TransactionCreationException {
        this.payload = payload;
        this.sender = sender;
        this.nonce = nonce;
        this.expiry = expiry;
        this.signer = signer;
        this.maxEnergyCost = maxEnergyCost;
    }

    public static InitContractTransactionBuilder builder() {
        return new CustomBuilder();
    }

    @Override
    public BlockItem getBlockItem() {
        return blockItem;
    }

    private static class CustomBuilder extends InitContractTransactionBuilder {
        @Override
        public InitContractTransaction build() throws TransactionCreationException {
            val transaction = super.build();
            verifyInitContractInput(
                    transaction.sender,
                    transaction.nonce,
                    transaction.expiry,
                    transaction.signer,
                    transaction.payload
            );
            transaction.blockItem = initializeSmartContractInstance(transaction).toBlockItem();

            return transaction;
        }


        private Payload initializeSmartContractInstance(InitContractTransaction transaction) throws TransactionCreationException {
            return InitContract.createNew(
                            transaction.payload,
                            transaction.maxEnergyCost).
                    withHeader(TransactionHeader.builder()
                            .sender(transaction.sender)
                            .accountNonce(transaction.nonce.getNonce())
                            .expiry(transaction.expiry.getValue())
                            .build())
                    .signWith(transaction.signer);
        }

        static void verifyInitContractInput(AccountAddress sender, AccountNonce nonce, Expiry expiry, TransactionSigner signer, InitContractPayload payload) throws TransactionCreationException {
            Transaction.verifyCommonInput(sender, nonce, expiry, signer);
            if (Objects.isNull(payload)) {
                throw TransactionCreationException.from(new IllegalArgumentException("Payload cannot be null"));
            }
        }

    }
}
