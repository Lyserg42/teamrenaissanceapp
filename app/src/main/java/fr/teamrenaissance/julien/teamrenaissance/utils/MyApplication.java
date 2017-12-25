package fr.teamrenaissance.julien.teamrenaissance.utils;

import android.app.Application;

//MyApplication inherits the application, is a global variable in Android. No matter which activity
//to jump to, you can directly call the relevant information.
public class MyApplication extends Application{

    private Integer userID;
    private String name;
    private String firstname;
    private String username;
    private String email;
    private String password;
    private String address;
    private String avatar;
    private String dciNumber;
    private String phoneNumber;
    private String facebook;
    private String twitter;
    private String city;
    private String zipCode;

    public MyApplication() {
    }

    public MyApplication(Integer userID, String name, String firstname, String username, String email, String password, String address, String avatar, String dciNumber, String phoneNumber, String facebook, String twitter, String city, String zipCode) {
        this.userID = userID;
        this.name = name;
        this.firstname = firstname;
        this.username = username;
        this.email = email;
        this.password = password;
        this.address = address;
        this.avatar = avatar;
        this.dciNumber = dciNumber;
        this.phoneNumber = phoneNumber;
        this.facebook = facebook;
        this.twitter = twitter;
        this.city = city;
        this.zipCode = zipCode;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getDciNumber() {
        return dciNumber;
    }

    public void setDciNumber(String dciNumber) {
        this.dciNumber = dciNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
}
