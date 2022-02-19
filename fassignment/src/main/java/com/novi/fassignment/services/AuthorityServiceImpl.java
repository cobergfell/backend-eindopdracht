package com.novi.fassignment.services;

import com.novi.fassignment.exceptions.RecordNotFoundException;
import com.novi.fassignment.exceptions.UsernameNotFoundException;
import com.novi.fassignment.models.Authority;
import com.novi.fassignment.models.FileStoredInDataBase;
import com.novi.fassignment.models.Question;
import com.novi.fassignment.models.User;
import com.novi.fassignment.repositories.AuthorityRepository;
import com.novi.fassignment.repositories.UserRepository;
import com.novi.fassignment.utils.RandomStringGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class AuthorityServiceImpl implements com.novi.fassignment.services.AuthorityService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorityRepository authorityRepository;


    @Override
    public void addAuthority(String username, String authorityName) {
        if (!userRepository.existsById(username)) throw new UsernameNotFoundException(username);
        Authority authority = new Authority(username, authorityName);
        authorityRepository.save(authority);
    }


    @Override
    public Set<Authority> getAuthorities(String username) {
        if (!userRepository.existsById(username)) throw new UsernameNotFoundException(username);
        User user = userRepository.findById(username).get();
        return user.getAuthorities();
    }

/*    @Override
    public void addAuthority(String username, String authority) {
        if (!userRepository.existsById(username)) throw new UsernameNotFoundException(username);
        User user = userRepository.findById(username).get();
        user.addAuthority(new Authority(username, authority));
        userRepository.save(user);
    }*/




    @Override
    public void removeAuthority(String username, String authorityStr) {
        if (!userRepository.existsById(username)) throw new UsernameNotFoundException(username);
        User user = userRepository.findById(username).get();
        Set<Authority> authorities= user.getAuthorities();
        for(Authority authorityObject :authorities){
            String authorityStrTobeInspected=authorityObject.getAuthority();
            Boolean check=authorityStrTobeInspected.equals(authorityStr);
            if(authorityStrTobeInspected.equals(authorityStr)){
                authorityRepository.delete(authorityObject);

            }
        }

    }

    @Override
    public void removeAllAuthorities(String username) {
        if (!userRepository.existsById(username)) throw new UsernameNotFoundException(username);
        User user = userRepository.findById(username).get();
        Set<Authority> authorities= user.getAuthorities();
        for(Authority authorityObject :authorities){
            authorityRepository.delete(authorityObject);
        }

    }


}
