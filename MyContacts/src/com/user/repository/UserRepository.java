package com.user.repository;

//repository class for simulating a data storage service
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import com.user.model.User;

public class UserRepository {

 private final Map<String, User> users = new HashMap<>();

 public void save(User user) {
     users.put(user.getEmail(), user);
 }

 public Optional<User> findByEmail(String email) {
     return Optional.ofNullable(users.get(email));
 }
}