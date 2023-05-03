package org.victorpiles.escacs.api.security;

import lombok.SneakyThrows;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

/**
 * Encarregat d'encriptar cadenes de text.
 *
 * @author Víctor Piles
 * @version 1.0
 */
public class PasswordEncoder {

    /**
     * Encripta la cadena passada per paràmetre amb l'algoritme PBKDF2.
     *
     * @param password La contrasenya en text plà.
     *
     * @return La contrasenya encriptada.
     */
    @SneakyThrows(value = {NoSuchAlgorithmException.class, InvalidKeySpecException.class})
    public static String encode(String password) {
        char[] passwordCharArray = password.toCharArray();
        byte[] salt = password.getBytes();
        int iterationCount = 65536;
        int keyLength = 128;

        KeySpec spec = new PBEKeySpec(passwordCharArray, salt, iterationCount, keyLength);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

        byte[] encoded = factory.generateSecret(spec).getEncoded();

        return new String(encoded, StandardCharsets.UTF_8);
    }

    /**
     * Compara una contrasenya en clar amb una contrasenya encriptada.
     *
     * @param plainPassword   Contrasenya en clar.
     * @param encodedPassword Contrasenya encriptada.
     *
     * @return Si les contrasenyes coincideixen.
     */
    public static boolean match(String plainPassword, String encodedPassword) {
        return encodedPassword.equals(encode(plainPassword));
    }
}