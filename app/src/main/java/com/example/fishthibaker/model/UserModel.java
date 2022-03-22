package com.example.fishthibaker.model;

import java.io.Serializable;

public class UserModel implements Serializable {
    String firstName;
    String lastName;
    String id;
    String email;
    String password;
    String address;

    public UserModel() {

    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    String phone;

    public UserModel(String fName, String lName, String id, String email, String password, String address, String phone, String profileImage, boolean isVerified) {
        this.firstName = fName;
        this.lastName = lName;
        this.id = id;
        this.email = email;
        this.password = password;
        this.address = address;
        this.phone = phone;
        this.profileImage = profileImage;
        this.isVerified = isVerified;
    }

    String profileImage;
    boolean isVerified;
}
