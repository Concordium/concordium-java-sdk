package com.concordium.sdk.responses.transactionevent.updatepayloads;

public class FinalizationCommitteeParametersUpdatePayload implements UpdatePayload {


    /**
     * TODO needs the GRPC type generated ??
     * @return
     */
    public static FinalizationCommitteeParametersUpdatePayload parse(){
        return null;
    };

    @Override
    public UpdateType getType() {
        return UpdateType.FINALIZATION_COMMITTEE_PARAMETERS_UPDATE;
    }
}
