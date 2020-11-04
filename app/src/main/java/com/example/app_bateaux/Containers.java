package com.example.app_bateaux;

public class Containers {
    public String Coord;
    public String Id;
    public String Date;
    public String Poids;
    public String Contenu;
    public String Societe;
    public String Dangers;
    public String Destination;

    public Containers(String id, String coord, String date, String poids) {
        Id = id;
        Coord = coord;
        Date = date;
        Poids = poids;
        Contenu ="";
        Societe ="";
        Dangers ="";
        Destination ="";
    }

    public Containers(String id, String poids, String contenu, String societe, String dangers, String dest, String coord) {
        Id = id;
        Poids = poids;
        Contenu = contenu;
        Societe = societe;
        Dangers = dangers;
        Destination = dest;
        Coord = coord;
        Date = "";
    }

    public String getDestination() {
        return Destination;
    }

    public void setDestination(String destination) {
        Destination = destination;
    }

    public String getCoord() {
        return Coord;
    }

    public String getId() {
        return Id;
    }

    public String getDate() {
        return Date;
    }

    public String getPoids() {
        return Poids;
    }

    public String getContenu() {
        return Contenu;
    }

    public void setContenu(String contenu) {
        Contenu = contenu;
    }

    public String getSociete() {
        return Societe;
    }

    public void setSociete(String societe) {
        Societe = societe;
    }

    public String getDangers() {
        return Dangers;
    }

    public void setDangers(String dangers) {
        Dangers = dangers;
    }

    public void setCoord(String coord) {
        Coord = coord;
    }

    public void setId(String id) {
        Id = id;
    }

    public void setDate(String date) {
        Date = date;
    }

    public void setPoids(String poids) {
        Poids = poids;
    }
}
