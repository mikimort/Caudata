package org.model;

import java.math.BigInteger;
import java.util.List;

/**
 * Obiekt przechowujący zagregowane statystyki
 * dla zestawu bloków i transakcji z sieci Ethereum Sepolia.
 *
 * Tworzony przez StatsBuilder i przekazywany do warstwy UI
 * Dashboard w celu wyświetlenia.
 */

public class Stats {

    private final BigInteger latestBlock;
    private final int blocksCount;
    private final int transactionsCount;

    private final double avgTxPerBlock;

    private final double totalEth;
    private final BigInteger avgGasUsed;

    private final List<TransactionData> latestTransactions;

    public Stats(
            BigInteger latestBlock,
            int blocksCount,
            int transactionsCount,
            double avgTxPerBlock,
            double totalEth,
            BigInteger avgGasUsed,
            List<TransactionData> latestTransactions
    ) {
        this.latestBlock = latestBlock;
        this.blocksCount = blocksCount;
        this.transactionsCount = transactionsCount;
        this.avgTxPerBlock = avgTxPerBlock;
        this.totalEth = totalEth;
        this.avgGasUsed = avgGasUsed;
        this.latestTransactions = latestTransactions;
    }



    public BigInteger getLatestBlock() {
        return latestBlock;
    }

    public int getBlocksCount() {
        return blocksCount;
    }

    public int getTransactionsCount() {
        return transactionsCount;
    }

    public double getAvgTxPerBlock() {
        return avgTxPerBlock;
    }

    public double getTotalEth() {
        return totalEth;
    }

    public BigInteger getAvgGasUsed() {
        return avgGasUsed;
    }

    public List<TransactionData> getLatestTransactions() {
        return latestTransactions;
    }
}