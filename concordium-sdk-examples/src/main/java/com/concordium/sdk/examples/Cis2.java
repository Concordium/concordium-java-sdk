package com.concordium.sdk.examples;

import com.concordium.sdk.ClientV2;
import com.concordium.sdk.Connection;
import com.concordium.sdk.cis2.BalanceQuery;
import com.concordium.sdk.cis2.Cis2Client;
import com.concordium.sdk.cis2.TokenAmount;
import com.concordium.sdk.cis2.TokenId;
import com.concordium.sdk.exceptions.ClientInitializationException;
import com.concordium.sdk.requests.BlockQuery;
import com.concordium.sdk.transactions.Hash;
import com.concordium.sdk.types.AbstractAddress;
import com.concordium.sdk.types.AccountAddress;
import com.concordium.sdk.types.ContractAddress;
import lombok.val;
import picocli.CommandLine;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Callable;

/**
 * Example usage of the CIS2 client
 */
@CommandLine.Command(name = "Cis2", mixinStandardHelpOptions = true)
public class Cis2 implements Callable<Integer> {

    @CommandLine.Option(
            names = {"--endpoint"},
            description = "GRPC interface of the node.",
            defaultValue = "http://localhost:20000")
    private String endpoint;

    @CommandLine.Option(
            names = {"--index"},
            description = "Index of the contract.",
            defaultValue = "9390")
    private long contractIndex;

    @Override
    public Integer call() throws ClientInitializationException, MalformedURLException {
        URL endpointUrl = new URL(this.endpoint);
        val client = Cis2Client.newClient(ClientV2.from(Connection.newBuilder()
                .host(endpointUrl.getHost())
                .port(endpointUrl.getPort())
                .build()), ContractAddress.from(contractIndex, 0));

        val eventsForBlock = client.getEventsFor(BlockQuery.HASH(Hash.from("cfd9a3de1b7de2d2942f80b102135bcc8553f472c53c6c8074110aba38bca43c")));
        while (eventsForBlock.hasNext()) {
            System.out.println(eventsForBlock.next());
        }

        val balances = client.balanceOf(new BalanceQuery(TokenId.min(), AccountAddress.from("3rXssmPErqhHvDMByFLCEydYAJwot7ZkL7xcu8y296iMJxwNGC")));
        for (BalanceQuery balanceQuery : balances.keySet()) {
            TokenId tokenId = balanceQuery.getTokenId();
            AbstractAddress owner = balanceQuery.getAddress();
            TokenAmount balance = balances.get(balanceQuery);
            System.out.println("TokenId: " + tokenId + " Owner: " + owner + " Balance " + balance);
        }

        return 0;
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new Cis2()).execute(args);
        System.exit(exitCode);
    }
}
