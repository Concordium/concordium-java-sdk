package com.concordium.sdk.examples;

import com.concordium.sdk.ClientV2;
import com.concordium.sdk.Connection;
import com.concordium.sdk.Credentials;
import com.concordium.sdk.exceptions.ClientInitializationException;
import com.concordium.sdk.requests.BlockHashInput;
import com.concordium.sdk.requests.BlockQuery;
import com.concordium.sdk.requests.getaccountinfo.AccountRequest;
import com.concordium.sdk.responses.accountinfo.AccountInfo;
import com.concordium.sdk.responses.modulelist.ModuleRef;
import com.concordium.sdk.transactions.AccountAddress;
import com.concordium.sdk.transactions.smartcontracts.WasmModule;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Callable;

/**
 * Gets the source of the module specified by {@link GetModuleSource#moduleRef} and prints it to the console.
 */
@Command(name = "GetModuleSource", mixinStandardHelpOptions = true)
public class GetModuleSource implements Callable<Integer> {
    @Option(
            names = {"--endpoint"},
            description = "GRPC interface of the node.",
            defaultValue = "http://localhost:20000")
    private String endpoint;

    @Option(
            names = {"--moduleRef"},
            description = "Module Reference",
            defaultValue = "247a7ac6efd2e46f72fd18741a6d1a0254ec14f95639df37079a576b2033873e")
    private String moduleRef;

    @Override
    public Integer call() throws ClientInitializationException, MalformedURLException {
        URL endpointUrl = new URL(this.endpoint);
        Connection connection = Connection.newBuilder()
                .host(endpointUrl.getHost())
                .port(endpointUrl.getPort())
                .credentials(new Credentials())
                .build();

        WasmModule moduleSource = ClientV2
                .from(connection)
                .getModuleSource(
                        BlockQuery.LAST_FINAL,
                        ModuleRef.from(moduleRef));

        System.out.println(moduleSource);

        return 0;
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new GetModuleSource()).execute(args);
        System.exit(exitCode);
    }
}
