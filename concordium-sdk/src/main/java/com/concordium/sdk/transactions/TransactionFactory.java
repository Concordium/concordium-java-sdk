package com.concordium.sdk.transactions;

import com.concordium.sdk.exceptions.TransactionCreationException;
import com.concordium.sdk.types.AccountAddress;
import com.concordium.sdk.types.Nonce;
import com.concordium.sdk.types.UInt32;
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
 * AccountTransaction tx =
 *      TransactionFactory
 *          .newTransfer(new Transfer(receiverAccountAddress, ccdAmount))
 *          .sender(senderAccountAddress)
 *          .nonce(senderNonce)
 *          .expiry(Expiry.createNew().addMinutes(5))
 *          .sign(senderSigner)
 * }
 * </pre>
 * <br>
 * Example: Create a sponsored CCD transfer transaction on the sponsor's server:
 * <pre>
 * {@code
 * PartiallySignedSponsoredTransaction partiallySignedTx =
 *      TransactionFactory
 *          .newTransfer(new Transfer(receiverAccountAddress, ccdAmount))
 *          .sender(senderAccountAddress)
 *          .nonce(senderNonce)
 *          .expiry(Expiry.createNew().addMinutes(5))
 *          .sponsoredBy(sponsorAccountAddress)
 *          .signAsSponsor(sponsorSigner)
 * }
 * </pre>
 <br>
 * Example: Complete a sponsored CCD transfer transaction in the sender's wallet:
 * <pre>
 * {@code
 * AccountTransactionV1 tx =
 *      TransactionFactory
 *          .completeSponsoredTransaction(partiallySignedTx)
 *          .asSender(senderSigner)
 * }
 * </pre>
 */
public class TransactionFactory {

    /**
     * Complete a partially signed sponsored transaction.
     */
    public static SponsoredCompletionBuilder completeSponsoredTransaction(@NonNull PartiallySignedSponsoredTransaction partiallySigned) {
        return new SponsoredCompletionBuilder(partiallySigned);
    }

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
    public static PayloadSubmissionBuilder newUpdateBakerKeys(@NonNull ConfigureBakerKeysPayload keysWithProofs) {
        return newConfigureBaker(
                ConfigureBaker
                        .builder()
                        .keysWithProofs(keysWithProofs)
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
        public PartiallySignedSponsoredBuilder sponsoredBy(@NonNull AccountAddress sponsor) {
            if (sender == null) {
                throw new IllegalStateException("Sender must be set before proceeding to sponsorship");
            }
            if (nonce == null) {
                throw new IllegalStateException("Nonce must be set before proceeding to sponsorship");
            }
            if (expiry == null) {
                throw new IllegalStateException("Expiry must be set before proceeding to sponsorship");
            }
            return new PartiallySignedSponsoredBuilder(sponsor);
        }

        /**
         * Build a signed account transaction.
         *
         * @throws TransactionCreationException if something goes wrong
         */
        public AccountTransaction sign(@NonNull TransactionSigner signer) {
            try {
                val payloadSize = payload.getBytes().length;
                val header = TransactionHeader
                        .builder()
                        .sender(sender)
                        .nonce(nonce)
                        .expiry(expiry)
                        .payloadSize(UInt32.from(payloadSize))
                        .maxEnergyCost(
                                TransactionHeader.calculateMaxEnergyCost(
                                        signer.size(),
                                        payloadSize,
                                        transactionSpecificCost
                                )
                        )
                        .build();
                val signature = signer.sign(AccountTransaction.getDataToSign(header, payload));
                return new AccountTransaction(signature, header, payload);
            } catch (Exception e) {
                throw TransactionCreationException.from(e);
            }
        }

        @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
        public class PartiallySignedSponsoredBuilder {
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
                try {
                    val payloadBytes = payload.getBytes();
                    val rawPayload = new RawPayload(payloadBytes);
                    val header = TransactionHeaderV1
                            .builder()
                            .sender(sender)
                            .nonce(nonce)
                            .expiry(expiry)
                            .payloadSize(UInt32.from(payloadBytes.length))
                            .sponsor(sponsor)
                            .maxEnergyCost(
                                    TransactionHeaderV1.calculateMaxEnergyCost(
                                            senderSignatureCount,
                                            sponsorSigner.size(),
                                            payloadBytes.length,
                                            transactionSpecificCost
                                    )
                            )
                            .build();
                    val sponsorSignature = sponsorSigner.sign(AccountTransactionV1.getDataToSign(header, rawPayload));
                    return new PartiallySignedSponsoredTransaction(
                            null,
                            sponsorSignature,
                            header,
                            rawPayload
                    );
                } catch (Exception e) {
                    throw TransactionCreationException.from(e);
                }
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
                try {
                    val payloadBytes = payload.getBytes();
                    val rawPayload = new RawPayload(payloadBytes);
                    val header = TransactionHeaderV1
                            .builder()
                            .sender(sender)
                            .nonce(nonce)
                            .expiry(expiry)
                            .payloadSize(UInt32.from(payloadBytes.length))
                            .sponsor(sponsor)
                            .maxEnergyCost(
                                    TransactionHeaderV1.calculateMaxEnergyCost(
                                            senderSigner.size(),
                                            sponsorSignatureCount,
                                            payloadBytes.length,
                                            transactionSpecificCost
                                    )
                            )
                            .build();
                    val senderSignature = senderSigner.sign(AccountTransactionV1.getDataToSign(header, rawPayload));
                    return new PartiallySignedSponsoredTransaction(
                            senderSignature,
                            null,
                            header,
                            rawPayload
                    );
                } catch (Exception e) {
                    throw TransactionCreationException.from(e);
                }
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


    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static class SponsoredCompletionBuilder {
        @NonNull
        private final PartiallySignedSponsoredTransaction partiallySigned;

        /**
         * Complete (sign) the transaction that it is already signed by the sponsor.
         */
        public AccountTransactionV1 asSender(@NonNull TransactionSigner senderSigner) {
            try {
                if (!partiallySigned.getSponsorSignature().isPresent()) {
                    throw new IllegalStateException("Missing sponsor signature");
                }
                return new AccountTransactionV1(
                        new TransactionSignaturesV1(
                                senderSigner.sign(
                                        AccountTransactionV1.getDataToSign(
                                                partiallySigned.getHeader(),
                                                partiallySigned.getPayload()
                                        )
                                ),
                                partiallySigned.getSponsorSignature().get()
                        ),
                        partiallySigned.getHeader(),
                        partiallySigned.getPayload()
                );
            } catch (Exception e) {
                throw TransactionCreationException.from(e);
            }
        }

        /**
         * Complete (sign) the transaction that it is already signed by the sender.
         */
        public AccountTransactionV1 asSponsor(@NonNull TransactionSigner sponsorSigner) {
            try {
                if (!partiallySigned.getSenderSignature().isPresent()) {
                    throw new IllegalStateException("Missing sender signature");
                }
                return new AccountTransactionV1(
                        new TransactionSignaturesV1(
                                partiallySigned.getSenderSignature().get(),
                                sponsorSigner.sign(
                                        AccountTransactionV1.getDataToSign(
                                                partiallySigned.getHeader(),
                                                partiallySigned.getPayload()
                                        )
                                )
                        ),
                        partiallySigned.getHeader(),
                        partiallySigned.getPayload()
                );
            } catch (Exception e) {
                throw TransactionCreationException.from(e);
            }
        }
    }
}
