package org.dataManage;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;
import org.model.BlockData;
import org.model.TransactionData;

import java.math.BigInteger;
import java.util.List;

class DataFilterTest {

    @Test
    @DisplayName("Filter Blocks By minimum 6 Transactions")
    void filterBlocksByMinTransactions1() {
        // given
        List<BlockData> blocks1 = List.of(
                new BlockData(BigInteger.valueOf(8), "aah69bh5", 10),
                new BlockData(BigInteger.valueOf(4), "ioh781nd", 14),
                new BlockData(BigInteger.valueOf(6), "poe767b2", 5)
        );

        List<BlockData> expected = List.of(
                new BlockData(BigInteger.valueOf(4), "ioh781nd", 14),
                new BlockData(BigInteger.valueOf(8), "aah69bh5", 10)
        );

        DataFilter filter = new DataFilter();

        // when
        List<BlockData> result = filter.filterBlocksByMinTransactions(blocks1, 6);

        // then
        assertTrue(result.containsAll(expected) && expected.containsAll(result));
    }

    @Test
    @DisplayName("Filter Blocks By minimum 1 Transactions")
    void filterBlocksByMinTransactions2() {
        // given
        List<BlockData> blocks1 = List.of(
                new BlockData(BigInteger.valueOf(45), "aah69bh5", 10),
                new BlockData(BigInteger.valueOf(4), "opasgu32", 0),
                new BlockData(BigInteger.valueOf(120), "poe767b2", 1)
        );

        List<BlockData> expected = List.of(
                new BlockData(BigInteger.valueOf(45), "aah69bh5", 10),
                new BlockData(BigInteger.valueOf(120), "poe767b2", 1)
        );

        DataFilter filter = new DataFilter();

        // when
        List<BlockData> result = filter.filterBlocksByMinTransactions(blocks1, 1);

        // then
        assertTrue(result.containsAll(expected) && expected.containsAll(result));
    }

    @Test
    @DisplayName("Filter Transactions By Min ETH Value 1.0")
    void filterByMinValue() {
        // given
        List<TransactionData> transactions = List.of(
                new TransactionData("tx1", "0xAAA", "0xBBB", new BigInteger("2500000000000000000"), BigInteger.valueOf(21000)), // 2.5 ETH
                new TransactionData("tx2", "0xAAA", "0xBBB", new BigInteger("500000000000000000"),  BigInteger.valueOf(21000)), // 0.5 ETH
                new TransactionData("tx3", "0xAAA", "0xBBB", new BigInteger("1000000000000000000"), BigInteger.valueOf(21000)), // 1.0 ETH
                new TransactionData("tx4", "0xAAA", "0xBBB", new BigInteger("100000000000000000"),  BigInteger.valueOf(21000))  // 0.1 ETH
        );

        List<TransactionData> expected = List.of(
                new TransactionData("tx1", "0xAAA", "0xBBB", new BigInteger("2500000000000000000"), BigInteger.valueOf(21000)),
                new TransactionData("tx3", "0xAAA", "0xBBB", new BigInteger("1000000000000000000"), BigInteger.valueOf(21000))
        );

        DataFilter filter = new DataFilter();

        // when
        List<TransactionData> result = filter.filterByMinValue(transactions, 1.0);

        // then
        assertTrue(result.containsAll(expected) && expected.containsAll(result));
    }

    @Test
    @DisplayName("Filter Transactions By Sender Address 0x1234abcd")
    void filterBySender() {
        // given
        String targetSender = "0x1234abcd";
        String otherSender  = "0x5678efgh";

        List<TransactionData> transactions = List.of(
                new TransactionData("tx1", targetSender, "0xRECV1", BigInteger.valueOf(1000), BigInteger.valueOf(21000)),
                new TransactionData("tx2", otherSender,  "0xRECV2", BigInteger.valueOf(2000), BigInteger.valueOf(21000)),
                new TransactionData("tx3", targetSender, "0xRECV3", BigInteger.valueOf(3000), BigInteger.valueOf(21000)),
                new TransactionData("tx4", "0x9999aaaa", "0xRECV4", BigInteger.valueOf(4000), BigInteger.valueOf(21000))
        );

        List<TransactionData> expected = List.of(
                new TransactionData("tx1", targetSender, "0xRECV1", BigInteger.valueOf(1000), BigInteger.valueOf(21000)),
                new TransactionData("tx3", targetSender, "0xRECV3", BigInteger.valueOf(3000), BigInteger.valueOf(21000))
        );

        DataFilter filter = new DataFilter();

        // when
        List<TransactionData> result = filter.filterBySender(transactions, targetSender);

        // then
        assertTrue(result.containsAll(expected) && expected.containsAll(result));
    }

    @Test
    @DisplayName("Filter Transactions By Recipient Address 0xRECV123")
    void filterByRecipient() {
        // given
        String targetRecipient = "0xRECV123";
        String otherRecipient  = "0xRECV456";

        List<TransactionData> transactions = List.of(
                new TransactionData("tx1", "0xAAA", targetRecipient, BigInteger.valueOf(1000), BigInteger.valueOf(21000)),
                new TransactionData("tx2", "0xBBB", otherRecipient,  BigInteger.valueOf(2000), BigInteger.valueOf(21000)),
                new TransactionData("tx3", "0xCCC", targetRecipient, BigInteger.valueOf(3000), BigInteger.valueOf(21000)),
                new TransactionData("tx4", "0xDDD", null, BigInteger.valueOf(4000), BigInteger.valueOf(21000))
        );

        List<TransactionData> expected = List.of(
                new TransactionData("tx1", "0xAAA", targetRecipient, BigInteger.valueOf(1000), BigInteger.valueOf(21000)),
                new TransactionData("tx3", "0xCCC", targetRecipient, BigInteger.valueOf(3000), BigInteger.valueOf(21000))
        );

        DataFilter filter = new DataFilter();

        // when
        List<TransactionData> result = filter.filterByRecipient(transactions, targetRecipient);

        // then
        assertTrue(result.containsAll(expected) && expected.containsAll(result));
    }

    @Test
    @DisplayName("Filter Transactions By Max Gas Used below 50000L")
    void filterByMaxGas() {
        // given
        long maxGasThreshold = 50000L;

        List<TransactionData> transactions = List.of(
                new TransactionData("tx1", "0xAAA", "0xBBB", BigInteger.valueOf(1000), BigInteger.valueOf(21000)),  // 21k <= 50k -> PASS
                new TransactionData("tx2", "0xCCC", "0xDDD", BigInteger.valueOf(2000), BigInteger.valueOf(65000)),  // 65k > 50k  -> FAIL
                new TransactionData("tx3", "0xEEE", "0xFFF", BigInteger.valueOf(3000), BigInteger.valueOf(50000)),  // 50k <= 50k -> PASS
                new TransactionData("tx4", "0xGGG", "0xHHH", BigInteger.valueOf(4000), BigInteger.valueOf(120000))  // 120k > 50k -> FAIL
        );

        List<TransactionData> expected = List.of(
                new TransactionData("tx1", "0xAAA", "0xBBB", BigInteger.valueOf(1000), BigInteger.valueOf(21000)),
                new TransactionData("tx3", "0xEEE", "0xFFF", BigInteger.valueOf(3000), BigInteger.valueOf(50000))
        );

        DataFilter filter = new DataFilter();

        // when
        List<TransactionData> result = filter.filterByMaxGas(transactions, maxGasThreshold);

        // then
        assertTrue(result.containsAll(expected) && expected.containsAll(result));
    }

    @Test
    @DisplayName("Take Newest Blocks")
    void takeNewest() {
        // given
        List<BlockData> blocks = List.of(
                new BlockData(BigInteger.valueOf(10), "hash10", 5),
                new BlockData(BigInteger.valueOf(50), "hash50", 10),
                new BlockData(BigInteger.valueOf(30), "hash30", 7),
                new BlockData(BigInteger.valueOf(20), "hash20", 3),
                new BlockData(BigInteger.valueOf(40), "hash40", 8)
        );
        int n = 3;

        List<BlockData> expected = List.of(
                new BlockData(BigInteger.valueOf(10), "hash10", 5),
                new BlockData(BigInteger.valueOf(50), "hash50", 10),
                new BlockData(BigInteger.valueOf(30), "hash30", 7)
        );

        DataFilter filter = new DataFilter();

        // when
        List<BlockData> result = filter.takeNewest(blocks, n);

        // then
        assertTrue(result.containsAll(expected) && expected.containsAll(result));
    }
}