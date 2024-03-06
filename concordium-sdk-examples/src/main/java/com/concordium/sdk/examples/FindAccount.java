package com.concordium.sdk.examples;

import com.concordium.sdk.ClientV2;
import com.concordium.sdk.Connection;
import com.concordium.sdk.crypto.ed25519.ED25519SecretKey;
import com.concordium.sdk.exceptions.ClientInitializationException;
import com.concordium.sdk.requests.AccountQuery;
import com.concordium.sdk.requests.BlockQuery;
import com.concordium.sdk.responses.FindAccountResponse;
import com.concordium.sdk.responses.blockinfo.BlockInfo;
import com.concordium.sdk.responses.blockitemstatus.FinalizedBlockItem;
import com.concordium.sdk.responses.blockitemsummary.Details;
import com.concordium.sdk.responses.blockitemsummary.Summary;
import com.concordium.sdk.responses.blockitemsummary.Type;
import com.concordium.sdk.transactions.*;
import com.concordium.sdk.types.AccountAddress;
import jdk.nashorn.internal.codegen.CompilerConstants;
import lombok.val;
import lombok.var;
import picocli.CommandLine;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.Optional;
import java.util.concurrent.Callable;

/**
 * Find out when a given account was created on the chain.
 * That is, the block in which the account creation transaction is committed.
 */
@CommandLine.Command(name = "FindAccount", mixinStandardHelpOptions = true)
public class FindAccount implements Callable<Integer> {

    @CommandLine.Option(
            names = {"--endpoint"},
            description = "GRPC interface of the node.",
            defaultValue = "http://localhost:20002")
    private String endpoint;

    @CommandLine.Option(
            names = {"--timeout"},
            description = "GRPC request timeout in milliseconds.",
            defaultValue = "100000")
    private int timeout;

    @CommandLine.Option(
            names = {"--a", "--account"},
            description = "Account to look for",
            defaultValue = "4AuT5RRmBwcdkLMA6iVjxTDb1FQmxwAh3wHBS22mggWL8xH6s3")
    private String account;

    @Override
    public Integer call() throws MalformedURLException, ClientInitializationException {
        URL endpointUrl = new URL(this.endpoint);
        Connection connection = Connection.newBuilder()
                .host(endpointUrl.getHost())
                .port(endpointUrl.getPort())
                .timeout(timeout)
                .build();
        ClientV2 client = ClientV2.from(connection);

        Optional<FindAccountResponse> response = client.findAccountCreation(AccountAddress.from(account));
        if (response.isEmpty()) {
            System.out.println("Account not found.");
            return 0;
        }
        Hash blockHash = response.get().getBlockHash();
        BlockQuery blockQuery = BlockQuery.HASH(blockHash);
        System.out.println("Account created in block: " + blockHash);
        BlockInfo blockInfo = client.getBlockInfo(blockQuery);
        System.out.println("Timestamp of the block: " + blockInfo.getBlockTime());
        Iterator<Summary> summaries = client.getBlockTransactionEvents(blockQuery);
        while (summaries.hasNext()) {
            Summary summary = summaries.next();
            Details details = summary.getDetails();
            if (details.getType() == Type.ACCOUNT_CREATION) {
                if (details.getAccountCreationDetails().getAddress().isAliasOf(AccountAddress.from(account))) {
                    System.out.println("Created by transaction hash: " + summary.getTransactionHash());
                    break;
                }
            }
        }

        return 0;
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new FindAccount()).execute(args);
        System.exit(exitCode);
    }
}
