package com.company;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Bogdan95 on 11/30/2016.
 */
public class Factura {
    private Calendar data;
    private List<Vanzare> lista_produse;
    private int suma_totala;

    public Factura(Calendar data, List<Vanzare> lista_produse, int suma_totala) {
        this.data = data;
        this.lista_produse = lista_produse;
        this.suma_totala = suma_totala;
    }

    public Factura() {
        data = new GregorianCalendar();
        lista_produse = new LinkedList<>();
        suma_totala = 0;
    }

    public Calendar getData() {
        return data;
    }

    public void setData(Calendar data) {
        this.data = data;
    }

    public List<Vanzare> getLista_produse() {
        return lista_produse;
    }

    public void setLista_produse(List<Vanzare> lista_produse) {
        this.lista_produse = lista_produse;

    }

    public int getSuma_totala() {
        return suma_totala;
    }

    public void setSuma_totala(int suma_totala) {
        this.suma_totala = suma_totala;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Factura factura = (Factura) o;

        if (suma_totala != factura.suma_totala) return false;
        if (!data.equals(factura.data)) return false;
        return lista_produse.equals(factura.lista_produse);
    }

    @Override
    public int hashCode() {
        int result = data.hashCode();
        result = 31 * result + lista_produse.hashCode();
        result = 31 * result + suma_totala;
        return result;
    }

    public void addProdus(Vanzare p) {
        lista_produse.add(p);
        suma_totala += p.getProdus().getPret_unitar() * p.getCantitate();
    }
}
