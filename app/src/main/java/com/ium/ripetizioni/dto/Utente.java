package com.ium.ripetizioni.dto;

import java.util.Objects;
import java.io.Serializable;

public class Utente implements Serializable{
    private int ID;
    private String username;
    private String password;
    private int isAdmin;

    public Utente(int ID, String username, String password, int isAdmin) {
        this.ID = ID;
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getIsAdmin() {
        return isAdmin;
    }

    public void setRuolo(int isAdmin) {
        this.isAdmin = isAdmin;
    }

    @Override
    public String toString() {
        return "Utente{ID=" + ID + ", username=" + username + ", password=" + password + ", isAdmin=" + isAdmin + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Utente utente = (Utente) o;
        return this.ID == utente.ID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID, username, password, isAdmin);
    }
}
