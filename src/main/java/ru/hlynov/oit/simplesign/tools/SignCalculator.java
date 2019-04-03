package ru.hlynov.oit.simplesign.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SignCalculator {

    private static final Logger log = LoggerFactory.getLogger(SignCalculator.class);

    public static String getStringCheckusm(String stSign) {

        String st = stSign;
        if (stSign == null) {
            st = "";
        }

        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
            log.error("unsupported algorithm exception", e);
            return null;
        }

        byte[] retVal = null;
        try {
            retVal = md.digest(st.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
            log.error("unsupported encoding exception", e);
            return null;
        }

        return bytesToDec(retVal);

    }


    public static String getFileCheckusm(String fileName) throws IOException {

        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
            log.error("unsupported algorithm exception", e);
            return null;
        }

        File file = new File(fileName);


        //Get file input stream for reading the file content
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
        } catch (FileNotFoundException e) {
//            e.printStackTrace();
            log.error("file not found", e);
            return null;
        }

        //Create byte array to read data in chunks
        byte[] byteArray = new byte[1024];
        int bytesCount = 0;

        //Read file data and update in message digest
        while ((bytesCount = fis.read(byteArray)) != -1) {
            md.update(byteArray, 0, bytesCount);
        };

        //close the stream; We don't need it now.
        fis.close();

        //Get the hash's bytes
        byte[] bytes = md.digest();

        return bytesToDec(bytes);
    }


    private static String bytesToDec(byte[] byteHash) {
        //This bytes[] has bytes in decimal format;
        //Convert it to hexadecimal format
        StringBuilder sb = new StringBuilder();

        for(int i=0; i< byteHash.length; i++)
        {
            sb.append(Integer.toString((byteHash[i] & 0xff) + 0x100, 16).substring(1));
        }

        //return complete hash
        return sb.toString();
    }
}
