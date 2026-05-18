package org.model;
import java.math.BigInteger;
import java.util.Objects;

public class BlockData
{
    private final BigInteger blockNumber;
    private final String blockHash;
    private final int transactionCount;
    private BigInteger timestamp;

    public BlockData(BigInteger blockNumber, String blockHash, int transactionCount, BigInteger timestamp)
    {
        this.blockNumber = blockNumber;
        this.blockHash = blockHash;
        this.transactionCount = transactionCount;
        this.timestamp = timestamp;
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
    public BigInteger getTimestamp()
    {
        return timestamp;
    }

    @Override
    public String toString()
    {
        return String.format("[Blok #%s] Hash: %s | Liczba Tx: %d", blockNumber, blockHash, transactionCount, timestamp);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BlockData blockData = (BlockData) o;
        return transactionCount == blockData.transactionCount &&
                Objects.equals(blockNumber, blockData.blockNumber) &&
                Objects.equals(blockHash, blockData.blockHash);
    }

    @Override
    public int hashCode() {
        return Objects.hash(blockNumber, blockHash, transactionCount);
    }

}
