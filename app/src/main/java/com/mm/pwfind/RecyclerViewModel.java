package com.mm.pwfind;

public class RecyclerViewModel {

    private String name,skill,landmark,city,state,verified,gender,mobile,dateofbirth,image;

    public RecyclerViewModel() {
    }

    public RecyclerViewModel(String name, String skill, String landmark, String city, String state, String verified, String gender, String mobile, String dateofbirth, String image) {
        this.name = name;
        this.skill = skill;
        this.landmark = landmark;
        this.city = city;
        this.state = state;
        this.verified = verified;
        this.gender = gender;
        this.mobile = mobile;
        this.dateofbirth = dateofbirth;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getVerified() {
        return verified;
    }

    public void setVerified(String verified) {
        this.verified = verified;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getDateofbirth() {
        return dateofbirth;
    }

    public void setDateofbirth(String dateofbirth) {
        this.dateofbirth = dateofbirth;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
