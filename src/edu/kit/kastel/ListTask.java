package edu.kit.kastel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class ListTask {

    public static void main(String[] args) {
        Integer[] numbers = { 1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89 };
        String[] strings = { "Hello", "World", "my", "Name", "is", "[]" };

        List<Integer> listInt = toList(numbers);
        List<String> listStr = toList(strings);
    }

    public abstract  <T> List<T> _toList(T[] array);




































    public static <T> List<T> toList(T[] array) {
        return null;    // Todo
    }

}
