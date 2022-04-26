package com.ium.ripetizioni.dto;
import java.io.Serializable;

public class Prenotazione implements Serializable{

    private Integer ID;
    private Corso corso;
    private Docente docente;
    private Utente utente;
    private int stato; // 1 : attiva    2 : effettuata  3 : Disdetta POSSO FARLO COME ENUM
    private Slot slot;

    public Prenotazione(Integer ID, Corso corso, Docente docente, Utente utente, int stato, Slot slot) {
        this.ID = ID;
        this.corso = corso;
        this.docente = docente;
        this.utente = utente;
        this.stato = stato;
        this.slot = slot;
    }

    public Prenotazione() {
    }

    @Override
    public String toString() {
        return "Prenotazione{ID=" + ID + ", corso=" + corso + ", docente=" + docente + ", utente=" + utente + ", stato=" + stato + ", slot=" + slot + '}';
    }

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
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

    public Utente getUtente() {
        return utente;
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
    }

    public int getStato() {
        return stato;
    }

    public void setStato(int stato) {
        this.stato = stato;
    }

    public Slot getSlot() {
        return this.slot;
    }

    public void setSlot(Slot slot) {
        this.slot = slot;
    }

    /*
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + Objects.hashCode(this.ID);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Prenotazione other = (Prenotazione) obj;
        if (!Objects.equals(this.ID, other.ID)) {
            return false;
        }
        return true;
    }
     */


}