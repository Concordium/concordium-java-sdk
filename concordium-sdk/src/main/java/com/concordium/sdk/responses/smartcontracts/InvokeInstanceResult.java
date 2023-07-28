package com.concordium.sdk.responses.smartcontracts;

import com.concordium.sdk.requests.smartcontracts.Energy;
import com.concordium.sdk.responses.transactionstatus.Outcome;
import com.concordium.sdk.responses.transactionstatus.RejectReasonType;
import com.google.common.collect.ImmutableList;
import lombok.*;

import java.util.List;

@EqualsAndHashCode
@ToString
@Builder
@Getter
public class InvokeInstanceResult {

    /**
     * Whether the contract execution succeeded or failed.
     */
    private final Outcome outcome;

    /**
     * Energy used by the execution.
     */
    private final Energy usedEnergy;

    /**
     * If invoking a V0 contract this is absent.
     * Otherwise, if outcome is {@link Outcome#SUCCESS} this is the return value produced by the contract.
     * If outcome is {@link Outcome#REJECT} this is the return value unless the call failed with 'out of energy' runtime error.
     * If the V1 contract terminated with a logic error then the return value is present.
     */
    private final byte[] returnValue;

    /**
     * Reason for failed contract execution.
     * Note, only populated if outcome is {@link Outcome#REJECT}.
     */
    private final RejectReasonType rejectReason;

    /**
     * Effects produced by contract execution.
     * Note, only populated if outcome is {@link Outcome#SUCCESS}.
     */
    private final List<ContractTraceElement> effects;

    /**
     * Parses {@link com.concordium.grpc.v2.InvokeInstanceResponse} to {@link InvokeInstanceResult}.
     *
     * @param response {@link com.concordium.grpc.v2.InvokeInstanceResponse} returned by the GRPC V2 API.
     * @return parsed {@link InvokeInstanceResult}.
     */
    public static InvokeInstanceResult parse(com.concordium.grpc.v2.InvokeInstanceResponse response) {
        val builder = InvokeInstanceResult.builder();
        switch (response.getResultCase()) {
            case SUCCESS:
                val success = response.getSuccess();
                val effects = new ImmutableList.Builder<ContractTraceElement>();
                if (success.hasReturnValue()) {
                    builder.returnValue(success.getReturnValue().toByteArray());
                }
                success.getEffectsList().forEach(e -> effects.add(ContractTraceElement.from(e)));

                builder.outcome(Outcome.SUCCESS)
                        .usedEnergy(Energy.from(success.getUsedEnergy()))
                        .effects(effects.build());
                break;
            case FAILURE:
                val failure = response.getFailure();
                if (failure.hasReturnValue()) {
                    builder.returnValue(failure.getReturnValue().toByteArray());
                }
                builder.outcome(Outcome.REJECT)
                        .usedEnergy(Energy.from(failure.getUsedEnergy()))
                        .rejectReason(RejectReasonType.from(failure.getReason()));

                break;
        }
        return builder.build();
    }
}
