package dataManage;

import org.dataManage.DataAggregator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.model.BlockData;
import org.model.TransactionData;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DataAggregatorTest {

    // ---------------------------------------------
    //  totalBlocks
    // ---------------------------------------------

    @Test
    @DisplayName("totalBlocks – returns 0 for an empty list")
    void totalBlocks_emptyList()
    {
        DataAggregator agg = new DataAggregator();
        assertEquals(0, agg.totalBlocks(Collections.emptyList()));
    }

    @Test
    @DisplayName("totalBlocks – returns the correct number of blocks")
    void totalBlocks_nonEmpty()
    {
        List<BlockData> blocks = List.of(
                new BlockData(BigInteger.ONE, "hash1", 3, BigInteger.valueOf(1000)),
                new BlockData(BigInteger.TWO, "hash2", 7, BigInteger.valueOf(2000)),
                new BlockData(BigInteger.valueOf(3), "hash3", 1, BigInteger.valueOf(3000))
        );
        DataAggregator agg = new DataAggregator();
        assertEquals(3, agg.totalBlocks(blocks));
    }

    // ---------------------------------------------
    //  totalTransactions
    // ---------------------------------------------

    @Test
    @DisplayName("totalTransactions – returns 0 for an empty list")
    void totalTransactions_emptyList()
    {
        DataAggregator agg = new DataAggregator();
        assertEquals(0, agg.totalTransactions(Collections.emptyList()));
    }

    @Test
    @DisplayName("totalTransactions – returns the correct number of transactions")
    void totalTransactions_nonEmpty()
    {
        List<TransactionData> txs = List.of(
                new TransactionData("tx1", "0xAAA", "0xBBB", BigInteger.valueOf(1000), BigInteger.valueOf(21000)),
                new TransactionData("tx2", "0xCCC", "0xDDD", BigInteger.valueOf(2000), BigInteger.valueOf(21000))
        );
        DataAggregator agg = new DataAggregator();
        assertEquals(2, agg.totalTransactions(txs));
    }

    // ---------------------------------------------
    //  averageTxPerBlock
    // ---------------------------------------------

    @Test
    @DisplayName("averageTxPerBlock – returns 0.0 for an empty list")
    void averageTxPerBlock_emptyList()
    {
        DataAggregator agg = new DataAggregator();
        assertEquals(0.0, agg.averageTxPerBlock(Collections.emptyList()), 1e-9);
    }

    @Test
    @DisplayName("averageTxPerBlock – single block, average equals its transaction count")
    void averageTxPerBlock_singleBlock()
    {
        List<BlockData> blocks = List.of(
                new BlockData(BigInteger.ONE, "hashA", 7, BigInteger.valueOf(1000))
        );
        DataAggregator agg = new DataAggregator();
        assertEquals(7.0, agg.averageTxPerBlock(blocks), 1e-9);
    }

    @Test
    @DisplayName("averageTxPerBlock – returns the correct average for multiple blocks")
    void averageTxPerBlock_multipleBlocks()
    {
        List<BlockData> blocks = List.of(
                new BlockData(BigInteger.valueOf(1), "h1", 10, BigInteger.valueOf(1000)),
                new BlockData(BigInteger.valueOf(2), "h2", 0,  BigInteger.valueOf(2000)),
                new BlockData(BigInteger.valueOf(3), "h3", 5,  BigInteger.valueOf(3000))
        );
        DataAggregator agg = new DataAggregator();
        assertEquals(5.0, agg.averageTxPerBlock(blocks), 1e-9);
    }

    @Test
    @DisplayName("averageTxPerBlock – fractional result (e.g. 10/3)")
    void averageTxPerBlock_fractionalResult()
    {
        List<BlockData> blocks = List.of(
                new BlockData(BigInteger.valueOf(1), "h1", 3, BigInteger.valueOf(1000)),
                new BlockData(BigInteger.valueOf(2), "h2", 3, BigInteger.valueOf(2000)),
                new BlockData(BigInteger.valueOf(3), "h3", 4, BigInteger.valueOf(3000))
        );
        DataAggregator agg = new DataAggregator();
        assertEquals(10.0 / 3.0, agg.averageTxPerBlock(blocks), 1e-9);
    }

    // ---------------------------------------------
    //  totalValueEth
    // ---------------------------------------------

    @Test
    @DisplayName("totalValueEth – returns 0.0 for an empty list")
    void totalValueEth_emptyList()
    {
        DataAggregator agg = new DataAggregator();
        assertEquals(0.0, agg.totalValueEth(Collections.emptyList()), 1e-9);
    }

    @Test
    @DisplayName("totalValueEth – returns the correct total ETH value")
    void totalValueEth_correctSum()
    {
        List<TransactionData> txs = List.of(
                new TransactionData("tx1", "0xA", "0xB", new BigInteger("1500000000000000000"), BigInteger.valueOf(21000)),
                new TransactionData("tx2", "0xC", "0xD", new BigInteger("500000000000000000"),  BigInteger.valueOf(21000)),
                new TransactionData("tx3", "0xE", "0xF", new BigInteger("2000000000000000000"), BigInteger.valueOf(21000))
        );
        DataAggregator agg = new DataAggregator();
        assertEquals(4.0, agg.totalValueEth(txs), 1e-9);
    }

    @Test
    @DisplayName("totalValueEth – single transaction with 0 ETH value")
    void totalValueEth_zeroValue()
    {
        List<TransactionData> txs = List.of(
                new TransactionData("tx1", "0xA", "0xB", BigInteger.ZERO, BigInteger.valueOf(21000))
        );
        DataAggregator agg = new DataAggregator();
        assertEquals(0.0, agg.totalValueEth(txs), 1e-9);
    }

    // ---------------------------------------------
    //  averageGasUsed
    // ---------------------------------------------

    @Test
    @DisplayName("averageGasUsed – returns BigInteger.ZERO for an empty list")
    void averageGasUsed_emptyList()
    {
        DataAggregator agg = new DataAggregator();
        assertEquals(BigInteger.ZERO, agg.averageGasUsed(Collections.emptyList()));
    }

    @Test
    @DisplayName("averageGasUsed – single transaction, average equals its gas usage")
    void averageGasUsed_singleTx()
    {
        List<TransactionData> txs = List.of(
                new TransactionData("tx1", "0xA", "0xB", BigInteger.valueOf(1000), BigInteger.valueOf(42000)));
        DataAggregator agg = new DataAggregator();
        assertEquals(BigInteger.valueOf(42000), agg.averageGasUsed(txs));
    }

    @Test
    @DisplayName("averageGasUsed – returns the correct average for multiple transactions")
    void averageGasUsed_multipleTxs()
    {
        List<TransactionData> txs = List.of(
                new TransactionData("tx1", "0xA", "0xB", BigInteger.valueOf(1000), BigInteger.valueOf(21000)),
                new TransactionData("tx2", "0xC", "0xD", BigInteger.valueOf(2000), BigInteger.valueOf(63000)),
                new TransactionData("tx3", "0xE", "0xF", BigInteger.valueOf(3000), BigInteger.valueOf(42000)));
        DataAggregator agg = new DataAggregator();
        assertEquals(BigInteger.valueOf(42000), agg.averageGasUsed(txs));
    }

    @Test
    @DisplayName("averageGasUsed – result rounded down (integer division)")
    void averageGasUsed_integerDivision()
    {
        List<TransactionData> txs = List.of(
                new TransactionData("tx1", "0xA", "0xB", BigInteger.valueOf(1000), BigInteger.valueOf(21000)),
                new TransactionData("tx2", "0xC", "0xD", BigInteger.valueOf(2000), BigInteger.valueOf(21001)));
        DataAggregator agg = new DataAggregator();
        assertEquals(BigInteger.valueOf(21000), agg.averageGasUsed(txs));
    }
}