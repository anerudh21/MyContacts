package com.user.session;

//session manager class
import com.user.model.User;
public class SessionManager {

 private User currentUser;

 public void login(User user) {
     this.currentUser = user;
 }

 public void logout() {
     this.currentUser = null;
 }

 public boolean isLoggedIn() {
     return currentUser != null;
 }

 public User getCurrentUser() {
     return currentUser;
 }
}
