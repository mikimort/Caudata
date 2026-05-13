package org.access;

import org.UI.Printer;
import org.model.BlockData;
import org.model.TransactionData;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.exceptions.ClientConnectionException;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/*
* Pobieranie transakcji dla wzkazanych bloków
*/
public class TransactionFetcher
{
    private final Web3j web3j;
    private final BlockFetcher blockFetcher;
    private final Consumer<String> onProgress;


    public TransactionFetcher(Web3j web3j, BlockFetcher blockFetcher) {
        this(web3j, blockFetcher, msg -> {});
    }

    public TransactionFetcher(Web3j web3j, BlockFetcher blockFetcher, Consumer<String> onProgress) {
        this.web3j        = web3j;
        this.blockFetcher = blockFetcher;
        this.onProgress   = onProgress;
    }

    public List<TransactionData> fetchTransactionsForBlocksWithRetry(List<BlockData> blocks) throws IOException
    {
        List<TransactionData> allTransactions = new ArrayList<>();
        for(int i =0; i< 3; i++)
        {
            try
            {
                allTransactions = fetchTransactionsForBlocks(blocks);
            }
            catch (ClientConnectionException e)
            {
                Printer.warn(String.format("Błąd tranzakcji, (próba %d/3), czekam 1s: %s%n", i + 1, e.getMessage()));
                sleep(1000);
                continue;
            }
        }
        return allTransactions;
    }

    public List<TransactionData> fetchTransactionsForBlocks(List<BlockData> blocks) throws IOException
    {
        List<TransactionData> allTransactions = new ArrayList<>();
        int total = blocks.size();

        for (int i = 0; i < total; i++) {
            BlockData bd = blocks.get(i);
            onProgress.accept(String.format(
                    "Pobieranie transakcji dla bloku #%s  (%d / %d)", bd.getBlockNumber(), i + 1, total));
            try
            {
                EthBlock.Block fullBlock = blockFetcher.fetchBlockWithTransactions(bd.getBlockNumber());

                if (fullBlock == null) continue;

                for(EthBlock.TransactionResult<?> txResult : fullBlock.getTransactions())
                {
                    EthBlock.TransactionObject txObj = (EthBlock.TransactionObject) txResult.get();

                    BigInteger gasUsed = txObj.getGas();

                    allTransactions.add(new TransactionData(txObj.getHash(), txObj.getFrom(), txObj.getTo(), txObj.getValue(), gasUsed));
                }
                Thread.sleep(150);
            }
            catch(IOException e)
            {
                Printer.error(String.format("Błąd pobierania dla bloku #%s: %s%n", bd.getBlockNumber(), e.getMessage()));
            }

            catch (InterruptedException e)
            {
                Thread.currentThread().interrupt();
                break;
            }
        }
        return allTransactions;
    }

    private void sleep(long ms)
    {
        try
        {
            Thread.sleep(ms);
        }
        catch (InterruptedException ie)
        {
            Thread.currentThread().interrupt();
        }
    }

}
