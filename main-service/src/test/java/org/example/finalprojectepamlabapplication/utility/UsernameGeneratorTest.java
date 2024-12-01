package org.example.finalprojectepamlabapplication.utility;

import org.example.finalprojectepamlabapplication.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.List;

public class UsernameGeneratorTest {

    List<User> defaultUsers;
    UsernameGenerator usernameGenerator;

    @BeforeEach
    public void setUp() {
        defaultUsers = new ArrayList<>();
        usernameGenerator = new UsernameGenerator();
        defaultUsers.add(new User(1L, "John", "Snow", "John.Snow", "12345",true, null,null));
        defaultUsers.add(new User(2L, "Martin", "Luther", "Martin.Luther", "12345",true, null,null));
        defaultUsers.add(new User(3L, "Martin", "Luther", "Martin.Luther1", "12345",true, null,null));
        defaultUsers.add(new User(4L, "Martin", "Luther", "Martin.Luther2", "12345",false, null,null));
        defaultUsers.add(new User(5L, "Joe", "Biden", "Joe.Biden", "12345",true, null,null));
        defaultUsers.add(new User(6L, "Joe", "Biden", "Joe.Biden1", "12345",true, null,null));
        defaultUsers.add(new User(7L, "John", "Luther", "John.Luther", "12345",true, null,null));
        defaultUsers.add(new User(8L, "Martin", "Snow", "Martin.Snow", "12345",true, null,null));
        defaultUsers.add(new User(9L, "Ivan", "Ivanov", "Ivan.Ivanov", "12345",true, null,null));
    }

    @Test
    public void testGenerateUsernameWithoutSameFirstAndLastName() {
        User user = new User();
        user.setFirstName("Robert");
        user.setLastName("Greene");
        Assertions.assertEquals("Robert.Greene", usernameGenerator.generateUsername(user, defaultUsers));
    }

    @Test
    public void testGenerateUsernameWithSameFirstName() {
        User user = new User();
        user.setFirstName("Joe");
        user.setLastName("Churchill");
        Assertions.assertEquals("Joe.Churchill", usernameGenerator.generateUsername(user, defaultUsers));
    }

    @Test
    public void testGenerateUsernameWithSameLastName() {
        User user = new User();
        user.setFirstName("Winston");
        user.setLastName("Ivanov");
        Assertions.assertEquals("Winston.Ivanov", usernameGenerator.generateUsername(user, defaultUsers));
    }

    @Test
    public void testGenerateUsernameWithSameFirstAndLastName() {
        User user = new User();
        user.setFirstName("Ivan");
        user.setLastName("Ivanov");
        Assertions.assertEquals("Ivan.Ivanov1", usernameGenerator.generateUsername(user, defaultUsers));
    }

    @Test
    public void testGenerateUsernameWithSameFirstAndLastNameIfSuffixHasAlreadyAdded() {
        User user = new User();
        user.setFirstName("Martin");
        user.setLastName("Luther");
        Assertions.assertEquals("Martin.Luther3", usernameGenerator.generateUsername(user, defaultUsers));
    }
}
