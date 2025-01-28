package edu.kit.kastel.debugging.cinema;

public class Main {
    public static void main(String[] args) {
        KinoManager kinoManager = new KinoManager();

        // Rabatt setzen
        kinoManager.getRabattManager().setRabatt("Matrix", 20);

        // Tickets erstellen
        Kunde alice = new Kunde("Alice", 25);
        Kunde bob = new Kunde("Bob", 30);

        Ticket ticket1 = kinoManager.erstelleTicket(alice, "Matrix", 1);
        Ticket ticket2 = kinoManager.erstelleTicket(bob, "Matrix", 1); // Bug: Platz doppelt reserviert?

        // Tickets prüfen
        System.out.println("Tickets:");
        for (Ticket ticket : kinoManager.getTickets()) {
            System.out.println(ticket.getKunde().getName() + " hat Ticket für " + ticket.getFilm() + ", Platz " + ticket.getPlatz() + " für " + ticket.getPreis() + " Euro.");
        }

        // Rabatt nachträglich ändern (Bug: wirkt sich nicht auf bestehende Tickets aus)
        kinoManager.getRabattManager().setRabatt("Matrix", 50);

        System.out.println("Sitzprüfung: Platz 1 für Matrix belegt? " + kinoManager.getSitzManager().istSitzBelegt("Matrix", 1));
    }
}
