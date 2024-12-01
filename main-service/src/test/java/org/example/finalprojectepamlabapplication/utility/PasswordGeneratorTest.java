package org.example.finalprojectepamlabapplication.utility;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PasswordGeneratorTest {
    @Test
    public void testPasswordLength() {
        String password = PasswordGenerator.generatePassword();
        Assertions.assertEquals(10, password.length());
    }

    @Test
    public void testPasswordCharactersInAsciiRange() {
        String password = PasswordGenerator.generatePassword();
        for (char c : password.toCharArray()) {
            Assertions.assertTrue(c >= 33 && c <= 126);
        }
    }

    @Test
    public void testPasswordRandomness() {
        String firstPassword = PasswordGenerator.generatePassword();
        String secondPassword = PasswordGenerator.generatePassword();
        Assertions.assertNotEquals(firstPassword, secondPassword);
    }
}
