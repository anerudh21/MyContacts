package com.user.model;


//preferences class for customized user experience
import com.user.exceptions.InvalidUserDataException;
public class Preferences {

 private boolean darkMode;
 private boolean emailNotifications;

 public Preferences() {
     this.darkMode = false;
     this.emailNotifications = true;
 }

 public boolean isDarkMode() {
     return darkMode;
 }

 public boolean isEmailNotifications() {
     return emailNotifications;
 }

 public void setDarkMode(boolean darkMode) {
     this.darkMode = darkMode;
 }

 public void setEmailNotifications(boolean emailNotifications) {
     this.emailNotifications = emailNotifications;
 }

 public void updatePreferences(boolean darkMode, boolean emailNotifications)
         throws InvalidUserDataException {

     this.darkMode = darkMode;
     this.emailNotifications = emailNotifications;
 }
}