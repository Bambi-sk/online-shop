package com.example.demo.services;

import com.example.demo.entities.Comments;
import com.example.demo.entities.Items;
import com.example.demo.entities.Roles;
import com.example.demo.entities.Users;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.management.relation.Role;
import java.util.List;

public interface UserService extends UserDetailsService {
    Users getUserByEmail(String email);
    Users addUser(Users user);
    List<Users> getAllUsers();
    Users getUser(Long id);
    void deleteUser(Users user);
    Users saveUser(Users user);


    Roles addRole(Roles role);
    List<Roles> gelAllRoles();
    Roles getRole(Long id);
    void deleteRole(Roles role);
    Roles saveRole(Roles role);
    Roles getRoleByName(String name);
}
