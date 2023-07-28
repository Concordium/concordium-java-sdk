package com.concordium.sdk.queries;

import com.concordium.sdk.responses.transactionstatus.*;
import com.concordium.sdk.responses.transactionstatusinblock.TransactionStatusInBlock;
import com.concordium.sdk.transactions.CCDAmount;
import com.concordium.sdk.transactions.Hash;
import com.concordium.sdk.types.AccountAddress;
import concordium.ConcordiumP2PRpc;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Optional;

public class GetTransactionStatusInBlockTest {

    @SneakyThrows
    @Test
    public void shouldDeserializeJsonTest() {
        val json = "{" +
                "\"result\": {" +
                "\"cost\": \"119965858\"," +
                "\"energyCost\": 96003," +
                "\"hash\": \"ea88c209c40f5828aeedf3326f314f66b7adf49e754a94f29b72e9d334d82eb7\"," +
                "\"index\": 0," +
                "\"result\": {" +
                "\"events\": [{" +
                "\"contents\": \"37eeb3e92025c97eaf40b66891770fcd22d926a91caeb1135c7ce7a1ba977c07\"," +
                "\"tag\": \"ModuleDeployed\"" +
                "}]," +
                "\"outcome\": \"success\"" +
                "}," +
                "\"sender\": \"48x2Uo8xCMMxwGuSQnwbqjzKtVqK5MaUud4vG7QEUgDmYkV85e\"," +
                "\"type\": { \"contents\": \"deployModule\", \"type\": \"accountTransaction\" }" +
                "}," +
                "\"status\": \"finalized\"" +
                "}";
        val req = ConcordiumP2PRpc.JsonResponse.newBuilder().setValue(json).build();
        val actual = TransactionStatusInBlock.fromJson(req);

        Assert.assertTrue(actual.isPresent());
        TransactionStatusInBlock expected = TransactionStatusInBlock.builder()
                .status(Status.FINALIZED)
                .result(Optional.of(TransactionSummary.builder()
                        .index(0)
                        .sender(AccountAddress.from("48x2Uo8xCMMxwGuSQnwbqjzKtVqK5MaUud4vG7QEUgDmYkV85e"))
                        .cost(CCDAmount.fromMicro(119_965_858L))
                        .energyCost(96003)
                        .hash(Hash.from("ea88c209c40f5828aeedf3326f314f66b7adf49e754a94f29b72e9d334d82eb7"))
                        .result(TransactionResult.builder()
                                .events(Arrays.asList(
                                        new ModuleCreatedResult("37eeb3e92025c97eaf40b66891770fcd22d926a91caeb1135c7ce7a1ba977c07")))
                                .outcome(Outcome.SUCCESS)
                                .build())
                        .type(TransactionTypeInfo.builder()
                                .type(TransactionType.ACCOUNT_TRANSACTION)
                                .contents(TransactionContents.DEPLOY_MODULE)
                                .build())
                        .build()))
                .build();
        Assert.assertEquals(expected, actual.get());
    }

    @Test
    public void shouldHandleNullJsonTest() {
        val json = "null";
        val req = ConcordiumP2PRpc.JsonResponse.newBuilder().setValue(json).build();
        val res = TransactionStatusInBlock.fromJson(req);

        Assert.assertFalse(res.isPresent());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldHandleInvalidJson() {
        val json = "{";
        val req = ConcordiumP2PRpc.JsonResponse.newBuilder().setValue(json).build();
        val res = TransactionStatusInBlock.fromJson(req);
    }
}
