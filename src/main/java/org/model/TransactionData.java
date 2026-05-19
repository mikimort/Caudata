package org.model;

import java.math.BigInteger;
import java.util.Objects;

/**
 * Niemodyfikowalny model danych reprezentujący jedną transakcję Ethereum.
 */
public class TransactionData {
    private final String txHash;
    private final String from;
    private final String to;
    private final BigInteger valueWei;
    private final BigInteger gasUsed;

    public TransactionData(String txHash, String from, String to, BigInteger valueWei, BigInteger gasUsed) {
        this.txHash = txHash;
        this.from = from;
        this.to = to;
        this.valueWei = valueWei;
        this.gasUsed = gasUsed;
    }

    public String getTxHash() {
        return txHash;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public BigInteger getValueWei() {
        return valueWei;
    }

    public BigInteger getGasUsed() {
        return gasUsed;
    }

    /**
     * Przelicza wartość z Wei na ETH (dzieli przez 1e18).
     */
    public double getValueEth() {
        return valueWei.doubleValue() / 1e18;
    }

    @Override
    public String toString() {
        return String.format("   Tx: %s%n    Od: %s%n     Do:    %s%n     Wartość: %.6f Eth   | Gas: %s", txHash, from, to != null ? to : "(tworzenie kontraktu)", getValueEth(), gasUsed);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransactionData that = (TransactionData) o;
        return Objects.equals(txHash, that.txHash) &&
                Objects.equals(from, that.from) &&
                Objects.equals(to, that.to) &&
                Objects.equals(valueWei, that.valueWei) &&
                Objects.equals(gasUsed, that.gasUsed);
    }

    @Override
    public int hashCode() {
        return Objects.hash(txHash, from, to, valueWei, gasUsed);
    }

}
