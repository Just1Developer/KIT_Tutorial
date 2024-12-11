package edu.kit.kastel;

import java.util.List;

public class Inheritance {
    static class Parent {
        protected void print() {
            System.out.println("A");
            System.out.println(getNumber());
        }
        public static int getNumber() {
            return 42;
        }
        public int g() {
            return h() + 2;
        }
        public static int h() {
            return 9;
        }
        public int k() {
            return i() + 5;
        }
        public int i() {
            return 20;
        }
        public Parent bruh() {
            return null;
        }
    }

    static class Child extends Parent {
        @Override
        protected void print() {
            System.out.println("B");
            System.out.println(getNumber());
        }
        public static int getNumber() {
            return 14;
        }
        public static int h() {
            return 5;
        }
        public int i() {
            return 30;
        }
    }

    public static void main(String[] args) {
        Parent p = new Child();
        p.print();
        System.out.println(p.g());
        System.out.println(p.k());
        System.out.println(p.getNumber());
    }


    static class Animal {

    }
    static class Cat extends Animal {

    }
    static class Dog extends Animal {

    }
    static class Stuff<T extends Dog> {
        public static <A> void doStuff(A dog) {
            Cat cat = new Cat();
            var list = List.of((Animal) cat);
            doStuffToo(list);
        }
        public static void doStuffToo(List<? super Dog> dog) {

        }
    }

}
