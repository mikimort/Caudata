package org.main;

import org.UI.Printer;
import org.UI.TerminalUI;
import org.access.BlockFetcher;
import org.access.BlockPoller;
import org.access.TransactionFetcher;
import org.dataManage.DataAggregator;
import org.dataManage.DataFilter;
import org.model.BlockData;
import org.model.TransactionData;
import org.raports.RaportCreator;
import org.sepolia.SepoliaConnection;

import org.UI.Dashboard;
import org.UI.Printer;
import org.dataManage.StatsBuilder;
import org.model.Stats;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Main
{
    private static final int BLOCKS = 100;
    private static final int POLL_SEC = 10;

    public static void main(String [] args) throws IOException
    {
        SepoliaConnection sepoliaConnection = new SepoliaConnection("https://sepolia.infura.io/v3/64151ebadac446c9a00c633e907341fd");
        Printer.success("Połączono z Sepolią, wersja: "+ sepoliaConnection.getClientVersion());
        Printer.info("Ostatni blok: " + sepoliaConnection.getLatestBlock());

        var web3j = sepoliaConnection.getWeb3j();
        BlockFetcher blockFetcher = new BlockFetcher(web3j, Printer::info);
        TransactionFetcher txFetcher = new TransactionFetcher(web3j, blockFetcher, Printer::info);
        DataFilter filter = new DataFilter();
        DataAggregator aggregator = new DataAggregator();
        Dashboard dashboard = new Dashboard();
        StatsBuilder builder = new StatsBuilder(aggregator);

        // ### Pobranie 100 najnowszych bloków
        Printer.section("Pobranie 100 najnowszych bloków i transakcji dla 10 ostatnich");

        List<BlockData> blocks = new CopyOnWriteArrayList<>(blockFetcher.fetchLatestBlocks(100));
        List<BlockData> activeBlocks = filter.filterBlocksByMinTransactions(blocks, 1);
        List<BlockData> newestTen = filter.takeNewest(blocks, 10);

        List<TransactionData> txs = txFetcher.fetchTransactionsForBlocksWithRetry(newestTen);

        Printer.filterSummary(filter, blocks, activeBlocks, txs);

        Stats stats = builder.build(blocks, txs);
        //TerminalUI.clear();
        dashboard.render(stats);

        Printer.section("Uruchamianie pollingu");
        BlockPoller poller = new BlockPoller(blockFetcher, txFetcher, POLL_SEC, blocks);

        RaportCreator raportCreator = new RaportCreator();
        //Ctrl+C zatrzymuje
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\nZamykanie aplikacji... Zatrzymuję polling.");
            poller.stop();

            System.out.println("Generowanie raportu końcowego...");
            String nazwaPliku = "raport_blockchain_" + System.currentTimeMillis() + ".csv";

            // Raport zostaje wygenerowany w momencie zamknięcia programu
            raportCreator.exportToCSV(nazwaPliku, blocks);
            System.out.println("Plik został zapisany jako: " + nazwaPliku);
        }));

        Thread pollingThread = new Thread(poller::start);
        pollingThread.start();
    }
}
