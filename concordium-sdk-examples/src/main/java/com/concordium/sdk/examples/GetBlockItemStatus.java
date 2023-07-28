package com.concordium.sdk.examples;

import com.concordium.grpc.v2.BlockItemStatus;
import com.concordium.sdk.ClientV2;
import com.concordium.sdk.Connection;
import com.concordium.sdk.exceptions.BlockNotFoundException;
import com.concordium.sdk.exceptions.ClientInitializationException;
import com.concordium.sdk.responses.transactionstatus.TransactionStatus;
import com.concordium.sdk.transactions.Hash;
import lombok.val;
import lombok.var;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Callable;

@Command(name = "GetBlockItemStatus", mixinStandardHelpOptions = true)
public class GetBlockItemStatus implements Callable<Integer> {
    @Option(
            names = {"--endpoint"},
            description = "GRPC interface of the node.",
            defaultValue = "http://localhost:20001")
    private String endpoint;

    private final Hash blockTransactionHashFailure = Hash.from("d1bf95c1a2acc0947ec3900040c2ba172071aa759adf269c55ebb896aa6825c2");

    private final Hash blockTransactionHashSuccess = Hash.from("1ea074f0e12e18684f2d6bbf2039c6db32d2fd5c28e6ba74c8e92f36e88b1901");

    @Override
    public Integer call() throws MalformedURLException, ClientInitializationException, BlockNotFoundException {
        var endpointUrl = new URL(this.endpoint);

        Connection connection = Connection.newBuilder()
                .host(endpointUrl.getHost())
                .port(endpointUrl.getPort())
                .build();

        val client = ClientV2.from(connection);
        com.concordium.sdk.responses.blockitemstatus.BlockItemStatus getBlockItemStatusFailure = client
                .getBlockItemStatus(blockTransactionHashFailure);

        System.out.println(getBlockItemStatusFailure);

        com.concordium.sdk.responses.blockitemstatus.BlockItemStatus  getBlockItemStatusSuccess = client
                .getBlockItemStatus(blockTransactionHashSuccess);

        System.out.println(getBlockItemStatusSuccess);

        return 0;
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new GetBlockItemStatus()).execute(args);
        System.exit(exitCode);
    }
}
