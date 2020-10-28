package com.example.app_bateaux;

public class Containers {
    public String Coord;
    public String Id;
    public String Date;
    public String Poids;

    public Containers(String coord, String id, String date, String poids) {
        Coord = coord;
        Id = id;
        Date = date;
        Poids = poids;
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
