package com.web.addressbook.pages;

import auto.framework.web.Element;
import auto.framework.web.Page;
import auto.framework.web.TextBox;
import org.openqa.selenium.By;

/**
 * Page-Object representation of Welcome page
 */
public class SignInPage extends BasePage {

    public static SignInSection signInSection = new SignInSection("SignIn Section", By.xpath("//div[@class='sign-in']"));

    public SignInPage(String name, String url) {
        super(name, url);
    }

    public static class SignInSection extends Element {

        public TextBox emailTextBox;
        public TextBox passwordtextBox;
        public Element signInButton;
        public Element signUpButton;

        public SignInSection(final String name, final By by) {
            super(name, by);
            emailTextBox = new TextBox("emailTextBox", By.xpath(".//input[@id='session_email']"), this);
            passwordtextBox = new TextBox("passwordtextBox", By.xpath(".//input[@id='session_password']"), this);
            signInButton = new Element("SignIn Button", By.xpath(".//input[@name='commit']"), this);
            signUpButton = new Element("SignUp Button", By.xpath(".//input[@data-test='sign-up']"), this);
        }
    }
}
