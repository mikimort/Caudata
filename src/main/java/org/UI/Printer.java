package org.UI;

import org.dataManage.DataFilter;
import org.model.BlockData;
import org.model.TransactionData;

import java.util.List;

import static org.UI.TerminalUI.*;

public class Printer {

    public static void section(String title) {
        System.out.println();
        System.out.println(BOLD + CYAN + "  " + title.toUpperCase() + RESET);
        hr(50);
    }

    public static void success(String msg) {
        System.out.println(GREEN + " ✔ " + RESET + msg);
    }

    public static void warn(String msg) {
        System.out.println(YELLOW + " ⚠ " + RESET + msg);
    }

    public static void error(String msg) {
        System.out.println(RED + " ✖ " + RESET + msg);
    }

    public static void info(String msg) {
        System.out.println(CYAN + " ℹ " + RESET + msg);
    }

    public static void dim(String msg) {
        System.out.println(DIM + "   " + msg + RESET);
    }

    public static void field(String label, String value) {
        System.out.printf(" %-28s %s%s%s%n",
                DIM + label + RESET, BOLD, value, RESET);
    }

    public static void field(String label, String value, String valueColor) {
        System.out.printf(" %-28s %s%s%s%n",
                DIM + label + RESET, valueColor + BOLD, value, RESET);
    }

    public static void filterSummary(
            DataFilter filter,
            List<BlockData> allBlocks,
            List<BlockData> activeBlocks,
            List<TransactionData> txs) {

        section("Podsumowanie filtrów");
        field("Bloki z co najmniej 1 tx",activeBlocks.size() + " / " + allBlocks.size());
        field("Tx >= 0.01 ETH", String.valueOf(filter.filterByMinValue(txs, 0.01).size()));
        field("Tx z gazem <= 100,000", String.valueOf(filter.filterByMaxGas(txs, 100_000).size()));
    }
}
