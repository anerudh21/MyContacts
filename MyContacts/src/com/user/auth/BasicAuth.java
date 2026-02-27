package com.user.auth;


import java.util.Optional;
import com.user.encryption.PasswordHashing;
import com.user.model.User;
import com.user.repository.UserRepository;

// basic authentication class
public class BasicAuth implements Authentication {

    private final UserRepository repository;

    public BasicAuth(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<User> login(String email, String password) {

        Optional<User> optionalUser = repository.findByEmail(email);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            String hashedInput = PasswordHashing.hashPassword(password);

            if (user.getPassword().equals(hashedInput)) return Optional.of(user);
        }
        return Optional.empty();
    }
}