package com.mm.pwfind;

public class UserModel {
    private String name,mobileno,emailid,password;

    public UserModel() {
    }

    public UserModel(String name, String mobileno, String emailid, String password) {
        this.name = name;
        this.mobileno = mobileno;
        this.emailid = emailid;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobileno() {
        return mobileno;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }

    public String getEmailid() {
        return emailid;
    }

    public void setEmailid(String emailid) {
        this.emailid = emailid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
