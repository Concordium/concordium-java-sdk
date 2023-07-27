package com.concordium.sdk.responses.intanceinfo;

import com.concordium.sdk.responses.modulelist.ModuleRef;
import com.concordium.sdk.responses.transactionstatus.ContractVersion;
import com.concordium.sdk.serializing.JsonMapper;
import com.concordium.sdk.types.AccountAddress;
import com.concordium.sdk.transactions.CCDAmount;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.ImmutableList;
import concordium.ConcordiumP2PRpc;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.val;

import java.util.List;
import java.util.Optional;

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

    @JsonCreator
    @Builder
    public InstanceInfo(
            @JsonProperty("owner") AccountAddress owner,
            @JsonProperty("amount") CCDAmount amount,
            @JsonProperty("methods") List<String> methods,
            @JsonProperty("name") String name,
            @JsonProperty("sourceModule") ModuleRef sourceModule,
            @JsonProperty("version") ContractVersion version
    ) {
        this.owner = owner;
        this.amount = amount;
        this.methods = ImmutableList.copyOf(methods);
        this.name = name;
        this.sourceModule = sourceModule;
        this.version = version;
    }

    public static Optional<InstanceInfo> fromJson(ConcordiumP2PRpc.JsonResponse res) {
        try {
            val ret = JsonMapper.INSTANCE.readValue(res.getValue(), InstanceInfo.class);

            return Optional.ofNullable(ret);
        } catch (JsonProcessingException ex) {
            throw new IllegalArgumentException("Could not deserialize Contract Instance Info JSON");
        }
    }
}
