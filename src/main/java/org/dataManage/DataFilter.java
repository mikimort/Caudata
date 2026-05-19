package org.dataManage;

import org.model.BlockData;
import org.model.TransactionData;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Klasa narzędziowa do filtrowania list bloków i transakcji.
 * Wszystkie metody są bezstanowe i nie modyfikują wejściowych kolekcji
 * - zwracają nowe listy.
 */
public class DataFilter {
    /**
     * Zwraca bloki z liczbą transakcji >= minTxCount.
     */
    public List<BlockData> filterBlocksByMinTransactions(List<BlockData> blocks, int minTxCount) {
        return blocks.stream().filter(b -> b.getTransactionCount() >= minTxCount).collect(Collectors.toList());
    }

    /**
     * Filtruje transakcje o wartości >= minEth ETH.
     */
    public List<TransactionData> filterByMinValue(List<TransactionData> txs, double minEth) {
        BigInteger minWei = BigInteger.valueOf((long) (minEth * 1e18));
        return txs.stream().filter(tx -> tx.getValueWei().compareTo(minWei) >= 0).collect(Collectors.toList());
    }

    /**
     * Filtruje transakcje według adresu nadawcy (case-insensitive).
     */
    public List<TransactionData> filterBySender(List<TransactionData> txs, String address) {
        return txs.stream().filter(tx -> address.equalsIgnoreCase(tx.getFrom())).collect(Collectors.toList());
    }

    /**
     * Filtruje transakcje według adresu odbiorcy (pomija null).
     */
    public List<TransactionData> filterByRecipient(List<TransactionData> txs, String address) {
        return txs.stream().filter(tx -> tx.getTo() != null && address.equalsIgnoreCase(tx.getTo())).collect(Collectors.toList());
    }

    /**
     * Filtruje transakcje z gasUsed <= maxGas.
     */
    public List<TransactionData> filterByMaxGas(List<TransactionData> txs, long maxGas) {
        return txs.stream().filter(tx -> tx.getGasUsed().longValue() <= maxGas).collect(Collectors.toList());
    }

    /**
     * Zwraca n pierwszych elementów listy (najnowsze bloki).
     */
    public List<BlockData> takeNewest(List<BlockData> blocks, int n) {
        return blocks.stream().limit(n).collect(Collectors.toList());
    }

}
