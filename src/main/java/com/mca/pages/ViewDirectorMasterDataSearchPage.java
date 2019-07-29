package com.mca.pages;

import org.openqa.selenium.By;

import auto.framework.web.Element;

/**
 * Page represent Search page
 */
public class ViewDirectorMasterDataSearchPage {

	public static Element dinInput = new  Element("DIN Input", By.name("din"));
	public static Element submitButton = new  Element("Submit button", By.xpath("//input[@value='Submit']"));	
	
	public static Element errorMsg = new Element("ErrorMsg", By.xpath("//div[@id='msg_overlay']//li"));
	public static Element errorCloseBtn = new Element("errorCloseBtn", By.id("msgboxclose"));
 
}
