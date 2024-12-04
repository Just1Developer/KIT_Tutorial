package edu.kit.kastel;

// Todo: Das hier kompatibel mit jedem Datentyp machen, nicht nur Strings

public class GenericStringConcat {
    private final String[] values;

    public GenericStringConcat(String[] values) {
        this.values = new String[values.length];
        System.arraycopy(values, 0, this.values, 0, values.length);
    }

    public String concat(char symbol) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < values.length; i++) {
            builder.append(values[i]);
            if (i < values.length - 1) {
                builder.append(symbol);
            }
        }
        return builder.toString();
    }
}
