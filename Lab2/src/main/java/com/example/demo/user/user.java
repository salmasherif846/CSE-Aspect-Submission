package com.example.demo.user;

import jakarta.persistence.*;

@Entity
@Table(name="users")
public class user {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String email;
    private String username;
    private String password;
    private String phoneNumber;

    public user() {}

    public user(Integer id, String email, String username, String password, String phoneNumber) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.password = password;
        this.phoneNumber = phoneNumber;
    }

    public user(String email, String username, String password, String phoneNumber) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.phoneNumber = phoneNumber;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
