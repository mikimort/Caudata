package org.UI;

import org.model.Stats;
import org.model.TransactionData;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static org.UI.TerminalUI.*;

public class Dashboard {

    private static final int WIDTH = 60;
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm:ss");

    public void render(Stats stats) {
        clear();

        printHeader();
        printOverview(stats);
        printGasStats(stats);
        printLatestTransactions(stats);
        printFooter();
    }

    private void printHeader() {
        String time = LocalTime.now().format(TIME_FMT);
        System.out.println();
        System.out.println(BOLD + CYAN + "  ⬡  ETH SEPOLIA MONITOR" + RESET + DIM + " " + time + RESET);hr(WIDTH);
    }

    private void printOverview(Stats stats) {
        Printer.section("Statystyki zbiorcze");
        Printer.field("Ostatni blok",       "#" + stats.getLatestBlock(), YELLOW);
        Printer.field("Przeskanowane bloki",     String.valueOf(stats.getBlocksCount()));
        Printer.field("Łączna liczba tx", String.valueOf(stats.getTransactionsCount()));
        Printer.field("Śr. tx / blok",     String.format("%.2f", stats.getAvgTxPerBlock()));
    }

    private void printGasStats(Stats stats) {
        Printer.section("Statystyki ekonomi");
        Printer.field("Łączna wartość (ETH)", String.format("%.6f ETH", stats.getTotalEth()), GREEN);
        Printer.field("Śr. zużycie gazu",           stats.getAvgGasUsed().toString());
    }

    private void printLatestTransactions(Stats stats) {
        if (stats.getLatestTransactions() == null || stats.getLatestTransactions().isEmpty()) return;

        Printer.section("Ostatnie transakcje:");
        for (TransactionData tx : stats.getLatestTransactions()) {
            printTxRow(tx);
        }
    }

    private void printTxRow(TransactionData tx) {
        String hash  = abbreviate(tx.getTxHash(), 18);
        String from  = abbreviate(tx.getFrom(), 14);
        String to    = tx.getTo() != null ? abbreviate(tx.getTo(), 14) : color(DIM, "(tworzenie kontraktu)");
        String value = String.format("%.5f ETH", tx.getValueEth());
        String gas   = tx.getGasUsed().toString();

        System.out.println();
        System.out.printf("  %s%s%s%n", CYAN, hash, RESET);
        System.out.printf("    %sod%s %-18s %sdo%s %s%n",
                DIM, RESET, from, DIM, RESET, to);
        System.out.printf("    %s%-20s%s  gaz: %s%s%s%n",
                GREEN + BOLD, value, RESET, DIM, gas, RESET);

        hr(WIDTH);
    }

    private void printFooter() {
        System.out.println();
        System.out.println(DIM + " Naciśnij Ctrl+C, aby wyjść" + RESET);
        System.out.println();
    }

    /** Truncates a string to maxLen, appending "…" if cut. */
    private static String abbreviate(String s, int maxLen) {
        if (s == null) return "(null)";
        if (s.length() <= maxLen) return s;
        return s.substring(0, maxLen - 1) + "…";
    }
}