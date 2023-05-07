package com.example.examentp;

public class Element {
    private String currency;
    private String somme;


    public Element(String currency, String somme) {
        this.currency = currency;
        this.somme = somme;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getSomme() {
        return somme;
    }

    public void setSomme(String somme) {
        this.somme = somme;
    }
}

