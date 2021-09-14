package com.concordium.sdk;

import com.concordium.sdk.connection.Connection;
import com.concordium.sdk.transactions.*;
import com.concordium.sdk.responsetypes.accountinfo.AccountInfo;
import com.concordium.sdk.responsetypes.transactionstatus.TransactionStatus;
import com.google.protobuf.ByteString;
import concordium.ConcordiumP2PRpc;
import concordium.P2PGrpc;

import io.grpc.Deadline;
import io.grpc.ManagedChannelBuilder;
import lombok.val;

import java.util.concurrent.TimeUnit;

public final class Client {

    private final P2PGrpc.P2PBlockingStub blockingStub;

    public static Client from(Connection connection) {
        return new Client(connection);
    }

    private Client(Connection connection) {
        this(connection, ManagedChannelBuilder.forAddress(connection.getHost(), connection.getPort()).usePlaintext());
    }

    private Client(Connection connection, ManagedChannelBuilder<?> builder) {
        val deadline = Deadline.after(connection.getTimeout(), TimeUnit.MILLISECONDS);
        val channel = builder.build();
        this.blockingStub = P2PGrpc
                .newBlockingStub(channel)
                .withCallCredentials(connection.getCredentials())
                .withDeadline(deadline);
    }

    public AccountInfo getAccountInfo(Account address, BlockHash blockHash) {
        val request = ConcordiumP2PRpc.GetAddressInfoRequest
                .newBuilder()
                .setAddressBytes(accountAddressAsByteString(address))
                .setBlockHash(blockHash.getHash())
                .build();
        val accountInfo = blockingStub.getAccountInfo(request);
        return AccountInfo.fromJson(accountInfo.getValue());
    }

    private ByteString accountAddressAsByteString(Account address) {
        return ByteString.copyFrom(address.getAddress().getEncodedBytes());
    }

    public AccountNonce getAccountNonce(Account address) {
        val request = ConcordiumP2PRpc.AccountAddress
                .newBuilder()
                .setAccountAddressBytes(accountAddressAsByteString(address))
                .build();
        val nextAccountNonce = blockingStub.getNextAccountNonce(request);
        return AccountNonce.fromJson(nextAccountNonce.getValue());
    }

    public TransactionStatus getTransactionStatus(TransactionHash transactionHash) {
        val request = ConcordiumP2PRpc.TransactionHash
                .newBuilder()
                .setTransactionHash(transactionHash.getHash())
                .build();
        val transactionStatus = blockingStub.getTransactionStatus(request);
        return TransactionStatus.fromJson(transactionStatus.getValue());
    }

    //default network id
    public BlockItemHash sendSimpleTransfer(SimpleTransfer transfer) {
        return sendSimpleTransfer(transfer, 100);
    }

    public BlockItemHash sendSimpleTransfer(SimpleTransfer transfer, int networkId) {
        val blockItem = transfer.toBlockItem();
        val request = ConcordiumP2PRpc.SendTransactionRequest
                .newBuilder()
                .setNetworkId(networkId)
                .setPayload(ByteString.copyFrom(blockItem.getVersionedBytes()))
                .build();
        val response = blockingStub.sendTransaction(request);
        if (response.getValue()) {
            return blockItem.getHash();
        }
        throw new RuntimeException("Could not send SimpleTransfer!");
    }
}
