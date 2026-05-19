package org.dataManage;

import org.model.BlockData;
import org.model.Stats;
import org.model.TransactionData;

import java.math.BigInteger;
import java.util.List;

/**
 * Buduje obiekt Stats na podstawie list bloków i transakcji.
 * Obliczenia delegowane są do DataAggregator, żeby uniknąć duplikacji logiki.
 * StatsBuilder odpowiada tylko za zebranie wyników i złożenie ich w jeden obiekt Stats.
 * <p>
 * Przykład użycia:
 * StatsBuilder builder = new StatsBuilder();
 * Stats stats = builder.build(blocks, transactions);
 */

public class StatsBuilder {

    private static final int LATEST_TX_LIMIT = 10;
    private final DataAggregator aggregator;

    public StatsBuilder() {
        this.aggregator = new DataAggregator();
    }

    public StatsBuilder(DataAggregator aggregator) {
        this.aggregator = aggregator;
    }

    public Stats build(List<BlockData> blocks, List<TransactionData> txs) {
        BigInteger latestBlock = blocks.isEmpty()
                ? BigInteger.ZERO
                : blocks.get(0).getBlockNumber();

        int blocksCount = aggregator.totalBlocks(blocks);
        int txCount = aggregator.totalTransactions(txs);

        double avgTxPerBlock = aggregator.averageTxPerBlockFromCount(txCount, blocksCount);
        double totalEth = aggregator.totalValueEth(txs);
        BigInteger avgGas = aggregator.averageGasUsed(txs);

        List<TransactionData> latestTxs = txs.stream()
                .limit(LATEST_TX_LIMIT)
                .toList();

        return new Stats(latestBlock, blocksCount, txCount, avgTxPerBlock, totalEth, avgGas, latestTxs);
    }
}