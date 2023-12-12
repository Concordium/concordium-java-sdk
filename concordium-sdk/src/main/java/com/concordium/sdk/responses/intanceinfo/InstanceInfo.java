package com.concordium.sdk.responses.intanceinfo;

import com.concordium.sdk.responses.modulelist.ModuleRef;
import com.concordium.sdk.responses.smartcontracts.ContractVersion;
import com.concordium.sdk.transactions.CCDAmount;
import com.concordium.sdk.types.AccountAddress;
import com.google.common.collect.ImmutableList;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * Smart Contract Instance Info.
 */
@Data
@EqualsAndHashCode
public class InstanceInfo {

    /**
     * The account address which deployed the instance.
     */
    private final AccountAddress owner;

    /**
     * The amount of CCD tokens in the balance of the instance.
     */
    private final CCDAmount amount;

    /**
     * The endpoints exposed by the instance.
     */
    private final ImmutableList<String> methods;

    /**
     * The name of the smart contract instance.
     */
    private final String name;

    /**
     * The module reference.
     */
    private final ModuleRef sourceModule;

    /**
     * The version of the contract.
     */
    private final ContractVersion version;
    @Builder
    public InstanceInfo(
            AccountAddress owner,
            CCDAmount amount,
            List<String> methods,
            String name,
            ModuleRef sourceModule,
            ContractVersion version
    ) {
        this.owner = owner;
        this.amount = amount;
        this.methods = ImmutableList.copyOf(methods);
        this.name = name;
        this.sourceModule = sourceModule;
        this.version = version;
    }
}
