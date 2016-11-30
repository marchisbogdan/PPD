package com.company;

/**
 * Created by Bogdan95 on 11/30/2016.
 */
public class Produs implements Cloneable{
    private String nume;
    private int cod;
    private int pret_unitar;
    private String unit_masura;
    private int stoc;

    public int getStoc() {
        return stoc;
    }

    public void setStoc(int stoc) {
        this.stoc = stoc;
    }

    public Produs(String nume, int cod, int pret_unitar, String unit_masura, int stoc) {
        this.nume = nume;
        this.cod = cod;
        this.pret_unitar = pret_unitar;
        this.unit_masura = unit_masura;
        this.stoc = stoc;

    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public int getCod() {
        return cod;
    }

    public void setCod(int cod) {
        this.cod = cod;
    }

    public int getPret_unitar() {
        return pret_unitar;
    }

    public void setPret_unitar(int pret_unitar) {
        this.pret_unitar = pret_unitar;
    }

    public String getUnit_masura() {
        return unit_masura;
    }

    public void setUnit_masura(String unit_masura) {
        this.unit_masura = unit_masura;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Produs produs = (Produs) o;

        if (cod != produs.cod) return false;
        if (pret_unitar != produs.pret_unitar) return false;
        if (stoc != produs.stoc) return false;
        if (!nume.equals(produs.nume)) return false;
        return unit_masura.equals(produs.unit_masura);
    }

    @Override
    public int hashCode() {
        int result = nume.hashCode();
        result = 31 * result + cod;
        result = 31 * result + pret_unitar;
        result = 31 * result + unit_masura.hashCode();
        result = 31 * result + stoc;
        return result;
    }

    @Override
    public String toString() {
        return "Produs{" +
                "nume='" + nume + '\'' +
                ", cod=" + cod +
                ", pret_unitar=" + pret_unitar +
                ", unit_masura='" + unit_masura + '\'' +
                ", stoc=" + stoc +
                '}';
    }

    public Object clone(){
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
