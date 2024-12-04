package edu.kit.kastel;

public class AbstractExample {

    public static void main(String[] args) {

    }

    public static void printCalc(A calculator, int x) {
        // Beim Aufrufen ist calculator ein Objekt, also entweder vom Typ B oder C
        System.out.println(calculator.calculate(x));
    }


    // --------- Andere Klassen. Hier nur in einer Datei für Übersicht ---------


    // Static damit wir keine Instanz von AbstractExample brauchen, um auf die Klasse zuzugreifen
    public static abstract class A {
        public abstract int calculate(int x);
    }

    public static class B extends A {
        // Todo
    }

    public static class C extends A {
        // Todo
    }

}
