package org.example.finalprojectepamlabapplication.service.implementation;

import lombok.extern.slf4j.Slf4j;
import org.example.finalprojectepamlabapplication.DTO.modelDTO.TraineeDTO;
import org.example.finalprojectepamlabapplication.DTO.modelDTO.TrainerDTO;
import org.example.finalprojectepamlabapplication.DTO.modelDTO.UserDTO;
import org.example.finalprojectepamlabapplication.repository.UserRepository;
import org.example.finalprojectepamlabapplication.model.User;
import org.example.finalprojectepamlabapplication.security.GumUserDetails;
import org.example.finalprojectepamlabapplication.service.UserService;
import org.example.finalprojectepamlabapplication.utility.PasswordGenerator;
import org.example.finalprojectepamlabapplication.utility.UsernameGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final UsernameGenerator usernameGenerator;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.usernameGenerator = new UsernameGenerator();
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = userRepository.findAll();
        log.info("Searching all users. Total users: {}", users.size());
        return users;
    }

    @Override
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow();

        return getUserDTO(user);
    }

    @Override
    public UserDTO getUserByUsername(String username) {
        User user = userRepository.getUserByUsername(username).orElseThrow();

        return getUserDTO(user);
    }

    @Override
    public UserDTO updateUser(UserDTO userDTO) {
        User user = userRepository.findById(userDTO.getId()).orElseThrow();
        if(!userDTO.getFirstName().equals(user.getFirstName()) || !userDTO.getLastName().equals(user.getLastName())) {
            user.setFirstName(userDTO.getFirstName());
            user.setLastName(userDTO.getLastName());
            user.setUsername(usernameGenerator.generateUsername(user, userRepository.findAll()));
        }
        userRepository.save(user);
        return UserDTO.toDTO(user);
    }

    @Override
    public UserDTO updateUserPassword(UserDTO userDTO, String password) {
        User user = userRepository.findById(userDTO.getId()).orElseThrow();
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
        return UserDTO.toDTO(user);
    }

    @Override
    public UserDTO changeActiveStatus(Long id){
        User user = userRepository.findById(id).orElseThrow();
        user.setActive(!user.isActive());
        userRepository.save(user);
        return UserDTO.toDTO(user);
    }

    @Override
    public User setUsernameAndPasswordForUser(User user){
        user.setUsername(usernameGenerator.generateUsername(user, userRepository.findAll()));
        String password = PasswordGenerator.generatePassword();
        user.setPassword(passwordEncoder.encode(password));
        return user;
    }

    @Override
    public boolean isOldPasswordSimilarToCurrentPassword(Long id, String currentPassword){
        UserDTO userDTO = getUserById(id);
        return passwordEncoder.matches(currentPassword, userDTO.getPassword());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.getUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        return new GumUserDetails(user);
    }

    private UserDTO getUserDTO(User user){
        UserDTO userDTO = UserDTO.toDTO(user);

        Optional.ofNullable(user.getTrainee())
                .ifPresent(trainee -> userDTO.toBuilder().traineeDTO(TraineeDTO.toDTO(trainee)).build());

        Optional.ofNullable(user.getTrainer())
                .ifPresent(trainer -> userDTO.toBuilder().trainerDTO(TrainerDTO.toDTO(trainer)).build());

        return userDTO;
    }
}
