package com.concordium.sdk.examples.contractexample.wccd;

import com.concordium.sdk.examples.contractexample.parameters.*;
import com.concordium.sdk.responses.modulelist.ModuleRef;
import com.concordium.sdk.serializing.JsonMapper;
import com.concordium.sdk.transactions.Hash;
import com.concordium.sdk.transactions.ReceiveName;
import com.concordium.sdk.transactions.smartcontracts.Schema;
import com.concordium.sdk.transactions.smartcontracts.SchemaParameter;
import com.concordium.sdk.transactions.smartcontracts.SchemaVersion;
import com.concordium.sdk.transactions.smartcontracts.parameters.AddressParam;
import com.concordium.sdk.types.AbstractAddress;
import com.concordium.sdk.types.AccountAddress;
import com.concordium.sdk.types.ContractAddress;
import com.concordium.sdk.types.UInt8;
import lombok.SneakyThrows;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Helper class for creating and initializing parameters being used in {@link Cis2WCCD} representing a <a href="https://github.com/Concordium/concordium-rust-smart-contracts/blob/main/examples/cis2-wccd/src/lib.rs">cis2-wCCD contract</a>.
 * All values are dummy values and should be replaced to get valid results.
 */
public class Cis2WCCDParameters {
    private static final AccountAddress ACCOUNT_ADDRESS = AccountAddress.from("3XSLuJcXg6xEua6iBPnWacc3iWh93yEDMCqX8FbE3RDSbEnT9P");
    private static final ContractAddress CONTRACT_ADDRESS_1 = ContractAddress.from(1, 0);
    private static final ContractAddress CONTRACT_ADDRESS_2 = ContractAddress.from(2, 0);
    private static final String CONTRACT_NAME = "cis2_wCCD";

    private static final Path SCHEMA_PATH = Paths.get("./src/main/java/com/concordium/sdk/examples/contractexample/wccd/cis2-wccd.schema.bin");
    @SneakyThrows
    public static SchemaParameter generateWrapParams() {
        Schema cis2wccdSchema = Schema.from(Files.readAllBytes(SCHEMA_PATH), SchemaVersion.V3);

        // Initialize WrapParams

        ReceiveName wrapReceiveName = ReceiveName.from(CONTRACT_NAME, "wrap");
        Receiver wrapReceiver = new Receiver(ACCOUNT_ADDRESS);
        List<UInt8> wrapData = new ArrayList<>();
        wrapData.add(UInt8.from(1));
        wrapData.add(UInt8.from(42));
        WrapParams wrapParams = new WrapParams(cis2wccdSchema, wrapReceiveName, wrapReceiver, wrapData);
        wrapParams.initialize(true);
        return wrapParams;
    }

    @SneakyThrows
    public static SchemaParameter generateUnwrapParams() {
        Schema cis2wccdSchema = Schema.from(Files.readAllBytes(SCHEMA_PATH), SchemaVersion.V3);
        // Initialize UnwrapParams

        ReceiveName unwrapReceiveName = ReceiveName.from(CONTRACT_NAME, "unwrap");
        String unwrapAmount = "2"; // TokenAmountU64
        Receiver unwrapReceiver = new Receiver(CONTRACT_ADDRESS_2, "test");
        List<UInt8> unwrapData = new ArrayList<>();
        unwrapData.add(UInt8.from(1));
        unwrapData.add(UInt8.from(42));
        UnwrapParams unwrapParams = new UnwrapParams(cis2wccdSchema, unwrapReceiveName, unwrapAmount, ACCOUNT_ADDRESS, unwrapReceiver, unwrapData);
        unwrapParams.initialize(true);
        return unwrapParams;
    }

    @SneakyThrows
    public static SchemaParameter generateUpdateAdminParams() {
        Schema cis2wccdSchema = Schema.from(Files.readAllBytes(SCHEMA_PATH), SchemaVersion.V3);

        // Initialize Address as UpdateAdminParam
        ReceiveName updateAdmninReceiveName = ReceiveName.from(CONTRACT_NAME, "updateAdmin");
        AddressParam updateAdminParam = new AddressParam(cis2wccdSchema, updateAdmninReceiveName, CONTRACT_ADDRESS_1);
        updateAdminParam.initialize(true);
        return updateAdminParam;
    }

    @SneakyThrows
    public static SchemaParameter generateSetPausedParams() {
        Schema cis2wccdSchema = Schema.from(Files.readAllBytes(SCHEMA_PATH), SchemaVersion.V3);

        // Initialize SetPausedParams
        ReceiveName setPausedReceiveName = ReceiveName.from(CONTRACT_NAME, "setPaused");
        SetPausedParams setPausedParams = new SetPausedParams(cis2wccdSchema, setPausedReceiveName, true);
        setPausedParams.initialize(true);
        return setPausedParams;
    }

    @SneakyThrows
    public static SchemaParameter generateSetMetadataUrlParams() {
        Schema cis2wccdSchema = Schema.from(Files.readAllBytes(SCHEMA_PATH), SchemaVersion.V3);

        // Initialize SetMetadataUrlParams
        ReceiveName setMetadataUrlReceiveName = ReceiveName.from(CONTRACT_NAME, "setMetadataUrl");
        String metadataUrl = "https://github.com/Concordium/concordium-contracts-common/blob/9d1f254e52a6bc730e4f8d92e353096cebe02f0a/concordium-contracts-common/src/types.rs";
        Hash hash = Hash.from("688787d8ff144c502c7f5cffaafe2cc588d86079f9de88304c26b0cb99ce91c6");
        SetMetadataUrlParams setMetadataUrlParams = new SetMetadataUrlParams(cis2wccdSchema, setMetadataUrlReceiveName, metadataUrl, Optional.of(hash));
        setMetadataUrlParams.initialize(true);
        return setMetadataUrlParams;
    }

    @SneakyThrows
    public static SchemaParameter generateTransferParams() {
        Schema cis2wccdSchema = Schema.from(Files.readAllBytes(SCHEMA_PATH), SchemaVersion.V3);
        ReceiveName nftTransferReceiveName = ReceiveName.from(CONTRACT_NAME, "transfer");
        TokenIdUnit tokenId = new TokenIdUnit();
        TokenAmountU64 amount = TokenAmountU64.from(1);
        AbstractAddress from = CONTRACT_ADDRESS_1;
        Receiver to = new Receiver(CONTRACT_ADDRESS_2, "mint");
        List<UInt8> data = new ArrayList<>();
        data.add(UInt8.from(123));
        data.add(UInt8.from(23));
        WCCDTransfer transfer = new WCCDTransfer(tokenId, amount, from, to, data);
        List<WCCDTransfer> transfers = new ArrayList<>();
        transfers.add(transfer);
        SchemaParameter transferParameter = new WCCDTransferParam(cis2wccdSchema, nftTransferReceiveName, transfers);
        transferParameter.initialize(true);

        return transferParameter;
    }

    @SneakyThrows
    public static SchemaParameter generateUpdateOperatorParams() {
        Schema cis2wccdSchema = Schema.from(Files.readAllBytes(SCHEMA_PATH), SchemaVersion.V3);
        ReceiveName updateOperatorReceiveName = ReceiveName.from(CONTRACT_NAME, "updateOperator");
        UpdateOperator update1 = new UpdateOperator(UpdateOperator.OperatorUpdate.ADD, ACCOUNT_ADDRESS);
        UpdateOperator update2 = new UpdateOperator(UpdateOperator.OperatorUpdate.REMOVE, CONTRACT_ADDRESS_1);
        List<UpdateOperator> updateOperatorList = new ArrayList<>();
        updateOperatorList.add(update1);
        updateOperatorList.add(update2);
        SchemaParameter updateOperatorsParams = new UpdateOperatorParams(cis2wccdSchema, updateOperatorReceiveName, updateOperatorList);
        updateOperatorsParams.initialize(true);

        return updateOperatorsParams;
    }

    @SneakyThrows
    public static SchemaParameter generateBalanceOfParams() {
        Schema cis2wccdSchema = Schema.from(Files.readAllBytes(SCHEMA_PATH), SchemaVersion.V3);
        ReceiveName balanceOfReceiveName = ReceiveName.from(CONTRACT_NAME, "balanceOf");
        WCCDBalanceOfQuery balanceOfQuery1 = new WCCDBalanceOfQuery(new TokenIdUnit(), ACCOUNT_ADDRESS);
        WCCDBalanceOfQuery balanceOfQuery2 = new WCCDBalanceOfQuery(new TokenIdUnit(), CONTRACT_ADDRESS_1);
        List<WCCDBalanceOfQuery> balanceOfQueries = new ArrayList<>();
        balanceOfQueries.add(balanceOfQuery1);
        balanceOfQueries.add(balanceOfQuery2);
        SchemaParameter contractBalanceOfQueryParams = new WCCDBalanceOfQueryParams(cis2wccdSchema, balanceOfReceiveName, balanceOfQueries);
        contractBalanceOfQueryParams.initialize(true);
        return contractBalanceOfQueryParams;
    }

    @SneakyThrows
    public static SchemaParameter generateOperatorOfParams() {
        Schema cis2wccdSchema = Schema.from(Files.readAllBytes(SCHEMA_PATH), SchemaVersion.V3);
        ReceiveName operatorOfReceiveName = ReceiveName.from(CONTRACT_NAME, "operatorOf");
        OperatorOfQuery operatorOfQuery1 = new OperatorOfQuery(ACCOUNT_ADDRESS, CONTRACT_ADDRESS_1);
        OperatorOfQuery operatorOfQuery2 = new OperatorOfQuery(CONTRACT_ADDRESS_1, CONTRACT_ADDRESS_2);
        List<OperatorOfQuery> operatorOfQueries = new ArrayList<>();
        operatorOfQueries.add(operatorOfQuery1);
        operatorOfQueries.add(operatorOfQuery2);
        SchemaParameter operatorOfQueryParams = new OperatorOfQueryParams(cis2wccdSchema, operatorOfReceiveName, operatorOfQueries);
        operatorOfQueryParams.initialize(true);
        return operatorOfQueryParams;
    }

    @SneakyThrows
    public static SchemaParameter generateTokenMetadataParams() {
        Schema cis2wccdSchema = Schema.from(Files.readAllBytes(SCHEMA_PATH), SchemaVersion.V3);
        ReceiveName tokenMetadataReceiveName = ReceiveName.from(CONTRACT_NAME, "tokenMetadata");
        TokenIdUnit token = new TokenIdUnit();
        List<TokenIdUnit> tokensForMetadataQuery = new ArrayList<>();
        tokensForMetadataQuery.add(token);
        SchemaParameter wccdMetadataQuery = new WCCDTokenMetadataQueryParams(cis2wccdSchema, tokenMetadataReceiveName, tokensForMetadataQuery);
        wccdMetadataQuery.initialize(true);
        return wccdMetadataQuery;
    }

    @SneakyThrows
    public static SchemaParameter generateSupportsParams() {
        Schema cis2wccdSchema = Schema.from(Files.readAllBytes(SCHEMA_PATH), SchemaVersion.V3);
        ReceiveName supportsReceiveName = ReceiveName.from(CONTRACT_NAME, "supports");
        String standardIdentifier1 = "identifier1";
        String standardIdentifier2 = "identifier2";
        List<String> identifiers = new ArrayList<>();
        identifiers.add(standardIdentifier1);
        identifiers.add(standardIdentifier2);
        SchemaParameter supportsQueryParams = new SupportsQueryParams(cis2wccdSchema, supportsReceiveName, identifiers);
        supportsQueryParams.initialize(true);
        return supportsQueryParams;
    }

    @SneakyThrows
    public static SchemaParameter generateSetImplementorsParams() {
        Schema cis2wccdSchema = Schema.from(Files.readAllBytes(SCHEMA_PATH), SchemaVersion.V3);
        ReceiveName setImplementorsReceiveName = ReceiveName.from(CONTRACT_NAME, "setImplementors");
        List<ContractAddress> implementors = new ArrayList<>();
        String identifier = "IdentifierID";
        implementors.add(CONTRACT_ADDRESS_1);
        implementors.add(CONTRACT_ADDRESS_2);
        SchemaParameter setImplementorsParams = new SetImplementorsParams(cis2wccdSchema, setImplementorsReceiveName, identifier, implementors);
        setImplementorsParams.initialize();
        return setImplementorsParams;
    }

    @SneakyThrows
    public static SchemaParameter generateUpgradeParams() {
        Schema cis2wccdSchema = Schema.from(Files.readAllBytes(SCHEMA_PATH), SchemaVersion.V3);
        ReceiveName upgradeReceiveName = ReceiveName.from(CONTRACT_NAME, "upgrade");
        ModuleRef upgradeModuleRef = ModuleRef.from("67d568433bd72e4326241f262213d77f446db8ba03dfba351ae35c1b2e7e5109");
        SchemaParameter migrate = generateWrapParams();
        SchemaParameter upgradeParams = new UpgradeParams(cis2wccdSchema, upgradeReceiveName, upgradeModuleRef, migrate);
        upgradeParams.initialize(true);
        return upgradeParams;
    }

}
