package com.example.app_bateaux;

public class Bateau {
    private String id;
    private String capacite;
    private String destination;
    private String vide;

    public Bateau(String id, String capacite, String destination, String vide) {
        this.id = id;
        this.capacite = capacite;
        this.destination = destination;
        this.vide = vide;
    }

    public Bateau() {
        this.id = "";
        this.capacite = "";
        this.destination = "";
        this.vide = "";
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCapacite(String capacite) {
        this.capacite = capacite;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setVide(String vide) {
        this.vide = vide;
    }

    public String getId() {
        return id;
    }

    public String getCapacite() {
        return capacite;
    }

    public String getDestination() {
        return destination;
    }

    public String getVide() {
        return vide;
    }

}
