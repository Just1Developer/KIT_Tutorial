package edu.kit.kastel.debugging.rechnungsverwaltung;

public class InvoiceItem {
    private final String description;
    private final double price;

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

    @Override
    public String toString() {
        return description + " - " + price + " USD";
    }
}
