package com.web.addressbook.pages;

import auto.framework.web.*;
import org.openqa.selenium.By;

/**
 * Page-Object representation of Welcome page
 */
public class AddAddressPage extends BasePage {

    public static Element header = new Element("Header", By.xpath("//h2"));

    public static TextBox firstName = new TextBox("First Name", By.xpath("//input[@id='address_first_name']"));
    public static TextBox lastName = new TextBox("Last Name", By.xpath("//input[@id='address_last_name']"));
    public static TextBox address1 = new TextBox("Address1", By.xpath("//input[@id='address_street_address']"));
    public static TextBox address2 = new TextBox("Address2", By.xpath("//input[@id='address_secondary_address']"));
    public static TextBox city = new TextBox("City", By.xpath("//input[@id='address_city']"));
    public static ListBox state = new ListBox("State", By.xpath("//select[@id='address_state']"));
    public static TextBox zipCode = new TextBox("Zip Code", By.xpath("//input[@id='address_zip_code']"));
    public static RadioBtn countryUs = new RadioBtn("Country-US", By.xpath("//input[@id='address_country_us']"));
    public static RadioBtn countryCanada = new RadioBtn("Country-Canada", By.xpath("//input[@id='address_country_canada']"));

    public static Element createAddress = new Element("CreateAddress", By.xpath("//input[@name='commit']"));


    public AddAddressPage(String name, String url) {
        super(name, url);
    }

}
