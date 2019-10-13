package com.web.addressbook.models;

public enum Country {
    UNITED_STATES("United States"),
    CANADA("Canada");

    Country(String value){
        this.value = value;
    }
    String value;
}