package com.concordium.sdk.cis2;

import com.concordium.sdk.cis2.events.Cis2EventWithMetadata;
import com.concordium.sdk.requests.BlockQuery;
import com.concordium.sdk.responses.blockitemsummary.Summary;
import com.concordium.sdk.responses.blockitemsummary.Type;
import com.concordium.sdk.responses.smartcontracts.ContractTraceElement;
import com.concordium.sdk.responses.smartcontracts.ContractTraceElementType;
import com.concordium.sdk.responses.transactionstatus.ContractUpdated;
import com.concordium.sdk.responses.transactionstatus.RejectReasonType;
import com.concordium.sdk.responses.transactionstatus.TransactionResultEventType;
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
        return queries.hasNext() || !buffer.isEmpty();
    }

    @Override
    public Cis2EventWithMetadata next() {
        if (!buffer.isEmpty()) return buffer.remove(); // deplete the buffer of events before querying again.
        if (!queries.hasNext()) throw new NoSuchElementException("Queries depleted");
        val query = queries.next();
        tryAddEvents(query);
        if (!buffer.isEmpty()) {
            return buffer.remove();
        } else {
            while (queries.hasNext()) {
                tryAddEvents(queries.next());
                if (!buffer.isEmpty()) return buffer.remove();
            }
            throw new NoSuchElementException("Buffer and queries depleted");
        }
    }

    private void tryAddEvents(BlockQuery query) {
        val blockEvents = client.getClient().getBlockTransactionEvents(query);
        while (blockEvents.hasNext()) {
            buffer.addAll(extractCis2Events(query, blockEvents.next()));
        }
    }

    /**
     * Extract any events from the CIS2 specified contract.
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
            val eventType = details.getType();
            val isRelevantEventType = eventType == TransactionResultEventType.CONTRACT_UPDATED
                    || eventType == TransactionResultEventType.CONTRACT_INITIALIZED;
            if (details.isSuccessful()
                    && isRelevantEventType) {
                val contractUpdated = details.getContractUpdated();
                for (val contractTraceElement : contractUpdated) {
                    if (contractTraceElement.getTraceType() == ContractTraceElementType.INSTANCE_UPDATED) {
                        val updatedEvent = (ContractUpdated) contractTraceElement;
                        if (this.client.getContractAddress().equals(updatedEvent.getAddress())) {
                            for (val event : updatedEvent.getEvents()) {
                                accumulator.add(Cis2EventWithMetadata.ok(SerializationUtils.deserializeCis2Event(event), blockQuery, summary.getTransactionHash()));
                            }
                        }
                    }
                }
            } else if (!Objects.isNull(details.getRejectReason())) {
                if (details.getRejectReason().getType() == RejectReasonType.REJECTED_RECEIVE || details.getRejectReason().getType() == RejectReasonType.REJECTED_INIT) {
                    accumulator.add(Cis2EventWithMetadata.err(Cis2Error.from(details.getRejectReason()), blockQuery, summary.getTransactionHash()));
                }
            }
        }
        return accumulator;
    }
}
