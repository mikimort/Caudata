package org.dataManage;


import org.model.BlockData;
import org.model.TransactionData;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.List;

public class DataAggregator
{
    public int totalBlocks(List<BlockData> blocks)
    {
        return blocks.size();
    }

    public int totalTransactions(List<TransactionData> txs)
    {
        return txs.size();
    }

    public double averageTxPerBlock(List<BlockData> blocks)
    {
        if (blocks.isEmpty()) return 0;
        return blocks.stream().mapToInt(BlockData::getTransactionCount).average().orElse(0);
    }

    public double totalValueEth(List<TransactionData> txs)
    {
        return txs.stream().mapToDouble(TransactionData::getValueEth).sum();
    }

    public BigInteger averageGasUsed(List<TransactionData> txs)
    {
        if(txs.isEmpty()) return BigInteger.ZERO;
        BigInteger total = txs.stream().map(TransactionData::getGasUsed).reduce(BigInteger.ZERO, BigInteger::add);
        return total.divide(BigInteger.valueOf(txs.size()));
    }


}
