package objekte;

import java.util.Arrays;

public class DatumVergleichen {

    public static void main(String[] args) {
        String[] daten = { "16.05.1703", "08.12.1925", "08.06.1925",
                "12.02.1031" };
        System.out.println("Vorher:");
        for (String s : daten) {
            System.out.println(s);
        }
        System.out.println("\nNachher:");
        Arrays.sort(daten, new DateComparator(false));
        for (String s : daten) {
            System.out.println(s);
        }
        
        DateComparator dc = new DateComparator();
        System.out.println("Vergleich von " + daten[0] + " und " + daten[1]
                + ": " + dc.compare(daten[0], daten[1]));
        System.out.println("Vergleich von " + daten[1] + " und " + daten[2]
                + ": " + dc.compare(daten[1], daten[2]));
        System.out.println("Vergleich von " + daten[2] + " und " + daten[3]
                + ": " + dc.compare(daten[2], daten[3]));
        System.out.println("Vergleich von " + daten[3] + " und " + daten[0]
                + ": " + dc.compare(daten[3], daten[0]));
    }
}
