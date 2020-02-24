package com.water.melon.net.utils;

import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

//import java.util.Base64;

/**
 * @author vipin.cb , vipin.cb@experionglobal.com <br>
 * Sep 27, 2013, 5:18:34 PM <br>
 * Package:- <b>com.veebow.util</b> <br>
 * Project:- <b>Veebow</b>
 * <p>
 */
public class AESCipherforJiaMi {

    static String key = "AUCj067VsOWgmyFR";
    static String iv = "MNZuDexL4UHGCfyW";


    public static String encrypt(String data) {
        try {

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            int blockSize = cipher.getBlockSize();

            byte[] dataBytes = data.getBytes();
            int plaintextLength = dataBytes.length;
            if (plaintextLength % blockSize != 0) {
                plaintextLength = plaintextLength + (blockSize - (plaintextLength % blockSize));
            }

            byte[] plaintext = new byte[plaintextLength];
            System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);

            SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
            IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());

            cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
            byte[] encrypted = cipher.doFinal(plaintext);

            return new String(Base64.encode(encrypted, Base64.DEFAULT));

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String desEncrypt(String data) {
        try {
            data = new String(Base64.decode(data.getBytes(), Base64.DEFAULT));
            byte[] encrypted1 = Base64.decode(data, Base64.DEFAULT);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
            IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());

            cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);

            byte[] original = cipher.doFinal(encrypted1);
            String originalString = new String(original);
            return originalString;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String desEncrypt1(String data) {
        try {
//            data = new String(Base64.decode(data.getBytes(), Base64.DEFAULT));
            byte[] encrypted1 = Base64.decode(data, Base64.DEFAULT);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
            IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());

            cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);

            byte[] original = cipher.doFinal(encrypted1);
            String originalString = new String(original);
            return originalString;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String encrypt(String sSrc, String encodingFormat) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
        byte[] raw = key.getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        IvParameterSpec iva = new IvParameterSpec(iv.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iva);
        byte[] encrypted = cipher.doFinal(sSrc.getBytes(encodingFormat));
        return new String(Base64.encode(encrypted, Base64.DEFAULT));//使用BASE64做轉碼。
    }
}
