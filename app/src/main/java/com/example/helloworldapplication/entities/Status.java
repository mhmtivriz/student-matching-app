package com.example.helloworldapplication.entities;

public enum Status {
    NULL("Seçiniz"),
    HOME("Ev Arıyor."),
    FRIEND("Arkadaş Arıyor."),
    NONE("Aramıyor");

    private String displayText;

    Status(String displayText){
        this.displayText = displayText;
    }

    @Override
    public String toString() {
        return displayText;
    }
}
