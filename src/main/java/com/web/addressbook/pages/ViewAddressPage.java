package com.web.addressbook.pages;

import auto.framework.web.*;
import org.openqa.selenium.By;

/**
 * Page-Object representation of Welcome page
 */
public class ViewAddressPage extends BasePage {

    public static Element firstName = new Element("First Name", By.xpath("//span[@data-test='first_name']"));
    public static Element lastName = new Element("Last Name", By.xpath("//span[@data-test='last_name']"));
    public static Element address1 = new Element("Address1", By.xpath("//span[@data-test='street_address']"));
    public static Element address2 = new Element("Address2", By.xpath("//span[@data-test='secondary_address']"));
    public static Element city = new Element("City", By.xpath("//span[@data-test='city']"));
    public static Element state = new Element("State", By.xpath("//span[@data-test='state']"));
    public static Element zipCode = new Element("Zip Code", By.xpath("//span[@data-test='zip_code']"));
    public static Element country = new Element("Country", By.xpath("//span[@data-test='country']"));

    public static Link edit = new Link("Edit", By.xpath("//a[@data-test='edit']"));
    public static Link list = new Link("List", By.xpath("//a[@data-test='list']"));

    public ViewAddressPage(String name, String url) {
        super(name, url);
    }

}
