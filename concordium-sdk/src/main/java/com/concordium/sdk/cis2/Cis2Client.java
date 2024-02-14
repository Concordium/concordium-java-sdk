package com.concordium.sdk.cis2;

import com.concordium.sdk.ClientV2;
import com.concordium.sdk.cis2.events.Cis2Event;
import com.concordium.sdk.cis2.events.Cis2EventWithMetadata;
import com.concordium.sdk.requests.AccountQuery;
import com.concordium.sdk.requests.BlockQuery;
import com.concordium.sdk.requests.smartcontracts.Energy;
import com.concordium.sdk.requests.smartcontracts.InvokeInstanceRequest;
import com.concordium.sdk.responses.blockitemstatus.FinalizedBlockItem;
import com.concordium.sdk.responses.blockitemsummary.Summary;
import com.concordium.sdk.responses.blockitemsummary.Type;
import com.concordium.sdk.responses.blocksatheight.BlocksAtHeightRequest;
import com.concordium.sdk.responses.smartcontracts.ContractTraceElement;
import com.concordium.sdk.responses.smartcontracts.ContractTraceElementType;
import com.concordium.sdk.responses.transactionstatus.ContractUpdated;
import com.concordium.sdk.responses.transactionstatus.Outcome;
import com.concordium.sdk.responses.transactionstatus.TransactionResultEventType;
import com.concordium.sdk.transactions.*;
import com.concordium.sdk.types.*;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.val;
import lombok.var;

import java.util.*;
import java.util.stream.Collectors;

/**
 * A client dedicated to the CIS2 <a href="https://proposals.concordium.software/CIS/cis-2.html">specification</a>.
 */
@Getter
public class Cis2Client {

    final ClientV2 client;
    private final ContractAddress contractAddress;
    private final InitName contractName;

    private Cis2Client(ClientV2 client, ContractAddress contractAddress, InitName contractName) {
        this.client = client;
        this.contractAddress = contractAddress;
        this.contractName = contractName;
    }

    /**
     * Construct a new {@link Cis2Client} with the provided {@link ClientV2} for the provided {@link ContractAddress}
     *
     * @param client  client to use
     * @param address the address of the cis 2 contract
     * @return a cis2 client for interfacing with the provided contract
     */
    public static Cis2Client newClient(ClientV2 client, ContractAddress address) {
        val instanceInfo = client.getInstanceInfo(BlockQuery.LAST_FINAL, address);
        return new Cis2Client(client, address, InitName.from(instanceInfo.getName()));
    }

    /**
     * Perform a CIS2 transfer on the contract.
     *
     * @param sender    address of the sender of the transaction.
     * @param signer    signer of the transaction.
     * @param transfers the CIS2 transfers.
     * @return the transaction hash
     */
    public Hash transfer(AccountAddress sender, TransactionSigner signer, Energy maxEnergyCost, Cis2Transfer... transfers) {
        val listOfTransfers = Arrays.asList(transfers);
        val nextNonce = this.client.getAccountInfo(BlockQuery.LAST_FINAL, AccountQuery.from(sender)).getNonce();
        val endpoint = ReceiveName.from(contractName, "transfer");
        val parameters = SerializationUtils.serializeTransfers(listOfTransfers);
        return this.client.sendTransaction(
                TransactionFactory.newUpdateContract()
                        .maxEnergyCost(maxEnergyCost.getValue())
                        .payload(UpdateContract.from(CCDAmount.from(0), this.contractAddress, endpoint, parameters))
                        .expiry(Expiry.createNew().addMinutes(5))
                        .nonce(nextNonce)
                        .sender(sender)
                        .signer(signer)
                        .build());
    }

    /**
     * Update the addresses of which the owner (sender of this transaction) operates.
     *
     * @param operatorUpdates the updates to carry out. The keys of the map correspond to the
     *                        addresses which the sender (owner) should or should not operate given
     *                        by the provided boolean.
     * @return the transaction hash
     */
    public Hash updateOperator(AccountAddress sender, TransactionSigner signer, Energy maxEnergyCost, Map<AbstractAddress, Boolean> operatorUpdates) {
        val nextNonce = this.client.getAccountInfo(BlockQuery.LAST_FINAL, AccountQuery.from(sender)).getNonce();
        val endpoint = ReceiveName.from(contractName, "updateOperator");
        val parameters = SerializationUtils.serializeUpdateOperators(operatorUpdates);
        return this.client.sendTransaction(
                TransactionFactory.newUpdateContract()
                        .maxEnergyCost(maxEnergyCost.getValue())
                        .payload(UpdateContract.from(CCDAmount.from(0), this.contractAddress, endpoint, parameters))
                        .expiry(Expiry.createNew().addMinutes(5))
                        .nonce(nextNonce)
                        .sender(sender)
                        .signer(signer)
                        .build());

    }

    /**
     * Query the balance of token ids and associated {@link com.concordium.sdk.types.AbstractAddress}
     *
     * @param queries the token ids and addresses to query
     * @return the balances together with the queries used
     */
    public Map<BalanceQuery, TokenAmount> balanceOf(BalanceQuery... queries) {
        val listOfQueries = Arrays.asList(queries);
        val parameter = SerializationUtils.serializeBalanceOfParameter(listOfQueries);
        val endpoint = ReceiveName.from(contractName, "balanceOf");
        val result = this.client.invokeInstance(InvokeInstanceRequest.from(BlockQuery.LAST_FINAL, this.contractAddress, CCDAmount.from(0), endpoint, parameter, Optional.empty()));
        if (result.getOutcome() == Outcome.REJECT) {
            throw new RuntimeException("balanceOf failed: " + result.getRejectReason().toString());
        }
        val balances = SerializationUtils.deserializeTokenAmounts(result.getReturnValue());
        val responses = new HashMap<BalanceQuery, TokenAmount>();
        for (int i = 0; i < balances.length; i++) {
            responses.put(listOfQueries.get(i), balances[i]);
        }
        return responses;
    }

    /**
     * Query whether one or more owners are operators for one or more addresses.
     *
     * @param queries the addresses to query.
     * @return A map where the values indicate whether the specified owner was indeed operator of the supplied address.
     */
    public Map<OperatorQuery, Boolean> operatorOf(OperatorQuery... queries) {
        val listOfQueries = Arrays.asList(queries);
        val parameter = SerializationUtils.serializeOperatorOfParameter(listOfQueries);
        val endpoint = ReceiveName.from(contractName, "operatorOf");
        val result = this.client.invokeInstance(InvokeInstanceRequest.from(BlockQuery.LAST_FINAL, this.contractAddress, CCDAmount.from(0), endpoint, parameter, Optional.empty()));
        if (result.getOutcome() == Outcome.REJECT) {
            throw new RuntimeException("operatorOf failed: " + result.getRejectReason().toString());
        }
        val isOperatorOf = SerializationUtils.deserializeOperatorOfResponse(result.getReturnValue());
        val responses = new HashMap<OperatorQuery, Boolean>();
        for (int i = 0; i < isOperatorOf.length; i++) {
            responses.put(listOfQueries.get(i), isOperatorOf[i]);
        }
        return responses;
    }

    /**
     * Query the token metadata for each provided token id
     *
     * @param tokenIds the token ids to query
     * @return A map where the values indicate the token metadata responses for each {@link TokenId}
     */
    public Map<TokenId, TokenMetadata> tokenMetadata(TokenId... tokenIds) {
        val listOfQueries = Arrays.asList(tokenIds);
        val parameter = SerializationUtils.serializeTokenIds(listOfQueries);
        val endpoint = ReceiveName.from(contractName, "tokenMetadata");
        val result = this.client.invokeInstance(InvokeInstanceRequest.from(BlockQuery.LAST_FINAL, this.contractAddress, CCDAmount.from(0), endpoint, parameter, Optional.empty()));
        if (result.getOutcome() == Outcome.REJECT) {
            throw new RuntimeException("tokenMetadata failed: " + result.getRejectReason().toString());
        }
        val tokenMetadatas = SerializationUtils.deserializeTokenMetadatas(result.getReturnValue());
        val responses = new HashMap<TokenId, TokenMetadata>();
        for (int i = 0; i < tokenMetadatas.length; i++) {
            responses.put(listOfQueries.get(i), tokenMetadatas[i]);
        }
        return responses;
    }

    /**
     * Retrieve all events emitted from the CIS2 contract.
     *
     * @param from block to start from
     * @param to   block to end from
     * @return the list of events.
     */
    public Iterator<Cis2EventWithMetadata> getEvents(BlockQuery from, BlockQuery to) {
        final long[] current = {this.client.getBlockInfo(from).getBlockHeight().getValue()};
        val end = this.client.getBlockInfo(to).getBlockHeight().getValue();
        if (current[0] >= end) {
            throw new IllegalArgumentException("Starting block must be before the end block");
        }
        return new Cis2EventIterator(this, new Iterator<BlockQuery>() {
            @Override
            public boolean hasNext() {
                return end > current[0];
            }

            @Override
            public BlockQuery next() {
                BlockQuery query = BlockQuery.HEIGHT(BlocksAtHeightRequest.newAbsolute(current[0]));
                current[0] = current[0] + 1;
                return query;
            }
        });
    }


    /**
     * Get any events associated emitted from the specified CIS2 contract.
     *
     * @param queries blocks to query
     * @return the list of events.
     */
    public Iterator<Cis2EventWithMetadata> getEventsFor(BlockQuery... queries) {
        return new Cis2EventIterator(this, Lists.newArrayList(queries).iterator());
    }

    /**
     * Get any events associated emitted from the specified CIS2 contract by the
     * supplied transaction hash.
     *
     * @param transactionHash the hash of the transaction to query outcome for.
     * @return the list of events which originated from the specified transaction hash.
     * @throws IllegalArgumentException if the transaction was not finalized.
     */
    public Iterator<Cis2EventWithMetadata> getEventsForFinalizedTransaction(Hash transactionHash) {
        val status = this.client.getBlockItemStatus(transactionHash);
        if (!status.getFinalizedBlockItem().isPresent()) {
            if (status.getCommittedBlockItem().isPresent()) {
                throw new IllegalArgumentException("Transaction was not finalized. But it was committed in block(s) " + status.getCommittedBlockItem().get().getSummaries().keySet());
            }
            throw new IllegalArgumentException("Transaction was not finalized, but was " + status.getStatus().toString());
        }
        val accumulator = new ArrayList<Cis2EventWithMetadata>();
        val finalizedTransaction = status.getFinalizedBlockItem().get();
        return new Cis2EventIterator(this, Lists.newArrayList(BlockQuery.HASH(finalizedTransaction.getBlockHash())).iterator());
    }


    /**
     * Get any events associated with the CIS2 contract that this client is instantiated with.
     *
     * @param blockQuery the block to query events for.
     * @return The list of events if there are any.
     */
    private Iterator<Cis2EventWithMetadata> getEventsFor(BlockQuery blockQuery) {
        return new Cis2EventIterator(this, Lists.newArrayList(blockQuery).iterator());
    }


}
