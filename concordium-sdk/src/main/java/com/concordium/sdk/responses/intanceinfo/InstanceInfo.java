package com.concordium.sdk.responses.intanceinfo;

import com.concordium.sdk.exceptions.InstanceNotFoundException;
import com.concordium.sdk.requests.getinstanceinfo.GetInstanceInfoRequest;
import com.concordium.sdk.responses.transactionstatus.ContractVersion;
import com.concordium.sdk.serializing.JsonMapper;
import com.concordium.sdk.transactions.AccountAddress;
import com.concordium.sdk.transactions.CCDAmount;
import com.fasterxml.jackson.core.JsonProcessingException;
import concordium.ConcordiumP2PRpc;
import lombok.Data;
import lombok.Getter;
import lombok.val;

import java.util.List;

@Data
@Getter
public class InstanceInfo {

    private AccountAddress owner;
    private CCDAmount amount;
    private List<String> methods;
    private String name;
    private String sourceModule;
    private ContractVersion version;

    public static InstanceInfo fromJson(ConcordiumP2PRpc.JsonResponse res, GetInstanceInfoRequest req)
            throws InstanceNotFoundException, JsonProcessingException {
        val ret = JsonMapper.INSTANCE.readValue(res.getValue(), InstanceInfo.class);

        if(ret == null) {
            throw InstanceNotFoundException.from(req);
        }

        return ret;
    }
}
