package com.concordium.sdk.examples;

import com.concordium.sdk.ClientV2;
import com.concordium.sdk.Connection;
import com.concordium.sdk.Credentials;
import com.concordium.sdk.exceptions.ClientInitializationException;
import com.concordium.sdk.requests.BlockHashInput;
import com.concordium.sdk.requests.getaccountinfo.AccountRequest;
import com.concordium.sdk.responses.accountinfo.AccountInfo;
import com.concordium.sdk.transactions.AccountAddress;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Callable;

@Command(name = "GetAccountInfo", mixinStandardHelpOptions = true)
public class GetAccountInfo implements Callable<Integer> {
    @Option(
            names = {"--endpoint"},
            description = "GRPC interface of the node.",
            defaultValue = "http://localhost:20001")
    private String endpoint;

    @Override
    public Integer call() throws ClientInitializationException, MalformedURLException {
        URL endpointUrl = new URL(this.endpoint);
        Connection connection = Connection.newBuilder()
                .host(endpointUrl.getHost())
                .port(endpointUrl.getPort())
                .build();

        AccountInfo accountInfo = ClientV2
                .from(connection)
                .getAccountInfo(
                        BlockHashInput.BEST,
                        AccountRequest.from(AccountAddress.from("3bkTmK6GBWprhq6z2ukY6dEi1xNoBEMPDyyMQ6j8xrt8yaF7F2")));

        System.out.println(accountInfo);

        return 0;
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new GetAccountInfo()).execute(args);
        System.exit(exitCode);
    }
}
