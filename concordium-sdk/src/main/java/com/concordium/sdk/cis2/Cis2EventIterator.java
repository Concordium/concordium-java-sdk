package com.concordium.sdk.cis2;

import com.concordium.sdk.cis2.events.Cis2EventWithMetadata;
import com.concordium.sdk.requests.BlockQuery;
import com.concordium.sdk.responses.blockitemsummary.AccountTransactionDetails;
import com.concordium.sdk.responses.blockitemsummary.Summary;
import com.concordium.sdk.responses.blockitemsummary.Type;
import com.concordium.sdk.responses.smartcontracts.ContractTraceElementType;
import com.concordium.sdk.responses.transactionstatus.*;
import com.concordium.sdk.transactions.Hash;
import lombok.val;

import java.util.*;

class Cis2EventIterator implements Iterator<Cis2EventWithMetadata> {

    private final Cis2Client client;

    private final Iterator<BlockQuery> queries;

    private final Queue<Cis2EventWithMetadata> buffer = new LinkedList<>();

    Cis2EventIterator(Cis2Client client, Iterator<BlockQuery> queries) {
        this.client = client;
        this.queries = queries;
    }

    @Override
    public boolean hasNext() {
        if (!queries.hasNext() && buffer.isEmpty()) return false;
        val query = queries.next();
        tryAddEvents(query);
        if (!buffer.isEmpty()) {
            return true;
        } else {
            // deplete the queries buffer until we fill up the events buffer or there are no
            // more queries left.
            while (queries.hasNext()) {
                tryAddEvents(queries.next());
                if (!buffer.isEmpty()) return true;
            }
            return false;
        }
    }

    @Override
    public Cis2EventWithMetadata next() {
        return buffer.remove();
    }

    private void tryAddEvents(BlockQuery query) {
        val blockEvents = client.getClient().getBlockTransactionEvents(query);
        while (blockEvents.hasNext()) {
            buffer.addAll(extractCis2Events(query, blockEvents.next()));
        }
    }

    /**
     * Extract any events from the specified contract.
     * The events are added to the supplied accumulator.
     *
     * @param blockQuery a block identifier.
     * @param summary    the transaction summary to extract from.
     * @return list of cis2 events emitted from the contract in the block specified.
     */
    private List<Cis2EventWithMetadata> extractCis2Events(BlockQuery blockQuery, Summary summary) {
        val accumulator = new ArrayList<Cis2EventWithMetadata>();
        if (summary.getDetails().getType() == Type.ACCOUNT_TRANSACTION) {
            val details = summary.getDetails().getAccountTransactionDetails();
            if (details.isSuccessful()) {
                accumulator.addAll(getSuccessEvents(details, blockQuery, summary.getTransactionHash()));
            } else {
                val rejectReason = details.getRejectReason();
                if (rejectReason.getType() == RejectReasonType.REJECTED_RECEIVE) {
                    val rejectReceive = (RejectReasonRejectedReceive) rejectReason;
                    if (this.client.getContractAddress().equals(rejectReceive.getContractAddress())) {
                        accumulator.add(Cis2EventWithMetadata.err(Cis2Error.from(rejectReceive.getRejectReason()), blockQuery, summary.getTransactionHash()));
                    }
                }
            }
        }
        return accumulator;
    }

    /**
     * Parse events from the contract specified that originated from a successfully executed transaction.
     * The events can either origin from a contract update or a contract initialization.
     *
     * @param details         the account transaction details
     * @param blockQuery      the block identifier
     * @param transactionHash the origin transaction
     * @return list of parsed events.
     */
    private List<Cis2EventWithMetadata> getSuccessEvents(AccountTransactionDetails details, BlockQuery blockQuery, Hash transactionHash) {
        val accumulator = new ArrayList<Cis2EventWithMetadata>();
        val eventType = details.getType();
        if (eventType == TransactionResultEventType.CONTRACT_UPDATED) {
            val contractUpdatedEvents = details.getContractUpdated();
            for (val e : contractUpdatedEvents) {
                if (e.getTraceType() == ContractTraceElementType.INSTANCE_UPDATED) {
                    val updatedEvent = (ContractUpdated) e;
                    if (this.client.getContractAddress().equals(updatedEvent.getAddress())) {
                        for (val rawUpdateEvent : updatedEvent.getEvents()) {
                            accumulator.add(Cis2EventWithMetadata.ok(SerializationUtils.deserializeCis2Event(rawUpdateEvent), blockQuery, transactionHash));
                        }
                    }
                }
            }
        } else if (eventType == TransactionResultEventType.CONTRACT_INITIALIZED) {
            val contractInitialized = details.getContractInitialized();
            if (this.client.getContractAddress().equals(contractInitialized.getAddress())) {
                for (val rawInitializeEvent : contractInitialized.getEvents()) {
                    accumulator.add(Cis2EventWithMetadata.ok(SerializationUtils.deserializeCis2Event(rawInitializeEvent), blockQuery, transactionHash));
                }
            }
        }
        return accumulator;
    }
}
