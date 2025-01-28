package edu.kit.kastel.debugging.cinema;

import java.util.ArrayList;
import java.util.List;

class KinoManager {
    private List<Ticket> tickets = new ArrayList<>();
    private RabattManager rabattManager;
    private SitzManager sitzManager;

    public KinoManager() {
        this.rabattManager = new RabattManager();
        this.sitzManager = new SitzManager();
    }

    public Ticket erstelleTicket(Kunde kunde, String film, int platz) {
        if (sitzManager.istSitzBelegt(film, platz)) {
            System.out.println("Sitz " + platz + " ist bereits belegt.");
            return null; // Fehler: Es gibt keinen Hinweis, ob das Ticket wirklich null ist
        }

        double basisPreis = 10.0;
        double rabatt = rabattManager.getRabatt(film);
        double endPreis = basisPreis - (basisPreis * rabatt);

        Ticket ticket = new Ticket(kunde, film, platz, endPreis);

        // Fehler: Tickets werden vor Sitzreservierung hinzugefügt
        tickets.add(ticket);
        sitzManager.reserviereSitz(film, platz);

        return ticket;
    }

    public List<Ticket> getTickets() {
        return tickets; // Fehler: Gibt die Referenz direkt zurück, erlaubt Manipulation
    }

    public RabattManager getRabattManager() {
        return rabattManager;
    }

    public SitzManager getSitzManager() {
        return sitzManager;
    }
}

