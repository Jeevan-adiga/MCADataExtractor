package com.web.addressbook.models;

import com.github.javafaker.Faker;
import org.apache.commons.lang3.RandomUtils;

/**
 * this class represent Address
 */
public class AddressModel {

    Faker faker;

    public AddressModel() {
        faker = new Faker();

        firstName = faker.name().firstName();
        lastName = faker.name().lastName();

        address1 = faker.address().streetAddress();
        address2 = faker.address().secondaryAddress();
        city = faker.address().cityName();
        state = State.ALASKA; //faker.address().stateAbbr();
        zipCode = faker.address().zipCode();
        country = Country.UNITED_STATES;
    }

    String firstName;
    String lastName;

    String address1;
    String address2;
    String city;
    State state; //faker.address().stateAbbr();
    String zipCode;
    Country country;

    String birthDay;

    String color;
    String age;
    String website;
    String picture;
    String phone;
    String commonIntrests;
    String note;

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

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCommonIntrests() {
        return commonIntrests;
    }

    public void setCommonIntrests(String commonIntrests) {
        this.commonIntrests = commonIntrests;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
