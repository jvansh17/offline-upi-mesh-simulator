package com.vansh.offlineupimesh.service;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

import java.security.KeyPair;
import java.security.KeyPairGenerator;

@Service
public class EncryptionService {

    public SecretKey generateAESKey() throws NoSuchAlgorithmException {

        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");

        keyGenerator.init(256);

        return keyGenerator.generateKey();
    }

    public String encrypt(String plainText, SecretKey secretKey) throws Exception {

        byte[] iv = new byte[12];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(iv);

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");

        GCMParameterSpec parameterSpec =
                new GCMParameterSpec(128, iv);

        cipher.init(
                Cipher.ENCRYPT_MODE,
                secretKey,
                parameterSpec
        );

        byte[] encryptedBytes =
                cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

        byte[] result = new byte[iv.length + encryptedBytes.length];

        System.arraycopy(iv, 0, result, 0, iv.length);
        System.arraycopy(
                encryptedBytes,
                0,
                result,
                iv.length,
                encryptedBytes.length
        );

        return Base64.getEncoder().encodeToString(result);
    }

    public String decrypt(String encryptedText, SecretKey secretKey) throws Exception {

        byte[] decoded = Base64.getDecoder().decode(encryptedText);

        byte[] iv = new byte[12];
        byte[] encryptedBytes = new byte[decoded.length - 12];

        System.arraycopy(decoded, 0, iv, 0, 12);
        System.arraycopy(decoded, 12, encryptedBytes, 0, encryptedBytes.length);

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");

        GCMParameterSpec parameterSpec =
                new GCMParameterSpec(128, iv);

        cipher.init(
                Cipher.DECRYPT_MODE,
                secretKey,
                parameterSpec
        );

        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

    public KeyPair generateRSAKeyPair() throws Exception {

        KeyPairGenerator keyPairGenerator =
                KeyPairGenerator.getInstance("RSA");

        keyPairGenerator.initialize(2048);

        return keyPairGenerator.generateKeyPair();
    }
}