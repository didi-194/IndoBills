package com.example.uas_mobprog.Class;

public class Transaction {
    public String id, name, number, amount, via;
    public int imageId;

    public Transaction(){}

    public Transaction(String id, String name, String number, String amount, String via, int imageId) {
        this.id = id;
        this.name = name;
        this.number = number;
        this.amount = amount;
        this.via = via;
        this.imageId = imageId;
    }

}
