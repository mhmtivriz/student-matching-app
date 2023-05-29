package com.example.helloworldapplication.entities;

public enum RequestStatus {

    NOTRESPONSEYET("Henüz Cevap Verilmedi"),
    ACCEPTED("Kabul Edildi"),
    DENIED("Reddedildi");

    private String displayText;

    RequestStatus(String displayText){
        this.displayText = displayText;
    }

    @Override
    public String toString() {
        return displayText;
    }

}
