package org.access;

import org.UI.Printer;
import org.model.BlockData;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.response.EthBlock;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Klasa obsługująca pobieranie bloków z sieci.
 * Implementuje mechanizm ponawiania prób (MAX_RETRIES = 3) z opóźnieniem oraz opcjonalne
 * opóźnienie między żądaniami (DELAY_MS = 100 ms) zapobiegające przeciążeniu węzła.
 */
public class BlockFetcher {
    private final Web3j web3j;
    private final Consumer<String> onProgress;
    private final static int DELAY_MS = 100;
    private static final int MAX_RETRIES = 3;

    public BlockFetcher(Web3j web3j) {
        this(web3j, msg -> {
        });
    }

    public BlockFetcher(Web3j web3j, Consumer<String> onProgress) {
        this.web3j = web3j;
        this.onProgress = onProgress;
    }

    /**
     * Pobiera 'count' najnowszych bloków, zaczynając od bieżącego.
     */
    public List<BlockData> fetchLatestBlocks(int count) throws IOException {
        BigInteger latestNumber = web3j.ethBlockNumber().send().getBlockNumber();
        List<BlockData> result = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            BigInteger blockNum = latestNumber.subtract(BigInteger.valueOf(i));
            onProgress.accept(String.format("Pobieranie bloku #%s  (%d / %d)", blockNum, i + 1, count));
            BlockData block = fetchBlockWithRetry(blockNum);
            if (block != null) {
                result.add(block);
            }
        }
        return result;
    }

    /**
     * Pobiera pojedynczy blok z mechanizmem retry (maks. 3 próby).
     */
    public BlockData fetchBlockWithRetry(BigInteger blockNum) throws IOException {

        int attempt = 0;
        while (attempt < MAX_RETRIES) {

            try {
                EthBlock.Block block = web3j.ethGetBlockByNumber(DefaultBlockParameter.valueOf(blockNum), false).send().getBlock();
                if (block == null) return null;

                return new BlockData(block.getNumber(), block.getHash(), block.getTransactions().size(), block.getTimestamp());
            } catch (Exception e) {
                attempt++;
                long waitMS = 1000L + attempt;
                Printer.warn(String.format("Błąd bloku #%s (próba %d/%d), czekam %ds: %s", blockNum, attempt, MAX_RETRIES, waitMS / 1000, e.getMessage()));
                sleep(waitMS);
            }

        }
        Printer.error(String.format("Pominięto blok #%s po %d próbach. %n", blockNum, MAX_RETRIES));
        return null;
    }

    /**
     * Pobiera pełny blok wraz ze wszystkimi transakcjami (fullTx=true).
     */
    public EthBlock.Block fetchBlockWithTransactions(BigInteger blockNumber) throws IOException {
        return web3j.ethGetBlockByNumber(DefaultBlockParameter.valueOf(blockNumber), true).send().getBlock();
    }

    /**
     * Pobiera numer najnowszego bloku w sieci.
     */
    public BigInteger getLatestBlockNumber() throws IOException {
        return web3j.ethBlockNumber().send().getBlockNumber();
    }

    /**
     * Pobiera wszystkie bloki z podanego zakresu numerów.
     * Między blokami stosuje opóźnienie DELAY_MS.
     */
    public List<BlockData> fetchBlockRange(BigInteger from, BigInteger to) {
        List<BlockData> result = new ArrayList<>();
        for (BigInteger num = from; num.compareTo(to) <= 0; num = num.add(BigInteger.ONE)) {
            try {
                EthBlock.Block block = web3j.ethGetBlockByNumber(DefaultBlockParameter.valueOf(num), false).send().getBlock();

                if (block != null) {
                    result.add(new BlockData(block.getNumber(), block.getHash(), block.getTransactions().size(), block.getTimestamp()));
                }

                sleep(DELAY_MS);
            } catch (IOException e) {
                Printer.error(String.format("Błąd pobierania bloku #%s: %s%n", num, e.getMessage()));
            }
        }

        return result;
    }

    private void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }
}
