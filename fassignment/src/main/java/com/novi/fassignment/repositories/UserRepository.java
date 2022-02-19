package com.novi.fassignment.repositories;

import com.novi.fassignment.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByEmail(String email);
    //boolean existsByUsername(String username);// Id and username are here the same

}

