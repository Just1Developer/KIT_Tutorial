package edu.kit.kastel.debugging.rechnungsverwaltung;

import java.lang.reflect.Field;

public class InvoiceManagement {

    public static void fixRoundingDifferences(Invoice invoice) {
        double original = invoice.getTotalAmount();
        double rounded = Math.round(original * 100) / 100.0;
        rounded -= 0.05;

        // Now we override the private field in the invoice using Reflection:
        try {
            Field totalAmountField = Invoice.class.getDeclaredField("totalAmount");
            totalAmountField.setAccessible(true);
            totalAmountField.setDouble(invoice, rounded);
        } catch (NoSuchFieldException | IllegalAccessException ignored) { }
    }

    public static void finalizeInvoiceWithCorrection(Invoice invoice) {
        invoice.finalizeInvoice();
        fixRoundingDifferences(invoice);
    }
}
