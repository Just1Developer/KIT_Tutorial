package edu.kit.kastel.debugging.calendar;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;

// No visibility modifier to keep class visible in the package only
/**
 * This class simulates an Advent calendar containing {@link Candy candies}.
 *
 * @author Programmieren-Team
 * @author uwwfh
 */
final class AdventCalendar {

    private static final int DOORS_PER_LINE = 4;
    private static final String DOOR_REPRESENTATION_FORMAT = "[%s]";
    private static final String CANDY_REPRESENTATION_FORMAT = "%s:%d";
    private static final String EMPTY_DOOR_CONTENT = "   ";

    private int currentDay;
    private final List<Candy> backup;
    private List<Candy> candies;
    private final int maxDays;

    /**
     * {@code false} represents a closed door, {@code true} an opened door.
     * All doors are closed by default.
     */
    private final Set<Integer> openedDoorIndices;

    /**
     * Instantiates a new {@link AdventCalendar}.
     *
     * @param candies the {@link Candy candies} that are contained in this calendar
     */
    public AdventCalendar(List<Candy> candies) {
        this.maxDays = candies.size();
        this.candies = candies;
        this.currentDay = 0;
        this.backup = new ArrayList<>();
        this.openedDoorIndices = new HashSet<>();

        for (int i = 0; i < candies.size(); i++) {
            this.candies.add(candies.get(i));
            this.backup.add(candies.get(i));
        }
    }

    /**
     * Returns the current day.
     *
     * @return the current day
     */
    public int getDay() {
        return this.currentDay;
    }

    /**
     * Attempts to increase the current day by one day.
     * The day can only be increased if the current day is lower than the
     * count of objects initialized for this calendar.
     *
     * @return {@code true} if the current day was increased, {@code false}
     *         otherwise
     */
    public boolean nextDay() {
        return this.nextDays(1);
    }

    /**
     * Attempts to increase the current day by {@code days} days.
     * The day can only be increased if the resulting day will be lower than or
     * equal to the count of objects initialized for this calendar.
     *
     * @param days the number of days
     * @return {@code true} if the current day was increased, {@code false}
     *         otherwise
     */
    public boolean nextDays(int days) {
        if (days <= 0 || this.currentDay + days > maxDays) {
            return false;
        }

        this.currentDay += days;
        return true;
    }

    /**
     * Determines if the door of a given day is open.
     *
     * @param number the number/day of the corresponding door
     * @return {@code true} if the door is open, {@code false} otherwise
     */
    public boolean isDoorOpen(int number) {
        return number >= 1 && number <= maxDays && this.openedDoorIndices.contains(number - 1);
    }

    /**
     * Attempts to open the door with the given number.
     *
     * The door can only be opened if the given number is less then or equal to the
     * current day and if the door has not yet been opened before.
     *
     * @param number the number of the corresponding door
     * @return the sweet behind the door or {@code null} if the door cannot be or
     *         has already been opened
     */
    public Candy openDoor(int number) {
        if (number < 1 || number > this.currentDay || this.isDoorOpen(number)) {
            return null;
        }

        this.openedDoorIndices.add(number - 1);
        return this.candies.get(number - 1);
    }

    /**
     * Attempts to open the doors with the given numbers.
     *
     * The door can only be opened if the given number is less than or equal to the
     * current day and if the door has not yet been opened before.
     *
     * @param numbers the numbers of the corresponding doors
     * @return the {@link Candy candies} behind the doors that could be opened
     */
    public List<Candy> openDoors(List<Integer> numbers) {
        List<Candy> openedCandies = new ArrayList<>();
        for (Integer number : numbers) {
            if (number < 1 || number > this.currentDay || this.isDoorOpen(number)) {
                continue;
            }

            this.openedDoorIndices.add(number - 1);
            openedCandies.add(this.candies.get(number - 1));
        }
        return openedCandies;
    }

    /**
     * Returns the number of unopened doors that may be opened at the current day.
     *
     * @return the number of unopened doors that may be opened at the current day
     */
    public int numberOfUnopenedDoors() {
        return this.currentDay - this.openedDoorIndices.size();
    }

    /**
     * Resets this calendar.
     * After the reset all sweets will be restored, all doors will be closed and the
     * current day will be the day before December 1st.
     */
    public void reset() {
        this.currentDay = 0;
        this.openedDoorIndices.clear();
        this.candies = copyCandyList(this.backup);
    }

    /**
     * Resets the last opened doors for this calendar.
     * @param doors the number of opened doors to get resetted
     * @return if there are enough doors opened to get closed, {@code false} otherwise
     */
    public boolean resetLastDoors(int doors) {
        if (this.openedDoorIndices.size() < doors) {
            return false;
        }

        int count = 0;
        for (int i = currentDay - 1; i >= 0; i--) {
            if (count < doors && this.openedDoorIndices.contains(i)) {
                this.candies.set(i, this.backup.get(i).copy());
                this.openedDoorIndices.remove(i);
                count++;
            }
        }
        return true;
    }

    private List<Candy> copyCandyList(List<Candy> toCopy) {
        List<Candy> copy = new ArrayList<>();
        for (Candy candy : toCopy) {
            copy.add(candy.copy());
        }
        return copy;
    }

    @Override
    public String toString() {
        StringJoiner lines = new StringJoiner(System.lineSeparator());
        StringBuilder currentLine = new StringBuilder();
        for (int i = 0; i < maxDays; i++) {
            currentLine.append(DOOR_REPRESENTATION_FORMAT.formatted(this.openedDoorIndices.contains(i)
                    ? EMPTY_DOOR_CONTENT
                    : CANDY_REPRESENTATION_FORMAT.formatted(this.candies.get(i).getName(), this.candies.get(i).getQuantity())));

            if (i % DOORS_PER_LINE == DOORS_PER_LINE - 1) {
                lines.add(currentLine.toString());
                currentLine.setLength(0);
            }
        }
        if (!currentLine.isEmpty()) {
            lines.add(currentLine.toString());
        }
        return lines.toString();
    }
}

