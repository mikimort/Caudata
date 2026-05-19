package org.access;

import org.UI.Printer;
import org.model.BlockData;
import org.model.TransactionData;

import java.math.BigInteger;
import java.util.List;

/**
 * Klasa realizująca ciągłe monitorowanie sieci (polling).
 * Cyklicznie sprawdza, czy pojawiły się nowe bloki.
 * Przy nowych blokach pobiera je wraz z transakcjami i wypisuje do konsoli.
 * Zatrzymywanie jest wątkobezpieczne dzięki volatile boolean running.
 */
public class BlockPoller {
    private final BlockFetcher blockFetcher;
    private final TransactionFetcher transactionFetcher;
    private final int intervalSeconds;
    private final List<BlockData> allBlocks;

    private volatile boolean running = true;

    public BlockPoller(BlockFetcher blockFetcher, TransactionFetcher transactionFetcher, int intervalSeconds, List<BlockData> allBlocks) {
        this.blockFetcher = blockFetcher;
        this.transactionFetcher = transactionFetcher;
        this.intervalSeconds = intervalSeconds;
        this.allBlocks = allBlocks;
    }

    /**
     * Zatrzymuje pętlę pollingu (ustawia running = false).
     */
    public void stop() {
        running = false;
    }

    /**
     * Uruchamia pętlę pollingu. Blokuje wątek do momentu wywołania stop().
     */
    public void start() {
        Printer.info(String.format("Uruchomiono pulling (co %d s). ", intervalSeconds));
        BigInteger lastKnownBlock = null;

        while (running) {
            try {
                BigInteger latest = blockFetcher.getLatestBlockNumber();

                if (lastKnownBlock == null) {
                    lastKnownBlock = latest;
                    Printer.info("Punkt startowy: blok #" + latest);

                } else if (latest.compareTo(lastKnownBlock) > 0) {
                    //Są nowe bloki
                    BigInteger from = lastKnownBlock.add(BigInteger.ONE);
                    List<BlockData> newBlocks = blockFetcher.fetchBlockRange(from, latest);

                    allBlocks.addAll(newBlocks);
                    Printer.section("[ + ] " + newBlocks.size() + " nowy/nowych bloków: ");
                    System.out.println(newBlocks);
                    List<TransactionData> transactions = transactionFetcher.fetchTransactionsForBlocksWithRetry(newBlocks);
                    transactions.forEach(System.out::println);
                    lastKnownBlock = latest;
                } else {
                    Printer.dim("Brak nowych bloków (ostatni: #" + latest + ")");
                }
                Thread.sleep(intervalSeconds * 1000L);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                System.err.println("Błąd pollingu: " + e.getMessage());
                try {
                    Thread.sleep(intervalSeconds * 1000L);
                } catch (InterruptedException es) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        System.out.println("Polling zatrzymany.");
    }
}
