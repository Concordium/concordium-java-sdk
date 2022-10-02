package com.concordium.sdk.transactions;


import com.concordium.sdk.exceptions.TransactionCreationException;
import lombok.Builder;
import lombok.Getter;
import lombok.val;

import java.util.Objects;

@Getter
public class RegisterDataTransaction extends AbstractTransaction {
    private final AccountAddress sender;
    private final AccountNonce nonce;
    private final Expiry expiry;
    private final TransactionSigner signer;
    private final Data data;

    private BlockItem blockItem;

    @Builder
    public RegisterDataTransaction(AccountAddress sender,
                               Data data,
                               AccountNonce nonce,
                               Expiry expiry,
                               TransactionSigner signer) throws TransactionCreationException {
        this.sender = sender;
        this.data = data;
        this.nonce = nonce;
        this.expiry = expiry;
        this.signer = signer;
    }

    public static RegisterDataTransactionBuilder builder() {
        return new CustomBuilder();
    }

    @Override
    public BlockItem getBlockItem() {
        return blockItem;
    }

    private static class CustomBuilder extends RegisterDataTransaction.RegisterDataTransactionBuilder {
        @Override
        public RegisterDataTransaction build() throws TransactionCreationException {
            val transaction = super.build();
            verifyRegisterDataInput(transaction.sender, transaction.nonce, transaction.expiry, transaction.getData(), transaction.signer);
            transaction.blockItem = createRegisterData(transaction).toBlockItem();
            return transaction;
        }

        private Payload createRegisterData(RegisterDataTransaction transaction) throws TransactionCreationException {
            return RegisterData.createNew(
                            transaction.getData()).
                    withHeader(TransactionHeader.builder()
                            .sender(transaction.sender)
                            .accountNonce(transaction.nonce.getNonce())
                            .expiry(transaction.expiry.getValue())
                            .build())
                    .signWith(transaction.signer);
        }

        static void verifyRegisterDataInput(
                AccountAddress sender,
                AccountNonce nonce,
                Expiry expiry,
                Data data,
                TransactionSigner signer) throws TransactionCreationException {
            Transaction.verifyTransactionInput(sender, nonce, expiry, signer);

            if (Objects.isNull(data)) {
                throw TransactionCreationException.from(new IllegalArgumentException("Data cannot be null"));
            }
            if (Objects.isNull(signer) || signer.isEmpty()) {
                throw TransactionCreationException.from(new IllegalArgumentException("Signer cannot be null or empty"));
            }
        }
    }
}
