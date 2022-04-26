package com.ium.ripetizioni.dto;
import java.io.Serializable;

public class Docenza implements Serializable{

    private Corso corso;
    private Docente docente;
    private int attivo;

    public Docenza(Corso corso, Docente docente) {
        this.corso = corso;
        this.docente = docente;
        this.attivo = 1;
    }

    public Docenza() {
    }

    public Corso getCorso() {
        return corso;
    }

    public void setCorso(Corso corso) {
        this.corso = corso;
    }

    public Docente getDocente() {
        return docente;
    }

    public void setDocente(Docente docente) {
        this.docente = docente;
    }

    @Override
    public String toString() {
        return "Docenza{" + "corso=" + corso + ", docente=" + docente + '}';
    }

}
