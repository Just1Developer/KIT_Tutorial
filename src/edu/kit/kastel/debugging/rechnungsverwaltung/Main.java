package edu.kit.kastel.debugging.rechnungsverwaltung;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("===== Creating First Invoice =====");
        Invoice invoice1 = new Invoice();
        invoice1.addItem(new InvoiceItem("Pencils (10-pack)", 4.50));
        invoice1.addItem(new InvoiceItem("Notebook", 2.75));
        invoice1.addItem(new InvoiceItem("USB-Stick 32GB", 12.99));
        invoice1.calculateTotal();
        System.out.println("Invoice after calculation: " + invoice1);


        System.out.println("\n=== Step 2: Attempt to fix rounding differences ===");
        InvoiceManagement.fixRoundingDifferences(invoice1);
        System.out.println("Invoice after rounding fix: " + invoice1);


        System.out.println("\n=== Step 3: Finalize invoice with correction ===");
        InvoiceManagement.finalizeInvoiceWithCorrection(invoice1);
        System.out.println("Invoice after finalization: " + invoice1);


        System.out.println("\n===== Creating Second Invoice (with user input) =====");
        Invoice invoice2 = new Invoice();

        // Ask user for input
        System.out.println("We'll add 4 items. Please enter a numeric price.");

        for (int i = 1; i <= 4; i++) {
            System.out.print("Enter description for item #" + i + ": ");
            String desc = scanner.nextLine();

            System.out.print("Enter price for item #" + i + ": ");
            double price = Double.parseDouble(scanner.nextLine());
            invoice2.addItem(new InvoiceItem(desc, price));
        }
        scanner.close();

        System.out.println("\n=== Calculate total for second invoice ===");
        invoice2.calculateTotal();
        System.out.println("Invoice2 before any rounding or finalizing: " + invoice2);

        System.out.println("\n=== Fix rounding differences for second invoice ===");
        InvoiceManagement.fixRoundingDifferences(invoice2);
        System.out.println("Invoice2 after rounding fix: " + invoice2);

        System.out.println("\n=== Finalize second invoice with correction ===");
        InvoiceManagement.finalizeInvoiceWithCorrection(invoice2);
        System.out.println("Final state of invoice2: " + invoice2);
    }
}
