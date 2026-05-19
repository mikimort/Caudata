package org.dataManage;

import org.model.BlockData;
import org.model.TransactionData;

import java.math.BigInteger;
import java.util.List;

/**
 * Klasa narzędziowa do obliczania statystyk zbiorczych. Wszystkie metody są bezstanowe.
 */
public class DataAggregator {
    /**
     * Zwraca łączną liczbę bloków.
     */
    public int totalBlocks(List<BlockData> blocks) {
        return blocks.size();
    }

    /**
     * Zwraca łączną liczbę transakcji.
     */
    public int totalTransactions(List<TransactionData> txs) {
        return txs.size();
    }

    /**
     * Oblicza średnią liczbę transakcji na blok.
     */
    public double averageTxPerBlock(List<BlockData> blocks) {
        if (blocks.isEmpty()) return 0;
        return blocks.stream().mapToInt(BlockData::getTransactionCount).average().orElse(0);
    }

    /**
     * Sumuje wartości wszystkich transakcji w ETH.
     */
    public double totalValueEth(List<TransactionData> txs) {
        return txs.stream().mapToDouble(TransactionData::getValueEth).sum();
    }

    /**
     * Oblicza średnie zużycie gazu na transakcję.
     */
    public BigInteger averageGasUsed(List<TransactionData> txs) {
        if (txs.isEmpty()) return BigInteger.ZERO;
        BigInteger total = txs.stream().map(TransactionData::getGasUsed).reduce(BigInteger.ZERO, BigInteger::add);
        return total.divide(BigInteger.valueOf(txs.size()));
    }

    public double averageTxPerBlockFromCount(int txCount, int blockCount) {
        return blockCount == 0 ? 0.0 : (double) txCount / blockCount;
    }
}
