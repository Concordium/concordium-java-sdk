package com.concordium.sdk.examples.contractexample.cis2nft;

import com.concordium.sdk.transactions.ReceiveName;
import com.concordium.sdk.transactions.smartcontracts.Schema;
import com.concordium.sdk.transactions.smartcontracts.SchemaParameter;
import com.concordium.sdk.transactions.smartcontracts.SchemaVersion;
import com.concordium.sdk.types.*;
import lombok.SneakyThrows;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Cis2NftExample {

    @SneakyThrows
    public static void main(String[] args) {
        AccountAddress accountAddress = AccountAddress.from("3XSLuJcXg6xEua6iBPnWacc3iWh93yEDMCqX8FbE3RDSbEnT9P");
        ContractAddress contractAddress1 = ContractAddress.from(1, 0);
        ContractAddress contractAddress2 = ContractAddress.from(2, 0);
        Schema cis2nftSchema = Schema.from(Files.readAllBytes(Paths.get("./src/main/java/com/concordium/sdk/examples/contractexample/cis2nft/cis2-nft.schema.bin")), SchemaVersion.V3);
        String cis2nftContractName = "cis2_nft";

        // Initialize MintParams

        ReceiveName mintParamsReceiveName = ReceiveName.from(cis2nftContractName, "mint");
        List<TokenIdU32> tokens = new ArrayList<>();
        tokens.add(TokenIdU32.from(12));
        tokens.add(TokenIdU32.from(22));
        tokens.add(TokenIdU32.from(2132));
        SchemaParameter mintParameter = new MintParams(cis2nftSchema, mintParamsReceiveName, accountAddress, tokens);
        mintParameter.initialize(true);


        // Initialize TransferParameter
        ReceiveName nftTransferReceiveName = ReceiveName.from(cis2nftContractName, "transfer");
       TokenIdU32 tokenId = TokenIdU32.from(12);
        TokenAmountU8 amount = TokenAmountU8.from(1);
        AbstractAddress from = contractAddress1;
        Receiver to = new Receiver(contractAddress2, "mint");
        List<UInt8> data = new ArrayList<>();
        data.add(UInt8.from(123));
        data.add(UInt8.from(23));
        NftTransfer transfer = new NftTransfer(tokenId, amount, from, to, data);
        List<NftTransfer> transfers = new ArrayList<>();
        transfers.add(transfer);
        SchemaParameter transferParameter = new NftTransferParam(cis2nftSchema, nftTransferReceiveName, transfers);
        transferParameter.initialize(true);


        // Initialize UpdateOperatorParams

        ReceiveName updateOperatorReceiveName = ReceiveName.from(cis2nftContractName, "updateOperator");
        UpdateOperator update1 = new UpdateOperator(UpdateOperator.OperatorUpdate.ADD, accountAddress);
        UpdateOperator update2 = new UpdateOperator(UpdateOperator.OperatorUpdate.REMOVE, contractAddress1);
        List<UpdateOperator> updateOperatorList = new ArrayList<>();
        updateOperatorList.add(update1);
        updateOperatorList.add(update2);
        SchemaParameter updateOperatorsParams = new UpdateOperatorParams(cis2nftSchema, updateOperatorReceiveName, updateOperatorList);
        updateOperatorsParams.initialize(true);


        // Initialize OperatorOfQueryParams

        ReceiveName operatorOfReceiveName = ReceiveName.from(cis2nftContractName, "operatorOf");
        OperatorOfQuery operatorOfQuery1 = new OperatorOfQuery(accountAddress, contractAddress1);
        OperatorOfQuery operatorOfQuery2 = new OperatorOfQuery(contractAddress1, contractAddress2);
        List<OperatorOfQuery> operatorOfQueries = new ArrayList<>();
        operatorOfQueries.add(operatorOfQuery1);
        operatorOfQueries.add(operatorOfQuery2);
        SchemaParameter operatorOfQueryParams = new OperatorOfQueryParams(cis2nftSchema, operatorOfReceiveName, operatorOfQueries);
        operatorOfQueryParams.initialize(true);


        // Initialize ContractBalanceOfQueryParams

        ReceiveName balanceOfReceiveName = ReceiveName.from(cis2nftContractName, "balanceOf");
        NftBalanceOfQuery balanceOfQuery1 = new NftBalanceOfQuery(TokenIdU32.from(22222), accountAddress);
        NftBalanceOfQuery balanceOfQuery2 = new NftBalanceOfQuery(TokenIdU32.from(42), contractAddress1);
        List<NftBalanceOfQuery> balanceOfQueries = new ArrayList<>();
        balanceOfQueries.add(balanceOfQuery1);
        balanceOfQueries.add(balanceOfQuery2);
        SchemaParameter contractBalanceOfQueryParams = new NftBalanceOfQueryParams(cis2nftSchema, balanceOfReceiveName, balanceOfQueries);
        contractBalanceOfQueryParams.initialize(true);

        // Initialize ContractTokenMetadataQueryParams

        ReceiveName tokenMetadataReceiveName = ReceiveName.from(cis2nftContractName, "tokenMetadata");
        TokenIdU32 token1 = TokenIdU32.from(21);
        TokenIdU32 token2 = TokenIdU32.from(22);
        List<TokenIdU32> tokensForMetadataQuery = new ArrayList<>();
        tokensForMetadataQuery.add(token1);
        tokensForMetadataQuery.add(token2);
        SchemaParameter nftMetaDataQuery = new NftMetaDataQuery(cis2nftSchema, tokenMetadataReceiveName, tokensForMetadataQuery);
        nftMetaDataQuery.initialize(true);

        // Initialize SupportsQueryParams

        ReceiveName supportsReceiveName = ReceiveName.from(cis2nftContractName, "supports");
        String standardIdentifier1 = "identifier1";
        String standardIdentifier2 = "identifier2";
        List<String> identifiers = new ArrayList<>();
        identifiers.add(standardIdentifier1);
        identifiers.add(standardIdentifier2);
        SchemaParameter supportsQueryParams = new SupportsQueryParams(cis2nftSchema, supportsReceiveName, identifiers);
        supportsQueryParams.initialize(true);

        // Initialize SetImplementorsParams

        ReceiveName setImplementorsReceiveName = ReceiveName.from(cis2nftContractName, "setImplementors");
        List<ContractAddress> implementors = new ArrayList<>();
        String identifier = "IdentifierID";
        implementors.add(contractAddress1);
        implementors.add(contractAddress2);
        SchemaParameter setImplementorsParams = new SetImplementorsParams(cis2nftSchema, setImplementorsReceiveName, identifier, implementors);
        setImplementorsParams.initialize();

    }
}
