package org.model;

import java.math.BigInteger;

public class TransactionData
{
    private final String txHash;
    private final String from;
    private final String to;
    private final BigInteger valueWei;
    private final BigInteger gasUsed;

    public TransactionData(String txHash, String from, String to, BigInteger valueWei, BigInteger gasUsed)
    {
        this.txHash = txHash;
        this.from = from;
        this.to = to;
        this.valueWei = valueWei;
        this.gasUsed = gasUsed;
    }

    public String getTxHash()
    {
        return txHash;
    }

    public String getFrom()
    {
        return from;
    }

    public String getTo()
    {
        return to;
    }

    public BigInteger getValueWei()
    {
        return valueWei;
    }

    public BigInteger getGasUsed()
    {
        return gasUsed;
    }

    /*Zwraca wartość transakcji w ETH*/
    public double getValueEth()
    {
        return valueWei.doubleValue() / 1e18;
    }

    @Override
    public String toString()
    {
        return String.format("   Tx: %s%n    Od: %s%n     Do:    %s%n     Wartość: %.6f Eth   | Gas: %s", txHash, from, to != null ? to : "(tworzenie kontraktu)", getValueEth(), gasUsed);
    }

}
