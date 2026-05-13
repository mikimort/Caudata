package org.UI;

public class TerminalUI {

    // ANSI color codes
    public static final String RESET   = "\u001B[0m";
    public static final String BOLD    = "\u001B[1m";
    public static final String DIM     = "\u001B[2m";

    public static final String RED     = "\u001B[31m";
    public static final String GREEN   = "\u001B[32m";
    public static final String YELLOW  = "\u001B[33m";
    public static final String BLUE    = "\u001B[34m";
    public static final String CYAN    = "\u001B[36m";

    public static void clear() {
        System.out.print("\u001B[2J\u001B[H");
        System.out.flush();
    }

    public static void home() {
        System.out.print("\u001B[H");
        System.out.flush();
    }

    public static void hr(int width) {
        System.out.println(DIM + "─".repeat(width) + RESET);
    }

    public static String color(String ansiCode, String text) {
        return ansiCode + text + RESET;
    }
}