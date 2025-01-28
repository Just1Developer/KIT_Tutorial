package edu.kit.kastel.debugging.rechnungsverwaltung;

public class InvoiceItem {
    private String description;
    private double price;

    public InvoiceItem(String description, double price) {
        this.description = description;
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    // Might cause confusion if we think this sets the price
    // but actually doesn't do that properly or at all
    // (Uncommenting or incorrectly using it might reveal bugs)
    // public void setPrice(double price) {
    //    // Some logic was supposed to be here, but let's keep it empty as a trap
    // }

    @Override
    public String toString() {
        return description + " - " + price + " USD";
    }
}
