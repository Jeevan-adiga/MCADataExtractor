package com.web.addressbook.pages;

import auto.framework.web.Element;
import auto.framework.web.Link;
import org.openqa.selenium.By;

/**
 * Page-Object representation of Welcome page
 */
public class HomePage extends BasePage {

    public static Element welcomeText = new Element("Welcome Text", By.xpath("//h1"));
    public static Element descriptionText = new Element("Description text", By.xpath("//h4"));

    public static NavBar navbar = new NavBar("Navigation Bar", By.xpath("//div[@id='navbar']"));

    public HomePage(String name, String url) {
        super(name, url);
    }

    public static class NavBar extends Element {

        public Link homeLink;
        public Link signInLink;
        public Link addressesLink;
        public Link signOutLink;

        public Element currentUser;

        public NavBar(final String name, final By by) {
            super(name, by);
            homeLink = new Link("Home link", By.xpath(".//a[@data-test='home']"), this);
            signInLink = new Link("Sign-In link", By.xpath(".//a[@data-test='sign-in']"), this);
            addressesLink = new Link("addressesLink", By.xpath(".//a[@data-test='addresses']"), this);
            signOutLink = new Link("signOutLink", By.xpath(".//a[@data-test='sign-out']"), this);

            currentUser = new Element("current-user", By.xpath(".//span[@data-test='current-user']"), this);
        }
    }
}
