package com.github.shawramland.services;
import com.github.shawramland.utils.PasswordService;
public class UserService {
    private static String storedHashedPassword;
    public static void registerUser(String plainTextPassword) {
        storedHashedPassword = PasswordService.hashPassword(plainTextPassword);
    }
    public static boolean loginUser(String plainTextPassword) {
        return PasswordService.checkPassword(plainTextPassword, storedHashedPassword);
    }
}
