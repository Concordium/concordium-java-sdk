package com.concordium.sdk.examples.contractexample.cis2nft;

import com.concordium.sdk.examples.contractexample.parameters.*;
import com.concordium.sdk.transactions.ReceiveName;
import com.concordium.sdk.transactions.smartcontracts.Schema;
import com.concordium.sdk.transactions.smartcontracts.SchemaParameter;
import com.concordium.sdk.types.*;
import lombok.SneakyThrows;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for creating and initializing parameters being used in {@link Cis2Nft} representing a <a href="https://github.com/Concordium/concordium-rust-smart-contracts/blob/main/examples/cis2-nft/src/lib.rs">cis2-nft contract</a>.
 * All values are dummy values and should be replaced to get valid results.
 */
public class Cis2NftParameters {
    private static final AccountAddress ACCOUNT_ADDRESS = AccountAddress.from("3XSLuJcXg6xEua6iBPnWacc3iWh93yEDMCqX8FbE3RDSbEnT9P");
    private static final ContractAddress CONTRACT_ADDRESS_1 = ContractAddress.from(1, 0);
    private static final ContractAddress CONTRACT_ADDRESS_2 = ContractAddress.from(2, 0);
    private static final String CIS_2_NFT_CONTRACT_NAME = "cis2_nft";

    private static final String SCHEMA_PATH = "./src/main/java/com/concordium/sdk/examples/contractexample/cis2nft/cis2-nft.schema.bin";

    private static final Schema SCHEMA;

    static {
        try {
            SCHEMA = Schema.from(Files.readAllBytes(Paths.get(SCHEMA_PATH)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Generates and initializes {@link MintParams} for the 'mint' method of a cis2-nft contract.
     *
     * @return initialized {@link MintParams}.
     */
    @SneakyThrows
    public static SchemaParameter generateMintParams() {
        ReceiveName mintParamsReceiveName = ReceiveName.from(CIS_2_NFT_CONTRACT_NAME, "mint");
        List<TokenIdU32> tokens = new ArrayList<>();
        tokens.add(TokenIdU32.from(2));
        tokens.add(TokenIdU32.from(22));
        tokens.add(TokenIdU32.from(2132));
        SchemaParameter mintParameter = new MintParams(SCHEMA, mintParamsReceiveName, ACCOUNT_ADDRESS, tokens);
        mintParameter.initialize(true);
        return mintParameter;
    }

    /**
     * Generates and initializes {@link NftTransferParam} for the 'transfer' method of a cis2-nft contract.
     *
     * @return initialized {@link NftTransferParam}.
     */
    @SneakyThrows
    public static SchemaParameter generateTransferParams() {
        ReceiveName nftTransferReceiveName = ReceiveName.from(CIS_2_NFT_CONTRACT_NAME, "transfer");
        TokenIdU32 tokenId = TokenIdU32.from(12);
        TokenAmountU8 amount = TokenAmountU8.from(1);
        AbstractAddress from = CONTRACT_ADDRESS_1;
        Receiver to = new Receiver(CONTRACT_ADDRESS_2, "mint");
        byte[] data = new byte[]{123, -23};
        NftTransfer transfer = new NftTransfer(tokenId, amount, from, to, data);
        List<NftTransfer> transfers = new ArrayList<>();
        transfers.add(transfer);
        SchemaParameter transferParameter = new NftTransferParam(SCHEMA, nftTransferReceiveName, transfers);
        transferParameter.initialize(true);
        return transferParameter;
    }

    /**
     * Generates and initializes {@link UpdateOperatorParams} for the 'updateOperator' method of a cis2-nft contract.
     *
     * @return initialized {@link UpdateOperatorParams}.
     */
    @SneakyThrows
    public static SchemaParameter generateUpdateOperatorParams() {
        ReceiveName updateOperatorReceiveName = ReceiveName.from(CIS_2_NFT_CONTRACT_NAME, "updateOperator");
        UpdateOperator update1 = new UpdateOperator(UpdateOperator.OperatorUpdate.ADD, ACCOUNT_ADDRESS);
        UpdateOperator update2 = new UpdateOperator(UpdateOperator.OperatorUpdate.REMOVE, CONTRACT_ADDRESS_1);
        List<UpdateOperator> updateOperatorList = new ArrayList<>();
        updateOperatorList.add(update1);
        updateOperatorList.add(update2);
        SchemaParameter updateOperatorsParams = new UpdateOperatorParams(SCHEMA, updateOperatorReceiveName, updateOperatorList);
        updateOperatorsParams.initialize(true);
        return updateOperatorsParams;
    }

    /**
     * Generates and initializes {@link OperatorOfQueryParams} for the 'operatorOf' method of a cis2-nft contract.
     *
     * @return initialized {@link OperatorOfQueryParams}.
     */
    @SneakyThrows
    public static SchemaParameter generateOperatorOfParams() {
        ReceiveName operatorOfReceiveName = ReceiveName.from(CIS_2_NFT_CONTRACT_NAME, "operatorOf");
        OperatorOfQuery operatorOfQuery1 = new OperatorOfQuery(ACCOUNT_ADDRESS, CONTRACT_ADDRESS_1);
        OperatorOfQuery operatorOfQuery2 = new OperatorOfQuery(CONTRACT_ADDRESS_1, CONTRACT_ADDRESS_2);
        List<OperatorOfQuery> operatorOfQueries = new ArrayList<>();
        operatorOfQueries.add(operatorOfQuery1);
        operatorOfQueries.add(operatorOfQuery2);
        SchemaParameter operatorOfQueryParams = new OperatorOfQueryParams(SCHEMA, operatorOfReceiveName, operatorOfQueries);
        operatorOfQueryParams.initialize(true);
        return operatorOfQueryParams;
    }

    /**
     * Generates and initializes {@link NftBalanceOfQueryParams} for the 'balanceOf' method of a cis2-nft contract.
     *
     * @return initialized {@link NftBalanceOfQueryParams}.
     */
    @SneakyThrows
    public static SchemaParameter generateBalanceOfParams() {
        ReceiveName balanceOfReceiveName = ReceiveName.from(CIS_2_NFT_CONTRACT_NAME, "balanceOf");
        NftBalanceOfQuery balanceOfQuery1 = new NftBalanceOfQuery(TokenIdU32.from(22222), ACCOUNT_ADDRESS);
        NftBalanceOfQuery balanceOfQuery2 = new NftBalanceOfQuery(TokenIdU32.from(42), CONTRACT_ADDRESS_1);
        List<NftBalanceOfQuery> balanceOfQueries = new ArrayList<>();
        balanceOfQueries.add(balanceOfQuery1);
        balanceOfQueries.add(balanceOfQuery2);
        SchemaParameter contractBalanceOfQueryParams = new NftBalanceOfQueryParams(SCHEMA, balanceOfReceiveName, balanceOfQueries);
        contractBalanceOfQueryParams.initialize(true);
        return contractBalanceOfQueryParams;
    }

    /**
     * Generates and initializes {@link NftTokenMetaDataQueryParams} for the 'tokenMetadata' method of a cis2-nft contract.
     *
     * @return initialized {@link NftTokenMetaDataQueryParams}.
     */
    @SneakyThrows
    public static SchemaParameter generateTokenMetadataParams() {
        ReceiveName tokenMetadataReceiveName = ReceiveName.from(CIS_2_NFT_CONTRACT_NAME, "tokenMetadata");
        TokenIdU32 token1 = TokenIdU32.from(21);
        TokenIdU32 token2 = TokenIdU32.from(22);
        List<TokenIdU32> tokensForMetadataQuery = new ArrayList<>();
        tokensForMetadataQuery.add(token1);
        tokensForMetadataQuery.add(token2);
        SchemaParameter nftMetaDataQuery = new NftTokenMetaDataQueryParams(SCHEMA, tokenMetadataReceiveName, tokensForMetadataQuery);
        nftMetaDataQuery.initialize(true);
        return nftMetaDataQuery;
    }

    /**
     * Generates and initializes {@link SupportsQueryParams} for the 'supports' method of a cis2-nft contract.
     *
     * @return initialized {@link SupportsQueryParams}.
     */
    @SneakyThrows
    public static SchemaParameter generateSupportsParameter() {
        ReceiveName supportsReceiveName = ReceiveName.from(CIS_2_NFT_CONTRACT_NAME, "supports");
        String standardIdentifier1 = "identifier1";
        String standardIdentifier2 = "identifier2";
        List<String> identifiers = new ArrayList<>();
        identifiers.add(standardIdentifier1);
        identifiers.add(standardIdentifier2);
        SchemaParameter supportsQueryParams = new SupportsQueryParams(SCHEMA, supportsReceiveName, identifiers);
        supportsQueryParams.initialize(true);
        return supportsQueryParams;
    }

    /**
     * Generates and initializes {@link SetImplementorsParams} for the 'setImplementors' method of a cis2-nft contract.
     *
     * @return initialized {@link SetImplementorsParams}.
     */
    @SneakyThrows
    public static SchemaParameter generateSetImplementorsParams() {
        ReceiveName setImplementorsReceiveName = ReceiveName.from(CIS_2_NFT_CONTRACT_NAME, "setImplementors");
        List<ContractAddress> implementors = new ArrayList<>();
        String identifier = "IdentifierID";
        implementors.add(CONTRACT_ADDRESS_1);
        implementors.add(CONTRACT_ADDRESS_2);
        SchemaParameter setImplementorsParams = new SetImplementorsParams(SCHEMA, setImplementorsReceiveName, identifier, implementors);
        setImplementorsParams.initialize();
        return setImplementorsParams;
    }

}
