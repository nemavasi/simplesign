package ru.hlynov.oit.simplesign.rest;

import javax.xml.bind.annotation.*;
@XmlRootElement(name = "message")
@XmlAccessorType(XmlAccessType.FIELD)
public class SignRestModel {

    @XmlElement(name = "value")
    private String message;

    public SignRestModel() {
    }

    public SignRestModel(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}