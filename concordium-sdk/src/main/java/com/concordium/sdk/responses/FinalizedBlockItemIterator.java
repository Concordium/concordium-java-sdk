package com.concordium.sdk.responses;

import com.concordium.sdk.ClientV2;
import com.concordium.sdk.responses.blocksatheight.BlocksAtHeightRequest;
import com.concordium.sdk.types.AbsoluteBlockHeight;
import com.concordium.sdk.types.UInt64;
import lombok.val;
import lombok.var;

import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * An {@link Iterator} for {@link BlockIdentifier}s of finalized blocks. Contains a background task that
 * polls for new finalized blocks indefinitely. This task can be stopped by calling {@link FinalizedBlockItemIterator#drop()}.
 */
public class FinalizedBlockItemIterator implements Iterator<BlockIdentifier> {

    private final BlockingQueue<BlockIdentifier> queue = new LinkedBlockingQueue<>(20);
    private final Thread producer;
    public FinalizedBlockItemIterator(ClientV2 client, AbsoluteBlockHeight startHeight) {
        producer = new Thread(new Producer(queue, client, startHeight));
        producer.start();
    }

    /**
     * Interrupts the underlying task polling for finalized blocks.
     */
    public void drop() {
        producer.interrupt();
    }
    @Override
    public boolean hasNext() {
        return true;
    }

    /**
     * Get the {@link BlockIdentifier} of the next finalized block.
     * This function blocks until a finalized block becomes available.
     * Use {@link FinalizedBlockItemIterator#next(long)} to only wait at most a specified duration.
     * @return {@link BlockIdentifier} of the next finalized block.
     */
    @Override
    public BlockIdentifier next() {
        try {
            return queue.take();
        } catch (InterruptedException e) {
            producer.interrupt();
            throw new RuntimeException(e);
        }
    }

    /**
     * Like {@link FinalizedBlockItemIterator#next()} but wait's at most the specified duration.
     * @param timeoutMillis amount of milliseconds to wait at most.
     * @return {@link BlockIdentifier} of the next finalized block or null if not available in time.
     */
    public BlockIdentifier next(long timeoutMillis) {
        try {
            return queue.poll(timeoutMillis, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            producer.interrupt();
            throw new RuntimeException(e);
        }
    }

    /**
     * Polls for finalized blocks indefinitely.
     */
    private static class Producer implements Runnable {

        private final BlockingQueue<BlockIdentifier> queue;
        private final ClientV2 client;
        private final AbsoluteBlockHeight startHeight;

        /**
         * Instantiates a new {@link Producer} polling for finalized blocks using the provided {@link ClientV2}, starting from the provided {@link AbsoluteBlockHeight} and putting them in the provided {@link BlockingQueue}.
         * @param queue the {@link BlockingQueue} to put the found blocks in.
         * @param client {@link ClientV2} to use for polling.
         * @param startHeight {@link AbsoluteBlockHeight} to start from.
         */
        Producer(BlockingQueue<BlockIdentifier> queue, ClientV2 client, AbsoluteBlockHeight startHeight) {
            this.queue = queue;
            this.client = client;
            this.startHeight = startHeight;
        }

        @Override
        public void run() {
            produce(startHeight);
        }

        private void produce(AbsoluteBlockHeight startHeight) {
            var finalHeight = client.getConsensusInfo().getLastFinalizedBlockHeight();
            var height = startHeight.getHeight().getValue();
            while (true) {
                if (height > finalHeight) {
                    finalHeight = client.getConsensusInfo().getLastFinalizedBlockHeight();
                    if (height > finalHeight) {
                        break;
                    }
                } else {
                    tryPutBlockAtHeight(height);
                    height = height + 1;
                }
            }
            val blockStream = client.getFinalizedBlocks();
            while (!Thread.currentThread().isInterrupted()) {
                val block = blockStream.next();
                // Recover missed blocks
                val blockHeight = block.getBlockHeight().getValue();
                while (height < blockHeight) {
                    tryPutBlockAtHeight(height);
                    height = height + 1;
                }
                tryPut(block);
                height = height + 1;
            }
        }

        private void tryPutBlockAtHeight(long height) {
            var blocks = client.getBlocksAtHeight(BlocksAtHeightRequest.newAbsolute(height));
            val blockHash = blocks.get(0);
            val info = BlockIdentifier.builder().blockHash(blockHash).blockHeight(UInt64.from(height)).build();
            tryPut(info);
        }

        private void tryPut(BlockIdentifier info) {
            try {
                queue.put(info);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

    }
}

