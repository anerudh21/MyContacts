package com.user.encryption;


//password hashing class which uses message digest and SHA-256
import java.security.*;
public class PasswordHashing {
	public static String hashPassword(String password) {
     try {
         MessageDigest md = MessageDigest.getInstance("SHA-256");
         byte[] hashedBytes = md.digest(password.getBytes());

         StringBuilder sb = new StringBuilder();
         for (byte b : hashedBytes)
             sb.append(String.format("%02x", b));
         
         return sb.toString();

     } catch (NoSuchAlgorithmException e) {
         throw new RuntimeException("Error hashing password");
     }
 }
}