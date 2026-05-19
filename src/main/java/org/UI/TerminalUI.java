package org.UI;

/**
 * Klasa narzędziowa do stylizacji tekstu za pomocą ANSI.
 *
 * Kody ANSI to specjalne sekwencje znaków, które terminal interpretuje
 * jako polecenia formatowania zamiast wyświetlać je dosłownie.
 * Każdy kod zaczyna się od ESC (unicode \u001B) - rozpoczęcie sekwencji sterującej,
 * po którym następuje '[' (początek kodu ANSI) i polecenie.
 *
 * Kody kolorów mają postać:
 *   \u001B[Xm   — gdzie X to liczba wybierająca kolor
 *
 * Numery kolorów tekstu:
 *
 *   31 = czerwony,  32 = zielony,  33 = żółty, 36 = cyjanowy
 *
 * Numery stylów:
 *
 *    0 = reset (powrót do domyślnych ustawień terminala)
 *    1 = pogrubienie (bold)
 *    2 = przyciemnienie (dim)
 *
 */
public class TerminalUI {

    public static final String RESET   = "\u001B[0m";
    public static final String BOLD    = "\u001B[1m";
    public static final String DIM     = "\u001B[2m";

    public static final String RED     = "\u001B[31m";
    public static final String GREEN   = "\u001B[32m";
    public static final String YELLOW  = "\u001B[33m";
    public static final String CYAN    = "\u001B[36m";

    /**
     * Drukuje poziomą linię separatora o podanej szerokości.
     * @param width liczba znaków '─' do wydrukowania
     */
    public static void hr(int width) {
        System.out.println(DIM + "─".repeat(width) + RESET);
    }

    /**
     * Ustawia kolor tekstu na kolor ANSI i go usuwa za pomocą RESET na końcu,
     * żeby kolor nie przenosił się na dalszy tekst.
     * @param ansiCode kolor Ansi
     * @param text     tekst do pokolorowania
     * @return pokolorowany ciąg znaków
     */
    public static String color(String ansiCode, String text) {
        return ansiCode + text + RESET;
    }
}