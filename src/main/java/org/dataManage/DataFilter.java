package org.dataManage;

import org.model.BlockData;
import org.model.TransactionData;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

public class DataFilter
{
    public List<BlockData> filterBlocksByMinTransactions(List<BlockData> blocks, int minTxCount)
    {
        return blocks.stream().filter(b -> b.getTransactionCount() >= minTxCount).collect(Collectors.toList());
    }

    public List<TransactionData> filterByMinValue(List<TransactionData> txs, double minEth)
    {
        BigInteger minWei = BigInteger.valueOf((long)(minEth * 1e18));
        return txs.stream().filter(tx -> tx.getValueWei().compareTo(minWei) >= 0).collect(Collectors.toList());
    }

    public List<TransactionData> filterBySender(List<TransactionData> txs, String address)
    {
        return txs.stream().filter(tx -> address.equalsIgnoreCase(tx.getFrom())).collect(Collectors.toList());
    }

    public List<TransactionData> filterByRecipient(List<TransactionData> txs, String address)
    {
        return txs.stream().filter(tx -> tx.getTo() != null && address.equalsIgnoreCase(tx.getTo())).collect(Collectors.toList());
    }

    public List<TransactionData> filterByMaxGas(List<TransactionData> txs, long maxGas)
    {
        return txs.stream().filter(tx -> tx.getGasUsed().longValue() <= maxGas).collect(Collectors.toList());
    }

    public List<BlockData> takeNewest(List<BlockData> blocks, int n)
    {
        return blocks.stream().limit(n).collect(Collectors.toList());
    }

}
