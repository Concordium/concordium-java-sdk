package com.concordium.sdk.examples;

import com.concordium.sdk.Client;
import com.concordium.sdk.Connection;
import com.concordium.sdk.Credentials;
import com.concordium.sdk.exceptions.AccountNotFoundException;
import com.concordium.sdk.exceptions.ClientInitializationException;
import com.concordium.sdk.responses.accountinfo.AccountInfo;
import com.concordium.sdk.transactions.AccountAddress;
import com.concordium.sdk.transactions.Hash;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Callable;

@Command(name = "GetAnonymityRevokers", mixinStandardHelpOptions = true)
public class GetAccountInfoGrpcV1 implements Callable<Integer> {
    @Option(
            names = {"--endpoint"},
            description = "GRPC interface of the node.",
            defaultValue = "http://localhost:10001")
    private String endpoint;

    @Override
    public Integer call() throws ClientInitializationException, MalformedURLException, AccountNotFoundException {
        URL endpointUrl = new URL(this.endpoint);
        Connection connection = Connection.builder()
                .host(endpointUrl.getHost())
                .port(endpointUrl.getPort())
                .credentials(Credentials.from("rpcadmin"))
                .build();

        Client client = Client.from(connection);

        Hash blockHash = client.getConsensusStatus().getBestBlock();
        AccountInfo accountInfo = client
                .getAccountInfo(
                        com.concordium.sdk.requests.getaccountinfo.AccountRequest.from(
                                AccountAddress.from("37UHs4b9VH3F366cdmrA4poBURzzARJLWxdXZ18zoa9pnfhhDf")),
                        blockHash);

        System.out.println(accountInfo);

        return 0;
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new GetAccountInfoGrpcV1()).execute(args);
        System.exit(exitCode);
    }
}
