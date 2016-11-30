package com.company;

/**
 * Created by Bogdan95 on 11/30/2016.
 */
public class Vanzare {
    private Produs produs;
    private int cantitate;

    public Vanzare(Produs produs, int cantitate) {
        this.produs = produs;
        this.cantitate = cantitate;
    }

    public Produs getProdus() {
        return produs;
    }

    public int getCantitate() {
        return cantitate;
    }
}
