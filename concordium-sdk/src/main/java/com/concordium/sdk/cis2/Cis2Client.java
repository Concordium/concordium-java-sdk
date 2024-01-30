package com.concordium.sdk.cis2;

import com.concordium.sdk.ClientV2;
import com.concordium.sdk.requests.BlockQuery;
import com.concordium.sdk.requests.smartcontracts.Energy;
import com.concordium.sdk.requests.smartcontracts.InvokeInstanceRequest;
import com.concordium.sdk.responses.transactionstatus.Outcome;
import com.concordium.sdk.transactions.CCDAmount;
import com.concordium.sdk.transactions.InitName;
import com.concordium.sdk.transactions.ReceiveName;
import com.concordium.sdk.types.ContractAddress;
import com.concordium.sdk.types.UInt16;
import com.concordium.sdk.types.UInt64;
import lombok.val;

import java.nio.ByteBuffer;
import java.util.*;

/**
 * A client dedicated to the CIS2 <a href="https://proposals.concordium.software/CIS/cis-2.html">specification</a>.
 */
public class Cis2Client {

    private final ClientV2 client;
    private final ContractAddress contractAddress;
    private final InitName contractName;

    private static final Energy MAX_ENERGY = Energy.from(UInt64.from(1000000));

    private Cis2Client(ClientV2 client, ContractAddress contractAddress, InitName contractName) {
        this.client = client;
        this.contractAddress = contractAddress;
        this.contractName = contractName;
    }

    public static Cis2Client newClient(ClientV2 client, ContractAddress address) {
        val instanceInfo = client.getInstanceInfo(BlockQuery.LAST_FINAL, address);
        return new Cis2Client(client, address, InitName.from(instanceInfo.getName()));
    }

    public void transfer() {

    }

    public void updateOperator() {

    }

    public int[] balanceOf(Collection<BalanceQuery> query) {
        val parameter = SerializationUtils.serializeBalanceOfParameter(query);
        val endpoint = ReceiveName.from(contractName, "balanceOf");
        val result = this.client.invokeInstance(InvokeInstanceRequest.from(BlockQuery.LAST_FINAL, this.contractAddress, CCDAmount.from(0), endpoint, parameter, MAX_ENERGY));
        if (result.getOutcome() == Outcome.REJECT) {
            System.out.println(result);
            throw new RuntimeException("balanceOf failed: " + result.getRejectReason().toString());
        }
        ByteBuffer resultBuffer = ByteBuffer.wrap(result.getReturnValue());
        short noOfOutputs = resultBuffer.getShort();
        val outputs = new int[noOfOutputs];
        for (int i = 0; i < noOfOutputs; i++) {
            outputs[i] = resultBuffer.get();
            resultBuffer.get();
        }
        return outputs;
    }

    public void operatorOf() {
    }

    public void tokenMetadata() {
    }


}
