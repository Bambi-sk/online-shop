package com.example.demo.services.impl;

import com.example.demo.entities.Comments;
import com.example.demo.entities.Roles;
import com.example.demo.entities.Users;
import com.example.demo.repositories.CommentsRepository;
import com.example.demo.repositories.RolesRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RolesRepository rolesRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Users myUser=userRepository.findByEmail(s);
        if(myUser!=null){
            User secUser=new User(myUser.getEmail(),myUser.getPassword(),myUser.getRoles());
            return  secUser;
        }
        throw new UsernameNotFoundException("User Not Found");
    }

    @Override
    public Users getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Users addUser(Users user) {
        Users checkUser=userRepository.findByEmail(user.getEmail());
        if(checkUser==null){
            Roles role= rolesRepository.findByName("ROLE_USER");
            if(role!=null){
                ArrayList<Roles> roles=new ArrayList<>();
                roles.add(role);
                user.setRoles(roles);
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                return userRepository.save(user);
            }
        }
      return null;
    }

    @Override
    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Users getUser(Long id) {
        return userRepository.getOne(id);
    }

    @Override
    public void deleteUser(Users user) {
            userRepository.delete(user);
    }

    @Override
    public Users saveUser(Users user) {
        return userRepository.save(user);
    }

    @Override
    public Roles addRole(Roles role) {

        return rolesRepository.save(role);
    }

    @Override
    public List<Roles> gelAllRoles() {
        return rolesRepository.findAll();
    }

    @Override
    public Roles getRole(Long id) {
        return rolesRepository.getOne(id);
    }

    @Override
    public void deleteRole(Roles role) {
        rolesRepository.delete(role);
    }

    @Override
    public Roles saveRole(Roles role) {
        return rolesRepository.save(role);
    }

    @Override
    public Roles getRoleByName(String name) {
        return rolesRepository.findByName(name);
    }


}
