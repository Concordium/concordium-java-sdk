package com.concordium.sdk.transactions;

import com.concordium.sdk.crypto.bakertransactions.BakerKeys;
import com.concordium.sdk.exceptions.TransactionCreationException;
import com.concordium.sdk.types.AccountAddress;
import com.concordium.sdk.types.Nonce;
import com.concordium.sdk.types.UInt64;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;

/**
 * Provides convenient builders for account transactions.
 * <br><br>
 * Example: Create a CCD transfer transaction
 * <pre>
 * {@code
 * TransactionFactory
 *      .newTransfer(new Transfer(receiverAccountAddress, ccdAmount))
 *      .sender(senderAccountAddress)
 *      .nonce(senderNonce)
 *      .expiry(Expiry.createNew().addMinutes(5))
 *      .sign(senderSigner)
 * }
 * </pre>
 * <br>
 * Example: Create a sponsored CCD transfer transaction on the sponsor's server:
 * <pre>
 * {@code
 * TransactionFactory
 *      .newTransfer(new Transfer(receiverAccountAddress, ccdAmount))
 *      .sender(senderAccountAddress)
 *      .nonce(senderNonce)
 *      .expiry(Expiry.createNew().addMinutes(5))
 *      .sponsoredBy(sponsorAccountAddress)
 *      .signAsSponsor(sponsorSigner)
 * }
 * </pre>
 */
public class TransactionFactory {

    /**
     * General account transaction builder. Using it requires specifying
     * the cost (max energy that can be spent on executing this transaction) explicitly.
     * <br>
     * For specific transactions, such as transfer, protocol-level token transaction, etc. use dedicated builders.
     */
    public static PayloadSubmissionBuilder newPayloadSubmission(@NonNull Payload payload,
                                                                @NonNull UInt64 transactionSpecificCost) {
        return new PayloadSubmissionBuilder(payload, transactionSpecificCost);
    }

    /**
     * @return A builder for a CCD transfer transaction.
     */
    public static PayloadSubmissionBuilder newTransfer(@NonNull Transfer transfer) {
        return newPayloadSubmission(transfer, TransactionTypeCost.TRANSFER_BASE_COST.getValue());
    }

    /**
     * @return A builder for a CCD transfer transaction with a memo (short text message or binary data).
     */
    public static PayloadSubmissionBuilder newTransferWithMemo(@NonNull TransferWithMemo transferWithMemo) {
        return newPayloadSubmission(transferWithMemo, TransactionTypeCost.TRANSFER_WITH_MEMO.getValue());
    }

    /**
     * @return A builder for a data submission transaction.
     */
    public static PayloadSubmissionBuilder newRegisterData(@NonNull RegisterData registerData) {
        return newPayloadSubmission(registerData, TransactionTypeCost.REGISTER_DATA.getValue());
    }

    /**
     * @param maxContractExecutionEnergy max energy that can be spent on the initialization
     * @return A builder for a smart contract initialization transaction.
     */
    public static PayloadSubmissionBuilder newInitContract(@NonNull InitContract initContract,
                                                           @NonNull UInt64 maxContractExecutionEnergy) {
        return newPayloadSubmission(initContract, maxContractExecutionEnergy);
    }

    /**
     * @param maxContractExecutionEnergy max energy that can be spent on the update
     * @return A builder for an existing smart contract state update transaction.
     */
    public static PayloadSubmissionBuilder newUpdateContract(@NonNull UpdateContract updateContract,
                                                             @NonNull UInt64 maxContractExecutionEnergy) {
        return newPayloadSubmission(updateContract, maxContractExecutionEnergy);
    }

    /**
     * @param maxContractExecutionEnergy max energy that can be spent on the deployment
     * @return A builder for compiled smart contract module deployment transaction.
     */
    public static PayloadSubmissionBuilder newDeployModule(@NonNull DeployModule deployModule,
                                                           @NonNull UInt64 maxContractExecutionEnergy) {
        return newPayloadSubmission(deployModule, maxContractExecutionEnergy);
    }

    /**
     * @return A builder for a scheduled (delayed) CCD transfer transaction.
     */
    public static PayloadSubmissionBuilder newScheduledTransfer(@NonNull TransferSchedule transferSchedule) {
        UInt64 cost = UInt64.from((long) transferSchedule.getAmount().length * (300 + 64));
        return newPayloadSubmission(transferSchedule, cost);
    }

    /**
     * @return A builder for a scheduled (delayed) CCD transfer transaction with a memo (short text message or binary data).
     */
    public static PayloadSubmissionBuilder newScheduledTransferWithMemo(@NonNull TransferScheduleWithMemo transferScheduleWithMemo) {
        UInt64 cost = UInt64.from((long) transferScheduleWithMemo.getAmount().length * (300 + 64));
        return newPayloadSubmission(transferScheduleWithMemo, cost);
    }

    /**
     * @return A builder for a specific credential signing keys update transaction.
     */
    public static PayloadSubmissionBuilder newUpdateCredentialKeys(@NonNull UpdateCredentialKeys updateCredentialKeys) {
        val cost = UInt64.from(500L * updateCredentialKeys.getNumExistingCredentials().getValue() +
                100L * updateCredentialKeys.getKeys().getKeys().size());
        return newPayloadSubmission(updateCredentialKeys, cost);
    }

    /**
     * @return A builder for an unshielding transaction (making certain encrypted CCD amount public again).
     */
    public static PayloadSubmissionBuilder newTransferToPublic(@NonNull TransferToPublic transferToPublic) {
        return newPayloadSubmission(transferToPublic, TransactionTypeCost.TRANSFER_TO_PUBLIC.getValue());
    }

    /**
     * A builder for a validator (baker) set up or update transaction.
     */
    public static PayloadSubmissionBuilder newConfigureBaker(@NonNull ConfigureBaker configureBaker) {
        val cost = (configureBaker.getKeysWithProofs() != null)
                ? TransactionTypeCost.CONFIGURE_BAKER_WITH_PROOFS.getValue()
                : TransactionTypeCost.CONFIGURE_BAKER.getValue();
        return newPayloadSubmission(configureBaker, cost);
    }

    /**
     * A builder for an existing validator (baker) stake update transaction.
     */
    public static PayloadSubmissionBuilder newUpdateBakerStake(@NonNull CCDAmount stake) {
        return newConfigureBaker(
                ConfigureBaker
                        .builder()
                        .capital(stake)
                        .build()
        );
    }

    /**
     * A builder for an existing validator (baker) removal transaction.
     */
    public static PayloadSubmissionBuilder newRemoveBaker() {
        return newUpdateBakerStake(CCDAmount.from(0));
    }

    /**
     * A builder for an existing validator (baker) restake preference update transaction.
     */
    public static PayloadSubmissionBuilder newUpdateBakerRestakeEarnings(boolean restakeEarnings) {
        return newConfigureBaker(
                ConfigureBaker
                        .builder()
                        .restakeEarnings(restakeEarnings)
                        .build()
        );
    }

    /**
     * A builder for an existing validator (baker) keys update transaction.
     */
    public static PayloadSubmissionBuilder newUpdateBakerKeys(@NonNull AccountAddress accountAddress,
                                                              @NonNull BakerKeys bakerKeys) {
        return newConfigureBaker(
                ConfigureBaker
                        .builder()
                        .keysWithProofs(ConfigureBakerKeysPayload.getNewConfigureBakerKeysPayload(accountAddress, bakerKeys))
                        .build()
        );
    }

    /**
     * A builder for a delegation set up or update transaction.
     */
    public static PayloadSubmissionBuilder newConfigureDelegation(@NonNull ConfigureDelegation configureDelegation) {
        return newPayloadSubmission(configureDelegation, TransactionTypeCost.CONFIGURE_DELEGATION.getValue());
    }

    /**
     * A builder for a protocol-level token state update transaction.
     * This is the entry point, while the actual token operations (transfer, etc.) are defined
     * by the {@link TokenUpdate} payload.
     */
    public static PayloadSubmissionBuilder newTokenUpdate(@NonNull TokenUpdate tokenUpdate) {
        UInt64 cost = TransactionTypeCost.TOKEN_UPDATE_BASE_COST.getValue()
                .plus(tokenUpdate.getOperationsBaseCost());
        return newPayloadSubmission(tokenUpdate, cost);
    }

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static class PayloadSubmissionBuilder {
        @NonNull
        private final Payload payload;
        @NonNull
        private final UInt64 transactionSpecificCost;

        private AccountAddress sender = null;
        private Nonce nonce = null;
        private Expiry expiry = null;

        /**
         * Set the address of the account that is the source (sender) of the transaction.
         */
        public PayloadSubmissionBuilder sender(@NonNull AccountAddress sender) {
            this.sender = sender;
            return this;
        }

        /**
         * Set the time when the transaction should expire.
         */
        public PayloadSubmissionBuilder expiry(@NonNull Expiry expiry) {
            this.expiry = expiry;
            return this;
        }

        /**
         * Set the sequence number of the transaction, sender (source) account nonce.
         */
        public PayloadSubmissionBuilder nonce(@NonNull Nonce nonce) {
            this.nonce = nonce;
            return this;
        }

        /**
         * Proceed to building a sponsored transaction, for its cost to be paid by the {@code sponsor}.
         * Nonce, expiry and sender must be set before proceeding to sponsorship.
         */
        public SponsoredBuilder sponsoredBy(@NonNull AccountAddress sponsor) {
            if (sender == null) {
                throw new IllegalStateException("Sender must be set before proceeding to sponsorship");
            }
            if (nonce == null) {
                throw new IllegalStateException("Nonce must be set before proceeding to sponsorship");
            }
            if (expiry == null) {
                throw new IllegalStateException("Expiry must be set before proceeding to sponsorship");
            }
            return new SponsoredBuilder(sponsor);
        }

        /**
         * Build a signed account transaction.
         *
         * @throws TransactionCreationException if something goes wrong
         */
        public AccountTransaction sign(@NonNull TransactionSigner signer) {
            return AccountTransaction.from(sender, nonce, expiry, signer, payload, transactionSpecificCost);
        }

        @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
        public class SponsoredBuilder {
            @NonNull
            private final AccountAddress sponsor;

            /**
             * Build a partially signed sponsored transaction to be completed by the sender.
             *
             * @param senderSignatureCount Expected number of signatures by the sender, in a Singlesig wallet it is 1
             * @throws TransactionCreationException if something goes wrong
             */
            public PartiallySignedSponsoredTransaction signAsSponsor(@NonNull TransactionSigner sponsorSigner,
                                                                     int senderSignatureCount) {
                return PartiallySignedSponsoredTransaction
                        .builderForSponsor()
                        .sender(sender)
                        .nonce(nonce)
                        .expiry(expiry)
                        .senderSignatureCount(senderSignatureCount)
                        .payload(payload)
                        .transactionSpecificCost(transactionSpecificCost)
                        .sponsor(sponsor)
                        .sponsorSigner(sponsorSigner)
                        .build();
            }

            /**
             * Build a partially signed sponsored transaction to be completed by the sender.
             *
             * @throws TransactionCreationException if something goes wrong
             */
            public PartiallySignedSponsoredTransaction signAsSponsor(@NonNull TransactionSigner sponsorSigner) {
                return signAsSponsor(sponsorSigner, 1);
            }

            /**
             * Build a partially signed sponsored transaction to be completed by the sponsor.
             *
             * @param sponsorSignatureCount Expected number of signatures by the sponsor, in a Singlesig wallet it is 1
             * @throws TransactionCreationException if something goes wrong
             */
            public PartiallySignedSponsoredTransaction signAsSender(@NonNull TransactionSigner senderSigner,
                                                                    int sponsorSignatureCount) {
                return PartiallySignedSponsoredTransaction
                        .builderForSender()
                        .sender(sender)
                        .nonce(nonce)
                        .expiry(expiry)
                        .senderSigner(senderSigner)
                        .payload(payload)
                        .transactionSpecificCost(transactionSpecificCost)
                        .sponsor(sponsor)
                        .sponsorSignatureCount(sponsorSignatureCount)
                        .build();
            }

            /**
             * Build a partially signed sponsored transaction to be completed by the sponsor.
             *
             * @throws TransactionCreationException if something goes wrong
             */
            public PartiallySignedSponsoredTransaction signAsSender(@NonNull TransactionSigner senderSigner) {
                return signAsSender(senderSigner, 1);
            }
        }
    }
}
