package ru.hlynov.oit.simplesign.customfields;

public class Carrier {
    private String userName;
    private String hashCalc;

    public Carrier(String userName, String hashCalc) {
        this.userName = userName;
        this.hashCalc = hashCalc;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getHashCalc() {
        return hashCalc;
    }

    public void setHashCalc(String hashCalc) {
        this.hashCalc = hashCalc;
    }

    @Override
    public String toString() {
        return userName + "###" + hashCalc;
    }
}
