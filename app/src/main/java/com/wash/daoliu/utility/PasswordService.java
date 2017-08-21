package com.wash.daoliu.utility;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by rogerlzp on 15/11/25.
 */
public class PasswordService {

    private static PasswordService instance;
    private PasswordService()
    {}

    public synchronized String encrypt(String plaintext)
    {
        MessageDigest md = null;
        try
        {
            md = MessageDigest.getInstance("MD5"); //step 2
        }
        catch(NoSuchAlgorithmException e)
        {
            System.out.println("message" + e.getMessage());
        }
        try
        {
            md.update(plaintext.getBytes("UTF-8")); //step 3
        }
        catch(UnsupportedEncodingException e)
        {
            System.out.println("message"+e.getMessage());
        }
        // Base64 a;


        byte raw[] = md.digest(); //step 4

        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < raw.length; i++) {
            int val = ((int) raw[i]) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }

        return hexValue.toString();
        //String hash = Base64.encodeToString(raw, Base64.DEFAULT); //step 5
        //return hash; //step 6
    }


    public static synchronized PasswordService getInstance()
    {
        if (instance == null)
        {
            instance =  new PasswordService();
        }

        return instance;

    }
}
