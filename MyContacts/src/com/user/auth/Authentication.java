package com.user.auth;

//authentication class for abstraction, polymorphism and loose coupling
import java.util.Optional;
import com.user.model.User;

public interface Authentication {
 Optional<User> login(String email, String password);
}
