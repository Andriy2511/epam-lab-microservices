package org.example.finalprojectepamlabapplication.utility;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.security.SecureRandom;

@Slf4j
public class PasswordGenerator {
    private static final int PASSWORD_LENGTH = 10;

    public static String generatePassword() {
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder password = new StringBuilder();

        int[] randomNumbers = secureRandom.ints(PASSWORD_LENGTH, 33, 127).toArray();

        for (int randomNumber : randomNumbers) {
            password.append((char) randomNumber);
        }

        return password.toString();
    }

}
