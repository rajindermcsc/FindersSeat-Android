package com.websoftquality.findersseat.datamodels.main;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MatchProfileModel {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("online")
    @Expose
    private Integer online;
    @SerializedName("lastseen")
    @Expose
    private Integer lastseen;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("avater")
    @Expose
    private String avater;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("birthday")
    @Expose
    private String birthday;
    @SerializedName("language")
    @Expose
    private String language;
    @SerializedName("relationship")
    @Expose
    private Integer relationship;
    @SerializedName("height")
    @Expose
    private String height;
    @SerializedName("body")
    @Expose
    private Integer body;
    @SerializedName("smoke")
    @Expose
    private Integer smoke;
    @SerializedName("ethnicity")
    @Expose
    private Integer ethnicity;
    @SerializedName("pets")
    @Expose
    private Integer pets;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("country_text")
    @Expose
    private String countryText;
    @SerializedName("relationship_text")
    @Expose
    private Object relationshipText;
    @SerializedName("body_text")
    @Expose
    private Object bodyText;
    @SerializedName("smoke_text")
    @Expose
    private Object smokeText;
    @SerializedName("ethnicity_text")
    @Expose
    private Object ethnicityText;
    @SerializedName("pets_text")
    @Expose
    private Object petsText;
    @SerializedName("gender_text")
    @Expose
    private String genderText;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOnline() {
        return online;
    }

    public void setOnline(Integer online) {
        this.online = online;
    }

    public Integer getLastseen() {
        return lastseen;
    }

    public void setLastseen(Integer lastseen) {
        this.lastseen = lastseen;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvater() {
        return avater;
    }

    public void setAvater(String avater) {
        this.avater = avater;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Integer getRelationship() {
        return relationship;
    }

    public void setRelationship(Integer relationship) {
        this.relationship = relationship;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public Integer getBody() {
        return body;
    }

    public void setBody(Integer body) {
        this.body = body;
    }

    public Integer getSmoke() {
        return smoke;
    }

    public void setSmoke(Integer smoke) {
        this.smoke = smoke;
    }

    public Integer getEthnicity() {
        return ethnicity;
    }

    public void setEthnicity(Integer ethnicity) {
        this.ethnicity = ethnicity;
    }

    public Integer getPets() {
        return pets;
    }

    public void setPets(Integer pets) {
        this.pets = pets;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCountryText() {
        return countryText;
    }

    public void setCountryText(String countryText) {
        this.countryText = countryText;
    }

    public Object getRelationshipText() {
        return relationshipText;
    }

    public void setRelationshipText(Object relationshipText) {
        this.relationshipText = relationshipText;
    }

    public Object getBodyText() {
        return bodyText;
    }

    public void setBodyText(Object bodyText) {
        this.bodyText = bodyText;
    }

    public Object getSmokeText() {
        return smokeText;
    }

    public void setSmokeText(Object smokeText) {
        this.smokeText = smokeText;
    }

    public Object getEthnicityText() {
        return ethnicityText;
    }

    public void setEthnicityText(Object ethnicityText) {
        this.ethnicityText = ethnicityText;
    }

    public Object getPetsText() {
        return petsText;
    }

    public void setPetsText(Object petsText) {
        this.petsText = petsText;
    }

    public String getGenderText() {
        return genderText;
    }

    public void setGenderText(String genderText) {
        this.genderText = genderText;
    }


}
