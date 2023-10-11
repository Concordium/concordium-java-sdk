package com.concordium.sdk.examples.contractexample.cis2nft;

import com.concordium.sdk.examples.contractexample.parameters.*;
import com.concordium.sdk.transactions.ReceiveName;
import com.concordium.sdk.transactions.smartcontracts.Schema;
import com.concordium.sdk.transactions.smartcontracts.SchemaParameter;
import com.concordium.sdk.transactions.smartcontracts.SchemaVersion;
import com.concordium.sdk.types.*;
import lombok.SneakyThrows;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for creating and initializing parameters being used in {@link Cis2Nft}.
 */
public class Cis2NftParameters {

    private static final AccountAddress ACCOUNT_ADDRESS = AccountAddress.from("3XSLuJcXg6xEua6iBPnWacc3iWh93yEDMCqX8FbE3RDSbEnT9P");
    private static  final ContractAddress CONTRACT_ADDRESS_1 = ContractAddress.from(1, 0);
    private static final ContractAddress CONTRACT_ADDRESS_2 = ContractAddress.from(2, 0);

    private static final String CIS_2_NFT_CONTRACT_NAME = "cis2_nft";

    @SneakyThrows
    public static SchemaParameter generateMintParams() {
        Schema schema = Schema.from(Files.readAllBytes(Paths.get("./src/main/java/com/concordium/sdk/examples/contractexample/cis2nft/cis2-nft.schema.bin")), SchemaVersion.V3);
        ReceiveName mintParamsReceiveName = ReceiveName.from(CIS_2_NFT_CONTRACT_NAME, "mint");
        List<TokenIdU32> tokens = new ArrayList<>();
        tokens.add(TokenIdU32.from(2));
        tokens.add(TokenIdU32.from(22));
        tokens.add(TokenIdU32.from(2132));
        SchemaParameter mintParameter = new MintParams(schema, mintParamsReceiveName, ACCOUNT_ADDRESS, tokens);
        mintParameter.initialize(true);
        return mintParameter;
    }

    @SneakyThrows
    public static SchemaParameter generateTransferParams() {
        Schema schema = Schema.from(Files.readAllBytes(Paths.get("./src/main/java/com/concordium/sdk/examples/contractexample/cis2nft/cis2-nft.schema.bin")), SchemaVersion.V3);
        ReceiveName nftTransferReceiveName = ReceiveName.from(CIS_2_NFT_CONTRACT_NAME, "transfer");
        TokenIdU32 tokenId = TokenIdU32.from(12);
        TokenAmountU8 amount = TokenAmountU8.from(1);
        AbstractAddress from = CONTRACT_ADDRESS_1;
        Receiver to = new Receiver(CONTRACT_ADDRESS_2, "mint");
        List<UInt8> data = new ArrayList<>();
        data.add(UInt8.from(123));
        data.add(UInt8.from(23));
        NftTransfer transfer = new NftTransfer(tokenId, amount, from, to, data);
        List<NftTransfer> transfers = new ArrayList<>();
        transfers.add(transfer);
        SchemaParameter transferParameter = new NftTransferParam(schema, nftTransferReceiveName, transfers);
        transferParameter.initialize(true);

        return transferParameter;
    }

    @SneakyThrows
    public static SchemaParameter generateUpdateOperatorParams() {
        Schema schema = Schema.from(Files.readAllBytes(Paths.get("./src/main/java/com/concordium/sdk/examples/contractexample/cis2nft/cis2-nft.schema.bin")), SchemaVersion.V3);
        ReceiveName updateOperatorReceiveName = ReceiveName.from(CIS_2_NFT_CONTRACT_NAME, "updateOperator");
        UpdateOperator update1 = new UpdateOperator(UpdateOperator.OperatorUpdate.ADD, ACCOUNT_ADDRESS);
        UpdateOperator update2 = new UpdateOperator(UpdateOperator.OperatorUpdate.REMOVE, CONTRACT_ADDRESS_1);
        List<UpdateOperator> updateOperatorList = new ArrayList<>();
        updateOperatorList.add(update1);
        updateOperatorList.add(update2);
        SchemaParameter updateOperatorsParams = new UpdateOperatorParams(schema, updateOperatorReceiveName, updateOperatorList);
        updateOperatorsParams.initialize(true);

        return updateOperatorsParams;
    }

    public static SchemaParameter generateOperatorOfParams() throws IOException {
        Schema schema = Schema.from(Files.readAllBytes(Paths.get("./src/main/java/com/concordium/sdk/examples/contractexample/cis2nft/cis2-nft.schema.bin")), SchemaVersion.V3);
        ReceiveName operatorOfReceiveName = ReceiveName.from(CIS_2_NFT_CONTRACT_NAME, "operatorOf");
        OperatorOfQuery operatorOfQuery1 = new OperatorOfQuery(ACCOUNT_ADDRESS, CONTRACT_ADDRESS_1);
        OperatorOfQuery operatorOfQuery2 = new OperatorOfQuery(CONTRACT_ADDRESS_1, CONTRACT_ADDRESS_2);
        List<OperatorOfQuery> operatorOfQueries = new ArrayList<>();
        operatorOfQueries.add(operatorOfQuery1);
        operatorOfQueries.add(operatorOfQuery2);
        SchemaParameter operatorOfQueryParams = new OperatorOfQueryParams(schema, operatorOfReceiveName, operatorOfQueries);
        operatorOfQueryParams.initialize(true);
        return operatorOfQueryParams;
    }

    public static SchemaParameter generateBalanceOfParams() throws IOException {
        Schema schema = Schema.from(Files.readAllBytes(Paths.get("./src/main/java/com/concordium/sdk/examples/contractexample/cis2nft/cis2-nft.schema.bin")), SchemaVersion.V3);
        ReceiveName balanceOfReceiveName = ReceiveName.from(CIS_2_NFT_CONTRACT_NAME, "balanceOf");
        NftBalanceOfQuery balanceOfQuery1 = new NftBalanceOfQuery(TokenIdU32.from(22222), ACCOUNT_ADDRESS);
        NftBalanceOfQuery balanceOfQuery2 = new NftBalanceOfQuery(TokenIdU32.from(42), CONTRACT_ADDRESS_1);
        List<NftBalanceOfQuery> balanceOfQueries = new ArrayList<>();
        balanceOfQueries.add(balanceOfQuery1);
        balanceOfQueries.add(balanceOfQuery2);
        SchemaParameter contractBalanceOfQueryParams = new NftBalanceOfQueryParams(schema, balanceOfReceiveName, balanceOfQueries);
        contractBalanceOfQueryParams.initialize(true);
        return contractBalanceOfQueryParams;
    }

    public static SchemaParameter generateTokenMetadataParams() throws IOException {
        Schema schema = Schema.from(Files.readAllBytes(Paths.get("./src/main/java/com/concordium/sdk/examples/contractexample/cis2nft/cis2-nft.schema.bin")), SchemaVersion.V3);
        ReceiveName tokenMetadataReceiveName = ReceiveName.from(CIS_2_NFT_CONTRACT_NAME, "tokenMetadata");
        TokenIdU32 token1 = TokenIdU32.from(21);
        TokenIdU32 token2 = TokenIdU32.from(22);
        List<TokenIdU32> tokensForMetadataQuery = new ArrayList<>();
        tokensForMetadataQuery.add(token1);
        tokensForMetadataQuery.add(token2);
        SchemaParameter nftMetaDataQuery = new NftMetaDataQuery(schema, tokenMetadataReceiveName, tokensForMetadataQuery);
        nftMetaDataQuery.initialize(true);
        return nftMetaDataQuery;
    }

    public static SchemaParameter generateSupportsParameter() throws IOException {
        Schema schema = Schema.from(Files.readAllBytes(Paths.get("./src/main/java/com/concordium/sdk/examples/contractexample/cis2nft/cis2-nft.schema.bin")), SchemaVersion.V3);
        ReceiveName supportsReceiveName = ReceiveName.from(CIS_2_NFT_CONTRACT_NAME, "supports");
        String standardIdentifier1 = "identifier1";
        String standardIdentifier2 = "identifier2";
        List<String> identifiers = new ArrayList<>();
        identifiers.add(standardIdentifier1);
        identifiers.add(standardIdentifier2);
        SchemaParameter supportsQueryParams = new SupportsQueryParams(schema, supportsReceiveName, identifiers);
        supportsQueryParams.initialize(true);
        return supportsQueryParams;
    }

    public static SchemaParameter generateSetImplementorsParams() throws IOException {
        Schema schema = Schema.from(Files.readAllBytes(Paths.get("./src/main/java/com/concordium/sdk/examples/contractexample/cis2nft/cis2-nft.schema.bin")), SchemaVersion.V3);
        ReceiveName setImplementorsReceiveName = ReceiveName.from(CIS_2_NFT_CONTRACT_NAME, "setImplementors");
        List<ContractAddress> implementors = new ArrayList<>();
        String identifier = "IdentifierID";
        implementors.add(CONTRACT_ADDRESS_1);
        implementors.add(CONTRACT_ADDRESS_2);
        SchemaParameter setImplementorsParams = new SetImplementorsParams(schema, setImplementorsReceiveName, identifier, implementors);
        setImplementorsParams.initialize();
        return setImplementorsParams;
    }
    @SneakyThrows
    public static void main(String[] args) {
        Schema schema = Schema.from(Files.readAllBytes(Paths.get("./src/main/java/com/concordium/sdk/examples/contractexample/cis2nft/cis2-nft.schema.bin")), SchemaVersion.V3);
        ReceiveName setImplementorsReceiveName = ReceiveName.from(CIS_2_NFT_CONTRACT_NAME, "setImplementors");
        List<ContractAddress> implementors = new ArrayList<>();
        String identifier = "IdentifierID";
        implementors.add(CONTRACT_ADDRESS_1);
        implementors.add(CONTRACT_ADDRESS_2);
        SchemaParameter setImplementorsParams = new SetImplementorsParams(schema, setImplementorsReceiveName, identifier, implementors);
        setImplementorsParams.initialize();

    }



}
