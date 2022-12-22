package com.novi.fassignment.models;

import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    @Id
    @NotNull
    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false)
    private boolean enabled = true;

    @Column
    private String apikey;

    @Column
    private String email;

    @CreationTimestamp
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+00:00")
    private ZonedDateTime dateTimeRegisteredGMT;
    @UpdateTimestamp
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+00:00")
    private ZonedDateTime lastUpdate;

    @OneToMany(
            targetEntity = com.novi.fassignment.models.Authority.class,
            mappedBy = "username",
            //cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER)
    private Set<com.novi.fassignment.models.Authority> authorities = new HashSet<>();

//    the @OneToMany relations of user with question and answer table induce 'violoate foreign key contraint'
    // is is not clear why so they are commented for the moment but not removed from the code

/*    @OneToMany(
            targetEntity = com.novi.fassignment.models.Question.class,
            mappedBy = "questionId",
            //cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER)
    private Set<com.novi.fassignment.models.Question> questions = new HashSet<>();

    @OneToMany(
            targetEntity = com.novi.fassignment.models.Answer.class,
            mappedBy = "answerId",
            //cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER)
    private Set<com.novi.fassignment.models.Answer> answers = new HashSet<>();*/

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getApikey() {
        return apikey;
    }

    public void setApikey(String apikey) {
        this.apikey = apikey;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ZonedDateTime getDateTimeRegisteredGMT() {
        return dateTimeRegisteredGMT;
    }

    public void setDateTimeRegisteredGMT(ZonedDateTime dateTimeRegisteredGMT) {
        this.dateTimeRegisteredGMT = dateTimeRegisteredGMT;
    }

    public ZonedDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(ZonedDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Set<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }

    public void addAuthority(Authority authority) {
        this.authorities.add(authority);
    }
    public void removeAuthority(Authority authority) {
        this.authorities.remove(authority);
    }
}