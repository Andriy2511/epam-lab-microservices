package org.example.finalprojectepamlabapplication.utility;

import lombok.extern.slf4j.Slf4j;
import org.example.finalprojectepamlabapplication.model.User;

import java.util.List;

@Slf4j
public class UsernameGenerator {

    public String generateUsername(User user, List<User> users) {
        if(user.getFirstName() != null && user.getLastName() != null) {
            String username = user.getFirstName() + "." + user.getLastName();
            int countOfSimilarNameAndSurname = countCoincidenceWithNameAndSurname(user, users);
            if (countOfSimilarNameAndSurname != 0) {
                username = username + countOfSimilarNameAndSurname;
            }
            return username;
        } else {
            log.warn("Couldn't generate username. First name is {} and last name is {}", user.getFirstName(), user.getLastName());
            return null;
        }
    }

    private int countCoincidenceWithNameAndSurname(User user, List<User> users) {
        int count = 0;
        for (User u : users) {
            if (u.getFirstName().equals(user.getFirstName()) && u.getLastName().equals(user.getLastName())) {
                count++;
            }
        }
        return count;
    }

}
