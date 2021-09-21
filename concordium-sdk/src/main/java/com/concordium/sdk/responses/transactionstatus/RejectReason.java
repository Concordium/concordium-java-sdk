package com.concordium.sdk.responses.transactionstatus;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import lombok.Getter;
import lombok.ToString;
import lombok.val;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Getter
@ToString
public final class RejectReason {
    private final RejectReasonType tag;
    private final List<RejectContent> contents;
    private final String rejectReason;
    private final ContractAddress contractAddress;
    private final String receiveName;
    private final String parameter;

    @JsonCreator
    RejectReason(@JsonProperty("tag") RejectReasonType tag,
                 @JsonDeserialize(using = ContentDeserializer.class)
                 @JsonProperty("contents") List<RejectContent> contents,
                 @JsonProperty("rejectReason") String rejectReason,
                 @JsonProperty("contractAddress") ContractAddress contractAddress,
                 @JsonProperty("receiveName") String receiveName,
                 @JsonProperty("parameter") String parameter) {
        this.tag = tag;
        this.contents = contents;
        this.rejectReason = rejectReason;
        this.contractAddress = contractAddress;
        this.receiveName = receiveName;
        this.parameter = parameter;
    }

    static class ContentDeserializer extends StdDeserializer<List<RejectContent>> {

        protected ContentDeserializer() {
            super(RejectContent.class);
        }

        @Override
        public List<RejectContent> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            val result = new ArrayList<RejectContent>();
            if (p.currentToken() == JsonToken.START_ARRAY) {
                val list = p.readValueAs(List.class);
                for (Object item : list) {
                    if (item instanceof String) {
                        result.add(new RejectContentString((String) item));
                    }
                    if (item instanceof Map) {
                        val account = AbstractAccount.parseAccount((Map<String, Object>) item);
                        result.add(new RejectContentAccount(account));
                    }
                }
            }
            return result;
        }
    }
}
