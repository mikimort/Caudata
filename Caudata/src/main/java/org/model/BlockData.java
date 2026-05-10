package org.model;
import java.math.BigInteger;

public class BlockData
{
    private final BigInteger blockNumber;
    private final String blockHash;
    private final int transactionCount;

    public BlockData(BigInteger blockNumber, String blockHash, int transactionCount)
    {
        this.blockNumber = blockNumber;
        this.blockHash = blockHash;
        this.transactionCount = transactionCount;
    }

    public BigInteger getBlockNumber()
    {
        return blockNumber;
    }
    public String getBlockHash()
    {
        return blockHash;
    }
    public int getTransactionCount()
    {
        return transactionCount;
    }

    @Override
    public String toString()
    {
        return String.format("[Blok #%s] Hash: %s | Liczba Tx: %d", blockNumber, blockHash, transactionCount);
    }
}
