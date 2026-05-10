package org.access;

import org.model.BlockData;
import org.model.TransactionData;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/*
* Pobieranie transakcji dla wzkazanych bloków
*/
public class TransactionFetcher
{
    private final Web3j web3j;
    private final BlockFetcher blockFetcher;

    public TransactionFetcher(Web3j web3j, BlockFetcher blockFetcher)
    {
        this.web3j = web3j;
        this.blockFetcher = blockFetcher;
    }

    public List<TransactionData> fetchTransactionsForBlocks(List<BlockData> blocks) throws IOException
    {
        List<TransactionData> allTransactions = new ArrayList<>();

        for(BlockData bd : blocks)
        {
            try
            {
                EthBlock.Block fullBlock = blockFetcher.fetchBlockWithTransactions(bd.getBlockNumber());

                if (fullBlock == null) continue;

                for(EthBlock.TransactionResult<?> txResult : fullBlock.getTransactions())
                {
                    EthBlock.TransactionObject txObj = (EthBlock.TransactionObject) txResult.get();

                    BigInteger gasUsed = resolveGasUsed(txObj);

                    allTransactions.add(new TransactionData(txObj.getHash(), txObj.getFrom(), txObj.getTo(), txObj.getValue(), gasUsed));

                    Thread.sleep(150);
                }
            }
            catch(IOException e)
            {
                System.err.printf("Błąd pobierania dla bloku #%s: %s%n", bd.getBlockNumber(), e.getMessage());
            }

            catch (InterruptedException e)
            {
                Thread.currentThread().interrupt();
                break;
            }
        }
        return allTransactions;
    }

    private BigInteger resolveGasUsed(EthBlock.TransactionObject txObj)
    {
        try
        {
            EthGetTransactionReceipt receiptResponse = web3j.ethGetTransactionReceipt(txObj.getHash()).send();
            Optional<TransactionReceipt> receipt = receiptResponse.getTransactionReceipt();
            if(receipt.isPresent())
            {
                return receipt.get().getGasUsed();
            }
        }
        catch (IOException ignored) {}
        return txObj.getGas();
    }
}
