package edu.kit.kastel.debugging.rechnungsverwaltung;

import java.lang.reflect.Field;
import java.util.Scanner;

public class InvoiceManagement {

    /**
     * Bug 2: We "only want to check or round" the total,
     * but we actually modify it by reflection,
     * which can lead to unexpected changes in the main invoice object.
     */
    public static void fixRoundingDifferences(Invoice invoice) {
        double original = invoice.getTotalAmount();
        double rounded = Math.round(original * 100) / 100.0;

        // Let's subtract 0.05 incorrectly (maybe we intended 0.005).
        // This is a logic bug leading to a wrong total.
        rounded -= 0.05;

        // Now we override the private field in the invoice using Reflection:
        try {
            Field totalAmountField = Invoice.class.getDeclaredField("totalAmount");
            totalAmountField.setAccessible(true);
            totalAmountField.setDouble(invoice, rounded);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Bug 3: We finalize the invoice but then do more "adjustments"
     * that might be unexpected if we believe finalize means "no more changes."
     */
    public static void finalizeInvoiceWithCorrection(Invoice invoice) {
        // Step 1: Mark as finalized
        invoice.finalizeInvoice();

        // Step 2: Possibly "fix" or "round" again,
        // even though it's now "final"
        fixRoundingDifferences(invoice);
    }
}
