package com.concordium.sdk.examples;

import com.concordium.sdk.ClientV2;
import com.concordium.sdk.Connection;
import com.concordium.sdk.Credentials;
import com.concordium.sdk.exceptions.ClientInitializationException;
import com.concordium.sdk.requests.BlockHashInput;
import com.concordium.sdk.types.ContractAddress;
import lombok.var;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.concurrent.Callable;

@Command(name = "InstanceStateLookup", mixinStandardHelpOptions = true)
public class InstanceStateLookup implements Callable<Integer> {
    @Option(
            names = {"--endpoint"},
            description = "GRPC interface of the node.",
            defaultValue = "http://localhost:20000")
    private String endpoint;

    @Option(
            names = {"--timeout"},
            description = "GRPC request timeout in milliseconds.",
            defaultValue = "100000")
    private int timeout;

    @Option(
            names = {"--index"},
            description = "Contract Address Index.",
            defaultValue = "4341")
    private long index;

    @Option(
            names = {"--subindex"},
            description = "Contract Address Sub-index.",
            defaultValue = "0")
    private long subindex;

    @Option(
            names = {"--keyHex"},
            description = "Lookup Key in Hex.",
            defaultValue = "")
    private String keyHex;

    @Override
    public Integer call() throws MalformedURLException, ClientInitializationException, DecoderException {
        var endpointUrl = new URL(this.endpoint);

        Connection connection = Connection.builder()
                .host(endpointUrl.getHost())
                .port(endpointUrl.getPort())
                .credentials(new Credentials())
                .build();

        var value = ClientV2
                .from(connection)
                .instanceStateLookup(
                        BlockHashInput.LAST_FINAL,
                        ContractAddress.from(index, subindex),
                        Hex.decodeHex(keyHex));

        System.out.println(Arrays.toString(value));

        return 0;
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new InstanceStateLookup()).execute(args);
        System.exit(exitCode);
    }
}
