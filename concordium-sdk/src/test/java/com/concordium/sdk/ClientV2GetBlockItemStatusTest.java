package com.concordium.sdk;

import com.concordium.grpc.v2.*;
import com.concordium.sdk.responses.blockitemstatus.FinalizedBlockItem;
import com.concordium.sdk.responses.blockitemsummary.Details;
import com.concordium.sdk.responses.blockitemsummary.Summary;
import com.concordium.sdk.responses.blockitemsummary.Type;
import com.concordium.sdk.responses.transactionstatus.Status;
import com.concordium.sdk.responses.transactionstatus.TransactionResultEventType;
import com.concordium.sdk.responses.transactionstatus.TransferredResult;
import com.concordium.sdk.transactions.CCDAmount;
import com.concordium.sdk.transactions.Hash;
import com.concordium.sdk.types.AccountAddress;
import com.concordium.sdk.types.UInt64;
import io.grpc.ManagedChannel;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import io.grpc.stub.StreamObserver;
import io.grpc.testing.GrpcCleanupRule;
import lombok.var;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Optional;

import static com.concordium.sdk.ClientV2MapperExtensions.to;
import static com.concordium.sdk.ClientV2MapperExtensions.toTransactionHash;
import static org.junit.Assert.assertEquals;
import static org.mockito.AdditionalAnswers.delegatesTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ClientV2GetBlockItemStatusTest {

    private static final int TRANSACTION_INDEX = 5;
    private static final int ENERGY_COST = 500000;
    private static final String SENDER_ADDRESS = "48x2Uo8xCMMxwGuSQnwbqjzKtVqK5MaUud4vG7QEUgDmYkV85e";
    private static final String BLOCK_HASH = "d1bf95c1a2acc0947ec3900040c2ba172071aa759adf269c55ebb896aa6825c2";
    private static final String TRANSACTION_HASH = "1ea074f0e12e18684f2d6bbf2039c6db32d2fd5c28e6ba74c8e92f36e88b1901";
    private static final long TRANSACTION_COST = 123121;

    private static final long TRANSFERRED = TRANSACTION_COST - 42;

    private static final BlockItemStatus GRPC_BLOCK_ITEM_STATUS = BlockItemStatus.newBuilder()
            .setFinalized(
                    BlockItemStatus.Finalized.newBuilder()
                            .setOutcome(
                                    BlockItemSummaryInBlock.newBuilder()
                                            .setBlockHash(to(Hash.from(
                                                    BLOCK_HASH)))
                                            .setOutcome(
                                                    BlockItemSummary.newBuilder()
                                                            .setIndex(
                                                                    BlockItemSummary.TransactionIndex
                                                                            .newBuilder()
                                                                            .setValue(TRANSACTION_INDEX))
                                                            .setEnergyCost(
                                                                    Energy.newBuilder()
                                                                            .setValue(ENERGY_COST))
                                                            .setHash(
                                                                    toTransactionHash(
                                                                            Hash.from(TRANSACTION_HASH)))
                                                            .setAccountTransaction(
                                                                    AccountTransactionDetails
                                                                            .newBuilder()
                                                                            .setCost(Amount.newBuilder()
                                                                                    .setValue(TRANSACTION_COST))
                                                                            .setSender(
                                                                                    to(AccountAddress
                                                                                            .from(SENDER_ADDRESS)))
                                                                            .setEffects(
                                                                                    AccountTransactionEffects
                                                                                            .newBuilder()
                                                                                            .setAccountTransfer(
                                                                                                    AccountTransactionEffects.AccountTransfer
                                                                                                            .newBuilder()
                                                                                                            .setReceiver(to(AccountAddress.from(SENDER_ADDRESS)))
                                                                                                            .setAmount(to(CCDAmount.fromMicro(TRANSFERRED)))
                                                                                                            .build())))

                                            )

                            )).build();

    private static final Summary summary = Summary.builder()
            .transactionIndex(UInt64.from(TRANSACTION_INDEX))
            .energyCost(com.concordium.sdk.requests.smartcontracts.Energy.from(UInt64.from(ENERGY_COST)))
            .transactionHash(Hash.from(TRANSACTION_HASH))
            .details(Details
                    .builder()
                    .type(Type.ACCOUNT_TRANSACTION)
                    .accountTransactionDetails(com.concordium.sdk.responses.blockitemsummary.AccountTransactionDetails
                            .builder()
                            .successful(true)
                            .type(TransactionResultEventType.TRANSFERRED)
                            .sender(AccountAddress.from(SENDER_ADDRESS))
                            .cost(CCDAmount.fromMicro(TRANSACTION_COST))
                            .accountTransfer(TransferredResult
                                    .builder()
                                    .to(AccountAddress.from(SENDER_ADDRESS))
                                    .amount(CCDAmount.fromMicro(TRANSFERRED))
                                    .from(AccountAddress.from(SENDER_ADDRESS))
                                    .build())
                            .build())
                    .build()).build();
    private static final com.concordium.sdk.responses.blockitemstatus.BlockItemStatus FINALIZED_TRANSACTION_STATUS = com.concordium.sdk.responses.blockitemstatus.BlockItemStatus.builder()
            .status(Status.FINALIZED)
            .finalizedBlockItem(FinalizedBlockItem
                    .builder()
                    .blockHash(Hash.from(BLOCK_HASH))
                    .summary(summary)
                    .build())
            .build();

    private static final QueriesGrpc.QueriesImplBase serviceImpl = mock(QueriesGrpc.QueriesImplBase.class,
            delegatesTo(
                    new QueriesGrpc.QueriesImplBase() {
                        @Override
                        public void getBlockItemStatus(
                                TransactionHash request,
                                StreamObserver<BlockItemStatus> responseObserver) {
                            responseObserver.onNext(GRPC_BLOCK_ITEM_STATUS);
                            responseObserver.onCompleted();
                        }
                    }));
    @Rule
    public final GrpcCleanupRule grpcCleanup = new GrpcCleanupRule();

    private ClientV2 client;

    @Before
    public void setUp() throws Exception {
        String serverName = InProcessServerBuilder.generateName();
        grpcCleanup.register(InProcessServerBuilder
                .forName(serverName).directExecutor().addService(serviceImpl).build().start());
        ManagedChannel channel = grpcCleanup.register(
                InProcessChannelBuilder.forName(serverName).directExecutor().build());
        client = new ClientV2(10000, channel, Optional.empty());
    }

    @Test
    public void getBlockItemStatus() {
        var res = client.getBlockItemStatus(
                Hash.from("1ea074f0e12e18684f2d6bbf2039c6db32d2fd5c28e6ba74c8e92f36e88b"));

        verify(serviceImpl).getBlockItemStatus(any(TransactionHash.class), any(StreamObserver.class));
        assertEquals(FINALIZED_TRANSACTION_STATUS.toString(), res.toString());
    }
}
