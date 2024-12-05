package edu.kit.kastel;

// Todo: Das hier kompatibel mit jedem Datentyp machen, nicht nur Strings

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GenericStringConcat<T> {
    private List<T> values;

    public GenericStringConcat(T[] values) {
        this.values = new ArrayList<T>();
        //System.arraycopy(values, 0, this.values, 0, values.length);
        this.values.addAll(Arrays.asList(values));
    }

    public String concat(char symbol) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < values.size(); i++) {
            builder.append(values.get(i));
            if (i < values.size() - 1) {
                builder.append(symbol);
            }
        }
        return builder.toString();
    }
}
