package ru.hlynov.oit.simplesign.customfields;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Carrier {
    private String username;
    private String signdate;
    private String hashcalc;

    private static final Logger log = LoggerFactory.getLogger(Carrier.class);

//    public Carrier() {
//        log.warn(" ======== constr 1");
//    }


    public Carrier(String username, String signdate, String hashcalc) {
        this.username = username;
        this.signdate = signdate;
        this.hashcalc = hashcalc;

//        log.warn(" ======== constr 2");
//
//        if (username == null) {
//            log.warn(" name = null");
//        } else {
//            log.warn(" name = " + username);
//        }
//
//        if (hashcalc == null) {
//            log.warn(" hashCalc = null");
//        } else {
//            log.warn(" hashCalc = " + hashcalc);
//        }

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSigndate() {
        return signdate;
    }

    public void setSigndate(String signdate) {
        this.signdate = signdate;
    }

    public String getHashcalc() {
        return hashcalc;
    }

    public void setHashcalc(String hashcalc) {
        this.hashcalc = hashcalc;
    }

    @Override
    public String toString() {
        return username + "###" + signdate + "###" + hashcalc;
    }
}
