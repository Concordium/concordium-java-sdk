package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.serializing.JsonMapper;
import com.concordium.sdk.transactions.Hash;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertEquals;

public class RejectReasonDeserializingTest {

    @SneakyThrows
    @Test
    public void testDeserializeAmountTooLarge() {
        val deserialized = JsonMapper.INSTANCE.readValue(amountTooLargeJson, TransactionStatus.class);
        Set<Hash> outcomes = deserialized.getOutcomes().keySet();
        for (Hash outcome : outcomes) {
            TransactionSummary transactionSummary = deserialized.getOutcomes().get(outcome);
            val rejectReason = transactionSummary.getResult().getRejectReason();
            RejectReasonType type = rejectReason.getType();

            if (type == RejectReasonType.AMOUNT_TOO_LARGE) {
                val converted = type.<RejectReasonAmountTooLarge>convert(rejectReason);
                assertEquals("4PXJrvKGKb1YZt2Vua3mSDThqUU2EChweuydsFtVFtvDDr585H", ((Account) converted.getAccount()).getAddress().encoded())
                ;
            }

        }
    }

    private final static String amountTooLargeJson = "{\n" +
            "  \"status\": \"finalized\",\n" +
            "  \"outcomes\": {\n" +
            "    \"f161507c4a75ebb931b1f0e0612c8151cce4a011767c1f58288cd7feb4ab6b01\": {\n" +
            "      \"hash\": \"5cef01130216caff5bb5d0f6f8650fc562b84d1640f90b5d67e312558980774a\",\n" +
            "      \"sender\": \"4PXJrvKGKb1YZt2Vua3mSDThqUU2EChweuydsFtVFtvDDr585H\",\n" +
            "      \"cost\": \"70100\",\n" +
            "      \"energyCost\": 701,\n" +
            "      \"result\": {\n" +
            "        \"outcome\": \"reject\",\n" +
            "        \"rejectReason\": {\n" +
            "          \"tag\": \"AmountTooLarge\",\n" +
            "          \"contents\": [\n" +
            "            {\n" +
            "              \"address\": \"4PXJrvKGKb1YZt2Vua3mSDThqUU2EChweuydsFtVFtvDDr585H\",\n" +
            "              \"type\": \"AddressAccount\"\n" +
            "            },\n" +
            "            \"899996927700\"\n" +
            "          ]\n" +
            "        }\n" +
            "      },\n" +
            "      \"type\": {\n" +
            "        \"contents\": \"transfer\",\n" +
            "        \"type\": \"accountTransaction\"\n" +
            "      },\n" +
            "      \"index\": 0\n" +
            "    }\n" +
            "  }\n" +
            "}";
}


