package org.access;

import org.model.BlockData;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.response.EthBlock;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

public class BlockFetcher
{
    private final Web3j web3j;
    private final static int DELAY_MS = 250;
    private static final int MAX_RETRIES  = 3;
    public BlockFetcher(Web3j web3j)
    {
        this.web3j = web3j;
    }

    public  List<BlockData> fetchLatestBlocks(int count) throws IOException
    {
        BigInteger latestNumber = web3j.ethBlockNumber().send().getBlockNumber();
        List<BlockData> result = new ArrayList<>();

        for(int i=0; i < count; i++)
        {
            BigInteger blockNum = latestNumber.subtract(BigInteger.valueOf(i));
            BlockData block = fetchBlockWithRetry(blockNum);
            if(block != null)
            {
                result.add(block);
                System.out.printf("Pobrano blok #%s (%d/%d)%n", blockNum, i + 1, count);
            }
        }
        return result;

    }

    public  BlockData fetchBlockWithRetry(BigInteger blockNum) throws IOException
    {

        int attempt = 0;
        while (attempt < MAX_RETRIES)
        {
           try
           {
               EthBlock.Block block = web3j.ethGetBlockByNumber(DefaultBlockParameter.valueOf(blockNum), false).send().getBlock();
               if (block == null) return null;

               return new BlockData(block.getNumber(), block.getHash(), block.getTransactions().size());

           }
           catch(Exception e)
           {
               attempt++;
               long waitMS = 1000L + attempt;
               System.err.printf("Błąd bloku #%s (próba %d/%d), czekam %ds: %s%n", blockNum, attempt, MAX_RETRIES, attempt, e.getMessage());
               sleep(waitMS);
           }

        }
        System.err.printf("Pominięto blok #%s po %d próbach. %n", blockNum, MAX_RETRIES);
        return null;
    }


    public EthBlock.Block fetchBlockWithTransactions(BigInteger blockNumber) throws IOException
    {
        return web3j.ethGetBlockByNumber(DefaultBlockParameter.valueOf(blockNumber), true).send().getBlock();
    }

    public BigInteger getLatestBlockNumber() throws IOException
    {
        return web3j.ethBlockNumber().send().getBlockNumber();
    }

    public List<BlockData> fetchBlockRange(BigInteger from, BigInteger to)
    {
        List<BlockData> result = new ArrayList<>();
        for(BigInteger num = from; num.compareTo(to) <= 0; num.add(BigInteger.ONE))
        {
            try
            {
                EthBlock.Block block = web3j.ethGetBlockByNumber(DefaultBlockParameter.valueOf(num), false).send().getBlock();

                if(block != null)
                {
                    result.add(new BlockData(block.getNumber(), block.getHash(), block.getTransactions().size()));
                }

                sleep(DELAY_MS);
            }
            catch (IOException e)
            {
                System.err.printf("Błąd pobierania bloku #%s: %s%n", num, e.getMessage());
            }
        }

        return result;
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
