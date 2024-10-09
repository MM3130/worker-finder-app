package com.mm.pwfind;

public class WorkerModel {
    public String name,skill,landmark,city,state,verified,gender,mobile,dateofbirth,email,password,image;

    public WorkerModel() {
    }

    public WorkerModel(String name, String skill, String landmark, String city, String state, String verified, String gender, String mobile, String dateofbirth, String email, String password, String image) {
        this.name = name;
        this.skill = skill;
        this.landmark = landmark;
        this.city = city;
        this.state = state;
        this.verified = verified;
        this.gender = gender;
        this.mobile = mobile;
        this.dateofbirth = dateofbirth;
        this.email = email;
        this.password = password;
        this.image = image;
    }

}
