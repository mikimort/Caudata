package org.dataManage;

import org.model.BlockData;
import org.model.Stats;
import org.model.TransactionData;

import java.math.BigInteger;
import java.util.List;

/**
 * Assembles a Stats object from raw block and transaction lists.
 * Delegates all calculations to DataAggregator to avoid duplication.
 */
public class StatsBuilder {

    private static final int LATEST_TX_LIMIT = 10;
    private final DataAggregator aggregator;

    public StatsBuilder() {
        this.aggregator = new DataAggregator();
    }

    /** Allows injecting a custom/mock aggregator (useful for testing). */
    public StatsBuilder(DataAggregator aggregator) {
        this.aggregator = aggregator;
    }

    public Stats build(List<BlockData> blocks, List<TransactionData> txs) {
        BigInteger latestBlock = blocks.isEmpty()
                ? BigInteger.ZERO
                : blocks.get(0).getBlockNumber();

        int blocksCount = aggregator.totalBlocks(blocks);
        int txCount     = aggregator.totalTransactions(txs);

        double avgTxPerBlock = aggregator.averageTxPerBlockFromCount(txCount, blocksCount);
        double totalEth      = aggregator.totalValueEth(txs);
        BigInteger avgGas    = aggregator.averageGasUsed(txs);

        List<TransactionData> latestTxs = txs.stream()
                .limit(LATEST_TX_LIMIT)
                .toList();

        return new Stats(latestBlock, blocksCount, txCount, avgTxPerBlock, totalEth, avgGas, latestTxs);
    }
}