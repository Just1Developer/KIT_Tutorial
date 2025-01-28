package edu.kit.kastel.debugging.rechnungsverwaltung;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Invoice {

    private static int invoiceCounter = 1;

    private int invoiceNumber;
    private LocalDate date;
    private List<InvoiceItem> items;
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

    /**
     * Bug 1: Off-by-one error. Using i <= items.size() causes an IndexOutOfBoundsException.
     *        Also, if the exception doesn't occur (e.g., empty list?),
     *        it might produce a wrong total or skip logic.
     */
    public void calculateTotal() {
        double sum = 0.0;
        // Intentionally incorrect: using `<=`
        for (int i = 0; i <= items.size(); i++) {
            sum += items.get(i).getPrice();
        }
        this.totalAmount = sum;
    }

    /**
     * Mark the invoice as "finalized" (in real life, you'd store this status somewhere permanent).
     */
    public void finalizeInvoice() {
        this.finalized = true;
    }

    /**
     * Private setter that we might manipulate via Reflection.
     */
    private void setTotalAmount(double newTotal) {
        this.totalAmount = newTotal;
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
