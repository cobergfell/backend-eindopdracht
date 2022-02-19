package com.novi.fassignment.repositories;


import com.novi.fassignment.models.Authority;
import com.novi.fassignment.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, String> {
    boolean existsById(String username);
    //boolean existsByUsername(String username);// Id and username are here the same

}