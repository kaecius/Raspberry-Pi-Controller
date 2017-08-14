package es.canadillas.daniel.raspberrypicontroller.util;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Created by dani on 14/08/2017.
 */

public class Security {

    private static MessageDigest md;
    private static Charset utf8 = StandardCharsets.UTF_8;


    private static void init(){
        try {
            if (md != null){
                md = MessageDigest.getInstance("SHA-256");
            }
            md.reset();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public static String generateSalt(){
        byte[] out = new byte[20];
        new SecureRandom().nextBytes(out);
        BigInteger bi = new BigInteger(out);
        System.out.println("GENERATED SALT" + bi.toString(16));
        return bi.toString(16);
    }

    public static String toHash(String password, String salt){
        String hashed = "";

        init();
        md.update(salt.getBytes(utf8));
        byte[] temp = md.digest(password.getBytes(utf8));
        BigInteger bi = new BigInteger(temp);
        hashed = new BigInteger(temp).toString(16);
        System.out.println("GENERATED HASH" + hashed);

        return hashed;
    }

}
