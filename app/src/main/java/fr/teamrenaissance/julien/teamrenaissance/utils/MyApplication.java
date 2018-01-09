package fr.teamrenaissance.julien.teamrenaissance.utils;

import android.app.Application;

//MyApplication inherits the application, is a global variable in Android. No matter which activity
//to jump to, you can directly call the relevant information.
public class MyApplication extends Application{

    private Integer uID;
    private String uName;
    private String lastName;
    private String firstName;
    private String phone;
    private String DCI;
    private String address;
    private String zipCode;
    private String city;
    private String facebook;
    private String twitter;
    private String email;
    private String avatar;

    public MyApplication() {
    }

    public MyApplication(Integer uID, String uName, String lastName, String firstName, String phone, String DCI, String address, String zipCode, String city, String facebook, String twitter, String email, String avatar) {

        this.uID = uID;
        this.uName = uName;
        this.lastName = lastName;
        this.firstName = firstName;
        this.phone = phone;
        this.DCI = DCI;
        this.address = address;
        this.zipCode = zipCode;
        this.city = city;
        this.facebook = facebook;
        this.twitter = twitter;
        this.email = email;
        this.avatar = avatar;
    }


    public Integer getuID() {
        return uID;
    }

    public void setuID(Integer uID) {
        this.uID = uID;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDCI() {
        return DCI;
    }

    public void setDCI(String DCI) {
        this.DCI = DCI;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }


}
