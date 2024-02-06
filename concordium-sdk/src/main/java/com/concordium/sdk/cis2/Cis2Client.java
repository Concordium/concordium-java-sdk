package com.concordium.sdk.cis2;

import com.concordium.sdk.ClientV2;
import com.concordium.sdk.cis2.events.Cis2EventWithMetadata;
import com.concordium.sdk.requests.AccountQuery;
import com.concordium.sdk.requests.BlockQuery;
import com.concordium.sdk.requests.smartcontracts.Energy;
import com.concordium.sdk.requests.smartcontracts.InvokeInstanceRequest;
import com.concordium.sdk.responses.blockitemsummary.Type;
import com.concordium.sdk.responses.blocksatheight.BlocksAtHeightRequest;
import com.concordium.sdk.responses.smartcontracts.ContractTraceElement;
import com.concordium.sdk.responses.smartcontracts.ContractTraceElementType;
import com.concordium.sdk.responses.transactionstatus.ContractUpdated;
import com.concordium.sdk.responses.transactionstatus.Outcome;
import com.concordium.sdk.responses.transactionstatus.TransactionResultEventType;
import com.concordium.sdk.transactions.*;
import com.concordium.sdk.types.AbstractAddress;
import com.concordium.sdk.types.AccountAddress;
import com.concordium.sdk.types.ContractAddress;
import com.concordium.sdk.types.UInt64;
import lombok.val;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * A client dedicated to the CIS2 <a href="https://proposals.concordium.software/CIS/cis-2.html">specification</a>.
 */
public class Cis2Client {

    private final ClientV2 client;
    private final ContractAddress contractAddress;
    private final InitName contractName;

    // Default max energy for contract invocations.
    private static final Energy MAX_ENERGY = Energy.from(UInt64.from(3000000));

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
    public Hash transfer(AccountAddress sender, TransactionSigner signer, Cis2Transfer... transfers) {
        val listOfTransfers = Arrays.asList(transfers);
        val nextNonce = this.client.getAccountInfo(BlockQuery.LAST_FINAL, AccountQuery.from(sender)).getAccountNonce();
        val endpoint = ReceiveName.from(contractName, "transfer");
        val parameters = SerializationUtils.serializeTransfers(listOfTransfers);
        return this.client.sendTransaction(
                TransactionFactory.newUpdateContract()
                        .maxEnergyCost(UInt64.from(3000))
                        .payload(UpdateContract.from(CCDAmount.from(0), this.contractAddress, endpoint, parameters))
                        .expiry(Expiry.createNew().addMinutes(5))
                        .nonce(AccountNonce.from(nextNonce))
                        .sender(sender)
                        .signer(signer)
                        .build());
    }

    /**
     * Update which addresses that the sender (owner) operates.
     *
     * @param operatorUpdates the updates to carry out. The keys of the map correspond to the
     *                        addresses which the sender (owner) should or should not operate given
     *                        by the provided boolean.
     * @return the transaction hash
     */
    public Hash updateOperator(Map<AbstractAddress, Boolean> operatorUpdates, AccountAddress sender, TransactionSigner signer) {
        val nextNonce = this.client.getAccountInfo(BlockQuery.LAST_FINAL, AccountQuery.from(sender)).getAccountNonce();
        val endpoint = ReceiveName.from(contractName, "updateOperator");
        val parameters = SerializationUtils.serializeUpdateOperators(operatorUpdates);
        return this.client.sendTransaction(
                TransactionFactory.newUpdateContract()
                        .maxEnergyCost(UInt64.from(3000))
                        .payload(UpdateContract.from(CCDAmount.from(0), this.contractAddress, endpoint, parameters))
                        .expiry(Expiry.createNew().addMinutes(5))
                        .nonce(AccountNonce.from(nextNonce))
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
    public Map<BalanceQuery, Long> balanceOf(BalanceQuery... queries) {
        val listOfQueries = Arrays.asList(queries);
        val parameter = SerializationUtils.serializeBalanceOfParameter(listOfQueries);
        val endpoint = ReceiveName.from(contractName, "balanceOf");
        val result = this.client.invokeInstance(InvokeInstanceRequest.from(BlockQuery.LAST_FINAL, this.contractAddress, CCDAmount.from(0), endpoint, parameter, MAX_ENERGY));
        if (result.getOutcome() == Outcome.REJECT) {
            throw new RuntimeException("balanceOf failed: " + result.getRejectReason().toString());
        }
        val balances = SerializationUtils.deserializeTokenAmounts(result.getReturnValue());
        val responses = new HashMap<BalanceQuery, Long>();
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
        val result = this.client.invokeInstance(InvokeInstanceRequest.from(BlockQuery.LAST_FINAL, this.contractAddress, CCDAmount.from(0), endpoint, parameter, MAX_ENERGY));
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
     * @return A map where the values indicate the token metadata responses for each hex encoded token id.
     */
    public Map<byte[], TokenMetadata> tokenMetadata(byte[]... tokenIds) {
        val listOfQueries = Arrays.asList(tokenIds);
        val parameter = SerializationUtils.serializeTokenIds(listOfQueries);
        val endpoint = ReceiveName.from(contractName, "tokenMetadata");
        val result = this.client.invokeInstance(InvokeInstanceRequest.from(BlockQuery.LAST_FINAL, this.contractAddress, CCDAmount.from(0), endpoint, parameter, MAX_ENERGY));
        if (result.getOutcome() == Outcome.REJECT) {
            throw new RuntimeException("operatorOf failed: " + result.getRejectReason().toString());
        }
        val tokenMetadatas = SerializationUtils.deserializeTokenMetadatas(result.getReturnValue());
        val responses = new HashMap<byte[], TokenMetadata>();
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
    public List<Cis2EventWithMetadata> getEvents(BlockQuery from, BlockQuery to) {
        long current = this.client.getBlockInfo(from).getBlockHeight().getValue();
        val end = this.client.getBlockInfo(to).getBlockHeight().getValue();
        if (current >= end) {
            throw new IllegalArgumentException("Starting block must be before the end block");
        }
        val accumulator = new ArrayList<Cis2EventWithMetadata>();
        while (current <= end) {
            val events = getEventsFor(BlockQuery.HEIGHT(BlocksAtHeightRequest.newAbsolute(current)));
            accumulator.addAll(events);
            current++;
        }
        return accumulator;
    }

    /**
     * Get any events associated emitted from the specified CIS2 contract.
     *
     * @param queries blocks to query
     * @return the list of events.
     */
    public List<Cis2EventWithMetadata> getEventsFor(BlockQuery... queries) {
        val accumulator = new ArrayList<Cis2EventWithMetadata>();
        for (BlockQuery query : queries) {
            accumulator.addAll(getEventsFor(query));
        }
        return accumulator;
    }

    /**
     * Get any events associated emitted from the specified CIS2 contract by the
     * supplied transaction hash.
     *
     * @param transactionHash to query
     * @return the list of events which originated from the specified transaction hash.
     * Empty list if the transaction was not finalized.
     */
    public List<Cis2EventWithMetadata> getEventsForFinalizedTransaction(Hash transactionHash) {
        val status = this.client.getBlockItemStatus(transactionHash);
        if (!status.getFinalizedBlockItem().isPresent()) {
            return Collections.emptyList();
        }
        Hash blockHash = status.getFinalizedBlockItem().get().getBlockHash();
        return getEventsFor(BlockQuery.HASH(blockHash)).stream().filter(event -> transactionHash.equals(event.getTransactionHashOrigin())).collect(Collectors.toList());
    }


    /**
     * Get any events associated with the CIS2 contract that this client is instantiated with.
     *
     * @param blockQuery the block to query events for.
     * @return The list of events if there are any.
     */
    private List<Cis2EventWithMetadata> getEventsFor(BlockQuery blockQuery) {
        val accumulator = new ArrayList<Cis2EventWithMetadata>();
        val summaries = this.client.getBlockTransactionEvents(blockQuery);
        while (summaries.hasNext()) {
            val summary = summaries.next();
            if (summary.getDetails().getType() == Type.ACCOUNT_TRANSACTION) {
                val details = summary.getDetails().getAccountTransactionDetails();
                if (details.isSuccessful() && details.getType() == TransactionResultEventType.CONTRACT_UPDATED) {
                    val contractUpdated = details.getContractUpdated();
                    for (ContractTraceElement contractTraceElement : contractUpdated) {
                        if (contractTraceElement.getTraceType() == ContractTraceElementType.INSTANCE_UPDATED) {
                            val updatedEvent = (ContractUpdated) contractTraceElement;
                            if (this.contractAddress.equals(updatedEvent.getAddress())) {
                                for (byte[] event : updatedEvent.getEvents()) {
                                    accumulator.add(Cis2EventWithMetadata.ok(SerializationUtils.deserializeCis2Event(event), blockQuery, summary.getTransactionHash()));
                                }
                            }
                        }
                    }
                } else if (!Objects.isNull(details.getRejectReason())) {
                    accumulator.add(Cis2EventWithMetadata.err(Cis2Error.from(details.getRejectReason()), blockQuery, summary.getTransactionHash()));
                }
            }
        }
        return accumulator;
    }

}
