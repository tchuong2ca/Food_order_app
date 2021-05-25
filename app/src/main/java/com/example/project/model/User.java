package com.example.project.model;

public class User {
    private String Admin,Mail, Name, Password, Phone;
    public User(){}

    public User(String Admin,String mail, String name, String password) {
        this.Admin = Admin;
        Mail = mail;
        Name = name;
        Password = password;

    }
    public String getAdmin() {
        return Admin;
    }

    public void setAdmin(String Admin) {
        this.Admin = Admin;
    }
    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getMail() {
        return Mail;
    }

    public void setMail(String mail) {
        Mail = mail;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}



















