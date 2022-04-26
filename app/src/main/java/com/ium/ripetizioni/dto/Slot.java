package com.ium.ripetizioni.dto;

import java.util.Objects;
import java.io.Serializable;

public class Slot implements Serializable{

    private int giorno;
    private int orario;
    private String giornoString;
    private String orarioString;

    public Slot(int giorno, int orario){
        this.giorno = giorno;
        this.orario = orario;
        this.giornoString = "";
        this.orarioString = "";
    }

    public int getGiorno(){return this.giorno;}
    public int getOrario(){return this.orario;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Slot slot = (Slot) o;
        return this.giorno == slot.giorno && this.orario == slot.orario;
    }

    public String getGiornoString() {
        return giornoString;
    }

    public void setGiornoString(String giornoString) {
        this.giornoString = giornoString;
    }

    public String getOrarioString() {
        return orarioString;
    }

    public void setOrarioString(String orarioString) {
        this.orarioString = orarioString;
    }

    @Override
    public String toString() {
        return "Slot{" + "giorno=" + giornoString + ", orario=" + orarioString + '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(giorno, orario);
    }
}
