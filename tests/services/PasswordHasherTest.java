package services;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class PasswordHasherTest {
    @Test
    void hashesAndVerifiesPassword() {
        String hash = PasswordHasher.hash("secure-pass");
        assertNotEquals("secure-pass", hash);
        assertTrue(PasswordHasher.verify("secure-pass", hash));
        assertFalse(PasswordHasher.verify("wrong-pass", hash));
    }
}
