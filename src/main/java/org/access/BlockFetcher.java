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
    private final static int DELAY_MS = 1000;
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

            try
            {
                EthBlock.Block block = web3j.ethGetBlockByNumber(DefaultBlockParameter.valueOf(blockNum), false).send().getBlock();

                if (block != null)
                {
                    result.add(new BlockData(block.getNumber(), block.getHash(), block.getTransactions().size()));
                }
            }
            catch (IOException e)
            {
                System.err.printf("Błąd połączenia bloku #%s: %s%n", blockNum, e.getMessage());
                //Obsługa rate-limitingu
                sleep(1000);
            }
        }
        return result;

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
