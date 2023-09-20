package com.concordium.sdk.examples.contractexample.wccd;

import com.concordium.sdk.examples.contractexample.cis2nft.Receiver;
import com.concordium.sdk.responses.modulelist.ModuleRef;
import com.concordium.sdk.transactions.Hash;
import com.concordium.sdk.transactions.ReceiveName;
import com.concordium.sdk.transactions.smartcontracts.Schema;
import com.concordium.sdk.transactions.smartcontracts.SchemaParameter;
import com.concordium.sdk.transactions.smartcontracts.SchemaVersion;
import com.concordium.sdk.types.AccountAddress;
import com.concordium.sdk.types.ContractAddress;
import com.concordium.sdk.types.UInt8;
import lombok.SneakyThrows;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class WCCDExample {

    @SneakyThrows
    public static void main(String[] args) {
        AccountAddress accountAddress = AccountAddress.from("3XSLuJcXg6xEua6iBPnWacc3iWh93yEDMCqX8FbE3RDSbEnT9P");
        ContractAddress contractAddress1 = ContractAddress.from(1, 0);
        ContractAddress contractAddress2 = ContractAddress.from(2, 0);
        Schema cis2wccdSchema = Schema.from(Files.readAllBytes(Paths.get("./src/main/java/com/concordium/sdk/contractexample/wccd/cis2-wccd.schema.bin")), SchemaVersion.V3);
        String cis2wccdContractName = "cis2_wCCD";

        // Initialize UnwrapParams

        ReceiveName unwrapReceiveName = ReceiveName.from(cis2wccdContractName, "unwrap");
        String unwrapAmount = "2"; // TokenAmountU64
        Receiver unwrapReceiver = new Receiver(contractAddress2, "test");
        List<UInt8> unwrapData = new ArrayList<>();
        unwrapData.add(UInt8.from(1));
        unwrapData.add(UInt8.from(42));
        UnwrapParams unwrapParams = new UnwrapParams(cis2wccdSchema, unwrapReceiveName, unwrapAmount, accountAddress, unwrapReceiver, unwrapData);
        unwrapParams.initialize(true);

        // Initialize WrapParams

        ReceiveName wrapReceiveName = ReceiveName.from(cis2wccdContractName, "wrap");
        Receiver wrapReceiver = new Receiver(accountAddress);
        List<UInt8> wrapData = new ArrayList<>();
        WrapParams wrapParams = new WrapParams(cis2wccdSchema, wrapReceiveName, wrapReceiver, wrapData);
        wrapParams.initialize(true);

        // Initialize SetImplementorsParams
        // Already exists and works

        // Initialize UpgradeParams
        ReceiveName upgradeReceiveName = ReceiveName.from(cis2wccdContractName, "upgrade");
        ModuleRef upgradeModuleRef = ModuleRef.from("67d568433bd72e4326241f262213d77f446db8ba03dfba351ae35c1b2e7e5109");
        SchemaParameter migrate = new WrapParams(cis2wccdSchema, wrapReceiveName, wrapReceiver, wrapData);
        UpgradeParams upgradeParams = new UpgradeParams(cis2wccdSchema, upgradeReceiveName, upgradeModuleRef.toString(), migrate);
        upgradeParams.initialize(true);


        // Initialize SetMetadataUrlParams
        ReceiveName setMetadataUrlReceiveName = ReceiveName.from(cis2wccdContractName, "setMetadataUrl");
        String metadataUrl = "https://github.com/Concordium/concordium-contracts-common/blob/9d1f254e52a6bc730e4f8d92e353096cebe02f0a/concordium-contracts-common/src/types.rs";
        Hash hash = Hash.from("688787d8ff144c502c7f5cffaafe2cc588d86079f9de88304c26b0cb99ce91c6");
        SetMetadataUrlParams setMetadataUrlParams1 = new SetMetadataUrlParams(cis2wccdSchema, setMetadataUrlReceiveName, metadataUrl, Optional.of(hash));
        SetMetadataUrlParams setMetadataUrlParams2 = new SetMetadataUrlParams(cis2wccdSchema, setMetadataUrlReceiveName, metadataUrl, Optional.empty());
        setMetadataUrlParams1.initialize(true);
        setMetadataUrlParams2.initialize(true);


        // Initialize SetPausedParams
        ReceiveName setPausedReceiveName = ReceiveName.from(cis2wccdContractName, "setPaused");
        SetPausedParams setPausedParams = new SetPausedParams(cis2wccdSchema, setPausedReceiveName, true);
        setPausedParams.initialize(true);

        // Initialize Address as UpdateAdminParam
        ReceiveName updateAdmninReceiveName = ReceiveName.from(cis2wccdContractName, "updateAdmin");
        AddressParam updateAdminParam = new AddressParam(cis2wccdSchema, updateAdmninReceiveName, contractAddress1);
        AddressParam updateAdminParam2 = new AddressParam(cis2wccdSchema, updateAdmninReceiveName, accountAddress);
        updateAdminParam.initialize(true);
        updateAdminParam2.initialize(true);

        // Initialize TransferParameter
        // Is done since no wrapper types are used e.g. no constraints on 'amount' and 'token_id' is being enforced

        // Initialize UpdateOperatorParams
        // Is done

        // Initialize ContractBalanceOfQueryParams
        // Same as TransferParameter

        // Initialize OperatorOfQueryParams
        // Is done

        // Initialize ContractTokenMetadataQueryParams
        // Same as TransferParameter

        // Initialize SupportsQueryParams
        // Is done

    }
}
