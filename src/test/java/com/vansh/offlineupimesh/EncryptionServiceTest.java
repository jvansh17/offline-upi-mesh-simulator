package com.vansh.offlineupimesh;

import com.vansh.offlineupimesh.service.EncryptionService;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.security.KeyPair;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class EncryptionServiceTest {

    @Test
    void testEncryptionAndDecryption() throws Exception {

        EncryptionService encryptionService = new EncryptionService();

        SecretKey key = encryptionService.generateAESKey();

        String originalText = "Hello UPI";

        String encryptedText =
                encryptionService.encrypt(originalText, key);

        String decryptedText =
                encryptionService.decrypt(encryptedText, key);

        assertEquals(originalText, decryptedText);
    }

    @Test
    void testRSAKeyPairGeneration() throws Exception {

        EncryptionService encryptionService = new EncryptionService();

        KeyPair keyPair = encryptionService.generateRSAKeyPair();

        assertNotNull(keyPair.getPublic());
        assertNotNull(keyPair.getPrivate());

        assertEquals("RSA", keyPair.getPublic().getAlgorithm());
        assertEquals("RSA", keyPair.getPrivate().getAlgorithm());
    }
}