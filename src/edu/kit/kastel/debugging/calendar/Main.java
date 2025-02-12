package edu.kit.kastel.debugging.calendar;

import java.util.ArrayList;
import java.util.List;

// No visibility modifier to keep class visible in the package only
class Main {

    public static void main(String[] args) {
        List<Candy> candies = new ArrayList<>();
        candies.add(new TestCandy("test", 1, 1));
        candies.add(new TestCandy("test2", 1, 1));
        candies.add(new TestCandy("test3", 1, 1));
        candies.add(new TestCandy("test4", 1, 1));
        AdventCalendar calendar = new AdventCalendar(candies);
    }

    static class TestCandy extends Candy {

        /**
         * Constructs a new Candy with the given name, quantity, and price.
         *
         * @param name     is the name of the candy.
         * @param quantity is the quantity of the candy.
         * @param price    is the price of the candy in cents.
         */
        public TestCandy(String name, int quantity, int price) {
            super(name, quantity, price);
        }

        /**
         * Copies the current state of the candy into a new instance.
         *
         * @return A copy of the candy.
         */
        @Override
        public Candy copy() {
            return new TestCandy(this.getName(), this.getQuantity(), this.getPrice());
        }

        /**
         * Abstract method representing the action of eating the candy. Subclasses should provide their specific implementation.
         */
        @Override
        public void eat() {
            quantity = 0;
        }
    }

}
