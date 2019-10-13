package com.web.addressbook.pages;

import auto.framework.web.Element;
import auto.framework.web.Page;
import org.openqa.selenium.By;

/**
 * Page-Object representation of Welcome page
 */
public class BasePage extends Page {

    public static Element welcomeText = new Element("Welcome Text", By.xpath("//h1"));
    public static Element descriptionText = new Element("Description text", By.xpath("//h4"));

    public static NavBar navbar = new NavBar("Navigation Bar", By.xpath("//div[@id='navbar']"));

    public BasePage(String name, String url) {
        super(name, url);
    }

    public static class NavBar extends Element {

        public Element homeLink;
        public Element signInLink;

        public NavBar(final String name, final By by) {
            super(name, by);
            homeLink = new Element("Home link", By.xpath("//a[@data-test='home']"), this);
            signInLink = new Element("Sign-In link", By.xpath("//a[@data-test='sign-in']"), this);
        }
    }
}
