package com.concordium.sdk.responses.invokeinstance;

import com.concordium.sdk.responses.transactionevent.accounttransactionresults.ContractTraceElement;
import com.concordium.sdk.responses.transactionstatus.Outcome;
import com.concordium.sdk.responses.transactionstatus.RejectReason;
import com.concordium.sdk.types.Energy;
import com.google.common.collect.ImmutableList;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.val;

import java.util.List;

/**
 * Response type for InvokeInstance
 */
@Builder
@EqualsAndHashCode
@ToString
public class InvokeInstanceResponse {

    /**
     * Whether the contract execution succeeded or failed.
     */
    private Outcome outcome;

    /**
     * Energy used by the execution.
     */
    private Energy usedEnergy;

    /**
     * If invoking a V0 contract this is absent.
     * Otherwise, if outcome is {@link Outcome#SUCCESS} this is the return value produced by the contract.
     * If outcome is {@link Outcome#REJECT} this is the return value unless the call failed with out of energy runtime error.
     * If the V1 contract terminated with a logic error then the return value is present.
     */
    private byte[] returnValue;

    /**
     * Reason for failed contract execution.
     * Note, only populated if outcome is {@link Outcome#REJECT}.
     */
    private RejectReason rejectReason;

    /**
     * Effects produced by contract execution.
     * Note, only populated if outcome is {@link Outcome#SUCCESS}.
     */
    private List<ContractTraceElement> effects;

    /**
     * Parses {@link com.concordium.grpc.v2.InvokeInstanceResponse} to {@link InvokeInstanceResponse}.
     *
     * @param response {@link com.concordium.grpc.v2.InvokeInstanceResponse} returned by the GRPC V2 API.
     * @return parsed {@link InvokeInstanceResponse}.
     */
    public static InvokeInstanceResponse parse(com.concordium.grpc.v2.InvokeInstanceResponse response) {
        val builder = InvokeInstanceResponse.builder();
        switch (response.getResultCase()) {
            case SUCCESS:
                val success = response.getSuccess();
                val effects = new ImmutableList.Builder<ContractTraceElement>();
                if (success.hasReturnValue()) {
                    builder.returnValue(success.getReturnValue().toByteArray());
                }
                success.getEffectsList().forEach(e -> effects.add(ContractTraceElement.parse(e)));

                builder.outcome(Outcome.SUCCESS)
                        .usedEnergy(Energy.parse(success.getUsedEnergy()))
                        .effects(effects.build());
                break;
            case FAILURE:
                val failure = response.getFailure();
                if (failure.hasReturnValue()) {
                    builder.returnValue(failure.getReturnValue().toByteArray());
                }
                builder.outcome(Outcome.REJECT)
                        .usedEnergy(Energy.parse(failure.getUsedEnergy()))
                        .rejectReason(RejectReason.parse(failure.getReason()));

                break;
        }
        return builder.build();
    }
}
