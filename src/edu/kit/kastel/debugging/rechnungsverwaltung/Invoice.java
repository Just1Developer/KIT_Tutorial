package edu.kit.kastel.debugging.rechnungsverwaltung;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Invoice {

    private static int invoiceCounter = 1;

    private final int invoiceNumber;
    private final LocalDate date;
    private final List<InvoiceItem> items;
    private double totalAmount;
    private boolean finalized;

    public Invoice() {
        this.invoiceNumber = invoiceCounter++;
        this.date = LocalDate.now();
        this.items = new ArrayList<>();
        this.totalAmount = 0.0;
        this.finalized = false;
    }

    public void addItem(InvoiceItem item) {
        items.add(item);
    }

    public void calculateTotal() {
        double sum = 0.0;
        // Intentionally incorrect: using `<=`
        for (int i = 1; i < items.size(); i++) {
            sum += items.get(i).getPrice();
        }
        this.totalAmount = sum;
    }

    public void finalizeInvoice() {
        this.finalized = true;
    }

    public int getInvoiceNumber() {
        return invoiceNumber;
    }

    public LocalDate getDate() {
        return date;
    }

    public List<InvoiceItem> getItems() {
        return items;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public boolean isFinalized() {
        return finalized;
    }

    @Override
    public String toString() {
        return "Invoice #" + invoiceNumber + " from " + date
                + " | finalized: " + finalized
                + " | total: " + totalAmount + " USD";
    }
}
