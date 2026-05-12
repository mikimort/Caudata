package org.main;

import org.access.BlockFetcher;
import org.access.BlockPoller;
import org.access.TransactionFetcher;
import org.dataManage.DataAggregator;
import org.dataManage.DataFilter;
import org.model.BlockData;
import org.model.TransactionData;
import org.raports.RaportCreator;
import org.sepolia.SepoliaConnection;

import java.io.IOException;
import java.util.List;

public class Main
{
    public static void main(String [] args) throws IOException
    {
        SepoliaConnection sepoliaConnection = new SepoliaConnection("https://sepolia.infura.io/v3/64151ebadac446c9a00c633e907341fd");
        System.out.println("Client Version: " + sepoliaConnection.getClientVersion());
        System.out.println("Latest Block: " + sepoliaConnection.getLatestBlock());

        var web3j = sepoliaConnection.getWeb3j();

        BlockFetcher blockFetcher = new BlockFetcher(web3j);
        TransactionFetcher txFetcher = new TransactionFetcher(web3j, blockFetcher);


        // ### Pobranie 100 najnowszych bloków

        System.out.println("\n ### Pobranie 100 najnowszych bloków ###");
        List<BlockData> blocks = blockFetcher.fetchLatestBlocks(100);
        blocks.forEach(System.out::println);

        DataFilter filter = new DataFilter();
        List<BlockData> activeBlocks = filter.filterBlocksByMinTransactions(blocks, 1);
        System.out.printf("%nBloki z co najmniej 1 transakcją: %d / %d%n", activeBlocks.size(), blocks.size());

        List<BlockData> newestTen = filter.takeNewest(blocks, 10);

        System.out.println("### Pobieranie transakcji dla 10 najnowszych bloków ###");
        List<TransactionData> transactions = txFetcher.fetchTransactionsForBlocksWithRetry(newestTen);
        transactions.forEach(System.out::println);

        List<TransactionData> highValue = filter.filterByMinValue(transactions, 0.01);
        System.out.printf("%nTransakcje >= 0.01 ETH: %d%n", highValue.size());

        List<TransactionData> lowGas = filter.filterByMaxGas(transactions, 100000);
        System.out.printf("Transakcje z gasUsed <= 100000: %d%n", lowGas.size());

        DataAggregator aggregator = new DataAggregator();
        System.out.println("\n ### Statystyki zbiorcze ###");
        System.out.printf("Łącznei bloków:          %d%n", aggregator.totalBlocks(blocks));
        System.out.printf("Łącznie transakcji:      %d%n", aggregator.totalTransactions(transactions));
        System.out.printf("Śr. tx / blok:           %.2f%n", aggregator.averageTxPerBlock(blocks));
        System.out.printf("Łączna wartość (ETH):    %.4f%n", aggregator.totalValueEth(transactions));
        System.out.printf("Śr. żużycie gazu:        %s%n", aggregator.averageGasUsed(transactions));

        System.out.println("### Uruchamianie pollingu ###");
        BlockPoller poller = new BlockPoller(blockFetcher, txFetcher, 10);

        RaportCreator raportCreator = new RaportCreator();
        //Ctrl+C zatrzymuje
        Runtime.getRuntime().addShutdownHook(new Thread(poller::stop));

        Thread pollingThread = new Thread(poller::start);
        pollingThread.start();

        String nazwaPliku = "raport_blockchain_" + System.currentTimeMillis() + ".csv";
        raportCreator.exportToCSV(nazwaPliku, blocks);
        System.out.println("Plik został zapisany jako: " + nazwaPliku);
    }
}
