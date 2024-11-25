package edu.kit.kastel;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        // :)
        System.out.println(someMethod(3, false));
        System.out.println(someMethod(4, false));
        System.out.println(someMethod(4, true));
        System.out.println(someMethod(5, false));
    }

    public String getMonthName(int monthNr) {
        return switch (monthNr) {
            case 1 -> "Jan";
            case 2 -> "Feb";
            case 3 -> "Mar";
            case 4 -> "Apr";
            case 5 -> "May";
            case 6 -> "Jun";
            case 7 -> "Jul";
            case 8 -> "Aug";
            case 9 -> "Sep";
            case 10 -> "Oct";
            case 11 -> "Nov";
            case 12 -> "Dec";
            default -> "unknown";
        };
    }

    public String getMonthID(String monthName) {
        return switch (monthName) {
            case "Jan" -> "1";
            case "Feb" -> "2";
            case "Mar" -> "3";
            case "Apr" -> "4";
            case "May" -> "5";
            case "Jun" -> "6";
            case "Jul" -> "7";
            case "Aug" -> "8";
            case "Sep" -> "9";
            case "Oct" -> "10";
            case "Nov" -> "11";
            case "Dec" -> "12";
            default -> "unknown";
        };
    }

    public void _do(int x, boolean y) {
        if (!y && x++ < 5) {

        } else if (x < 5) {

        }
    }
    public static String someMethod(int x, boolean y) {
        String s = "";
        if (!y && x++ >= 5) {
            s += "a";
        } else if (x++ >= 5) {
            s += "b";
        }
        switch (s) {
            case "a": return "b" + x;
            case "b":
                s += "a";
                break;
            default:
                s += "c";
        }
        return s + x;
    }

}