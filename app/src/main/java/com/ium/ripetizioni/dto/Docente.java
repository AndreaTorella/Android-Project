package com.ium.ripetizioni.dto;

import java.util.Objects;
import java.io.Serializable;

public class Docente implements Serializable{

    private int ID;
    private String nome;
    private String cognome;
    private int attivo;

    public Docente(int ID, String nome, String cognome) {
        this.ID = ID;
        this.nome = nome;
        this.cognome = cognome;
        this.attivo = 1;
    }

    public Docente() {}

    public int getID() {
        return ID;
    }

    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public void setAttivo(){
        this.attivo = 1;
    }

    public void setNotAttivo(){
        this.attivo = 0;
    }

    @Override
    public String toString() {
        return "Docente{ID=" + ID + ", nome=" + nome + ", cognome=" + cognome + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Docente docente = (Docente) o;
        return this.ID == docente.ID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID, nome, cognome);
    }
}
