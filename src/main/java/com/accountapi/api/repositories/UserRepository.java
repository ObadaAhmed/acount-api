package com.accountapi.api.repositories;
import com.accountapi.api.models.Role;
import com.accountapi.api.models.User;
import org.springframework.stereotype.Service;

@Service
public class UserRepository {
   public User findByUsername(String username) {
        User[] users = {new User("admin" , "admin", Role.ADMIN.name() ) , new User("user" , "user" , Role.USER.name())};
        User savedUser = null;
        for (User user: users) {
            if (user.getUsername().equals(username)) {
                savedUser = user;
            }
        }
        return savedUser;
    }
}
