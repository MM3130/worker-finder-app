package com.mm.pwfind;

public class UpdateModelClass {

    public String name,skill,landmark,city,state,mobile,image;

    public UpdateModelClass() {
    }

    public UpdateModelClass(String name, String skill, String landmark, String city, String state, String mobile, String image) {
        this.name = name;
        this.skill = skill;
        this.landmark = landmark;
        this.city = city;
        this.state = state;
        this.mobile = mobile;
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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
