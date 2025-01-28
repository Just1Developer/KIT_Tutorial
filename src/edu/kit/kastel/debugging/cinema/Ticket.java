package edu.kit.kastel.debugging.cinema;

class Ticket {
    private Kunde kunde;
    private String film;
    private int platz;
    private double preis;

    public Ticket(Kunde kunde, String film, int platz, double preis) {
        this.kunde = kunde;
        this.film = film;
        this.platz = platz;
        this.preis = preis;
    }

    public Kunde getKunde() {
        return kunde;
    }

    public String getFilm() {
        return film;
    }

    public int getPlatz() {
        return platz;
    }

    public double getPreis() {
        return preis;
    }

    public void setPreis(double preis) {
        this.preis = preis;
    }
}
