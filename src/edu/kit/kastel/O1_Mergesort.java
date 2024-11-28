package edu.kit.kastel;

public class O1_Mergesort {

    // O(log n) Stack Space due to Recursion

    public static int[] _mergesort(int[] array) {
        mergesort_O1_noRecursion(array);
        return array;
    }


    /**
     * True O(1) space mergesort, without recursion or any additional allocations
     * besides a constant amount of integers.
     * @param array the array to sort
     */
    public static void mergesort_O1_noRecursion(int[] array) {
        if (array == null || array.length < 2) return;

        // It's still log(n) if we start at size 2 and double the size each iteration
        int segmentSize = 2;
        while ((segmentSize / 2) < array.length) {
            for (int index = 0; index < array.length; index += segmentSize) {
                // Split the given segment in two and assume it is sorted
                int length = Math.min(array.length - index, segmentSize);
                if (length == 1) continue;

                /* Merge functionality from below method O1_Mergesort::mergesort_logN */
                mergesort_inner(array, index, length);
            }
            segmentSize *= 2;
        }
    }
    private static void mergesort_inner(int[] array, int start, int length) {
        int len1 = (length+1) / 2;
        int len2 = length - len1;
        int i1 = start, i2 = start + len1, LIMIT_I1 = start + len1, LIMIT_I2 = start + len1 + len2;
        int swapped = 0;
        for (int k = 0, i = start; k < length - 1; k++, i++) {
            int realI1 = swapped < i1 - start ? i1 : i1 + len1;
            if (i2 >= LIMIT_I2 || (i1 < LIMIT_I1 && array[realI1] < array[i2])) {
                swap(array, i, realI1);
                i1++;
            } else /*if (true || array[realI1] > array[i2])*/ {
                swap(array, i, i2);
                i2++;
                swapped++;
            }
        }
    }




    public static void mergesort_logN(int[] array) {
        if (array == null || array.length < 2) return;
        mergesort_logN(array, 0, array.length);
    }
    private static void mergesort_logN(int[] array, int start, int length) {
        if (length <= 1) return;
        int len1 = (length+1) / 2;
        int len2 = length - len1;
        mergesort_logN(array, start, len1);
        mergesort_logN(array, start + len1, len2);
        // Merge together
        int i1 = start, i2 = start + len1, LIMIT_I1 = start + len1, LIMIT_I2 = start + len1 + len2;
        int swapped = 0;
        for (int k = 0, i = start; k < length - 1; k++, i++) {
            int realI1 = swapped < i1 - start ? i1 : i1 + len1;
            if (i2 >= LIMIT_I2 || (i1 < LIMIT_I1 && array[realI1] < array[i2])) {
                swap(array, i, realI1);
                i1++;
            } else /*if (true || array[realI1] > array[i2])*/ {
                swap(array, i, i2);
                i2++;
                swapped++;
            }
        }
    }

    private static void swap(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    public static void mergesort2(int[] array) {
        if (array == null || array.length < 2) return;
        mergesort2(array, 0, array.length);
    }
    private static void mergesort2(int[] array, int start, int length) {
        if (length <= 1) return;
        int len1 = (length+1) / 2;
        int len2 = length - len1;
        mergesort2(array, start, len1);
        mergesort2(array, start + len1, len2);
        // Merge together
        int i1 = start, i2 = start + len1, LIMIT_I1 = start + len1, LIMIT_I2 = start + len1 + len2;
        int swapped = 0;
        for (int k = 0, i = start; k < length - 1; k++, i++) {
            int realI1 = swapped <= i1 - start ? i1 : i1 + len1;
            if (i2 >= LIMIT_I2 || (i1 < LIMIT_I1 && array[realI1] < array[i2])) {
                swap(array, i, realI1);
                i1++;
            } else if (true || array[realI1] > array[i2]) {
                swap(array, i, i2);
                i2++;
                swapped++;
            }
        }
    }

}
