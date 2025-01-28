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
            return null;
        }

        double basisPreis = 10.0;
        double rabatt = rabattManager.getRabatt(film);
        double endPreis = basisPreis - (basisPreis * rabatt);

        Ticket ticket = new Ticket(kunde, film, platz, endPreis);

        tickets.add(ticket);
        sitzManager.reserviereSitz(film, platz);

        return ticket;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public RabattManager getRabattManager() {
        return rabattManager;
    }

    public SitzManager getSitzManager() {
        return sitzManager;
    }
}

