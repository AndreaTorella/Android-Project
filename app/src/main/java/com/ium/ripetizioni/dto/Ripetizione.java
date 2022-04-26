package com.ium.ripetizioni.dto;
import java.io.Serializable;

public class Ripetizione implements Serializable{
    private Corso corso;
    private Docente docente;
    private Slot slot;

    public Ripetizione(Corso corso, Docente docente, Slot slot) {
        this.corso = corso;
        this.docente = docente;
        this.slot = slot;
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

    public Slot getSlot() {
        return slot;
    }

    public void setSlot(Slot slot) {
        this.slot = slot;
    }

    @Override
    public String toString() {
        return "Ripetizione {" + "corso=" + corso + ", docente=" + docente + " , slot=" + slot + "}";
    }
}
