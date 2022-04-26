package com.ium.ripetizioni.dto;

import java.util.Objects;
import java.io.Serializable;

public class Corso implements Serializable{

    private int ID;
    private String titolo;
    private int attivo;

    public Corso(int ID, String titolo) {
        this.ID = ID;
        this.titolo = titolo;
        this.attivo = 1;
    }

    public Corso() {
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public void setAttivo(){
        this.attivo = 1;
    }

    public void setNotAttivo(){
        this.attivo = 0;
    }

    @Override
    public String toString() {
        return "Corso{ID=" + ID + ", titolo=" + titolo + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Corso corso = (Corso) o;
        return this.ID == corso.ID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID, titolo);
    }
}
