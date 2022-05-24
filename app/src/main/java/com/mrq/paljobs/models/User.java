package com.mrq.paljobs.models;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class User {

    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phone;
    private String photo;
    private String photoCover;
    private String address;
    private String jobTitle;
    private String cv;
    private String gender;
    private String userType;
    private ArrayList<String> skills;

    public User() {
    }

    @NotNull
    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", phone='" + phone + '\'' +
                ", photo='" + photo + '\'' +
                ", photoCover='" + photoCover + '\'' +
                ", address='" + address + '\'' +
                ", jobTitle='" + jobTitle + '\'' +
                ", cv='" + cv + '\'' +
                ", gender='" + gender + '\'' +
                ", userType='" + userType + '\'' +
                ", skills=" + skills +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPhotoCover() {
        return photoCover;
    }

    public void setPhotoCover(String photoCover) {
        this.photoCover = photoCover;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getCv() {
        return cv;
    }

    public void setCv(String cv) {
        this.cv = cv;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public ArrayList<String> getSkills() {
        return skills;
    }

    public void setSkills(ArrayList<String> skills) {
        this.skills = skills;
    }
}