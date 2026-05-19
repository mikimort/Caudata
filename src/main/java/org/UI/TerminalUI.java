package org.UI;

/**
 * Klasa narzędziowa do stylizacji tekstu za pomocą ANSI.
 * <p>
 * Kody ANSI to specjalne sekwencje znaków, które terminal interpretuje
 * jako polecenia formatowania zamiast wyświetlać je dosłownie.
 * Każdy kod zaczyna się od ESC (unicode \u001B) - rozpoczęcie sekwencji sterującej,
 * po którym następuje '[' (początek kodu ANSI) i polecenie.
 * <p>
 * Kody kolorów mają postać:
 * \u001B[Xm   — gdzie X to liczba wybierająca kolor
 * <p>
 * Numery kolorów tekstu:
 * <p>
 * 31 = czerwony,  32 = zielony,  33 = żółty, 36 = cyjanowy
 * <p>
 * Numery stylów:
 * <p>
 * 0 = reset (powrót do domyślnych ustawień terminala)
 * 1 = pogrubienie (bold)
 * 2 = przyciemnienie (dim)
 */
public class TerminalUI {

    public static final String RESET = "\u001B[0m";
    public static final String BOLD = "\u001B[1m";
    public static final String DIM = "\u001B[2m";

    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String CYAN = "\u001B[36m";

    /**
     * Drukuje poziomą linię separatora o podanej szerokości.
     */
    public static void hr(int width) {
        System.out.println(DIM + "─".repeat(width) + RESET);
    }

    /**
     * Ustawia kolor tekstu na kolor ANSI i go usuwa za pomocą RESET na końcu,
     * żeby kolor nie przenosił się na dalszy tekst.
     */
    public static String color(String ansiCode, String text) {
        return ansiCode + text + RESET;
    }
}