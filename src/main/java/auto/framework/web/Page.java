package auto.framework.web;

import java.util.regex.PatternSyntaxException;

import org.openqa.selenium.By;
import auto.framework.WebManager;
import auto.framework.assertion.Conditions.Condition;

public class Page extends AbstractWebObject implements ISearchContext {
	
	public static final String URL = "url";
	public static final String TITLE = "title";
	private static final Condition<String> InPage = new InPage();
	private static final Condition<String> NotInPage = new NotInPage();
	
	private String url = ".*/business/index.jsp.*";
	private String name = "Page object";
	
	public Page(String name, String url){
		this.name = name;
		this.url = url;
	}
	
	public String getURL(){
		return WebManager.getDriver().getCurrentUrl();
	}
	
	public String getTitle(){
		return WebManager.getDriver().getTitle();
	}
	
	public Boolean inURL(int timeOutInSeconds){
		return waitForProperty(URL, url, timeOutInSeconds, InPage);
	}
	
	public Boolean inURL(){
		String currentURL = WebManager.getDriver().getCurrentUrl();
		try {
			return currentURL.equalsIgnoreCase(url) || currentURL.matches(url);
		} catch(PatternSyntaxException e){
			return false;
		}
	}
	
	public void verifyURL(){
		verifyURL(true, 10);
	}
	
	public void assertURL(){
		assertURL(true, 10);
	}
	
	public void verifyURL(final Boolean expected, int timeOutInSeconds){
		waitForProperty(URL, url, timeOutInSeconds, expected? InPage : NotInPage);
		verifyURL(expected);
//		WebDriverWait wait = new WebDriverWait(WebManager.getDriver(),timeOutInSeconds);
//		Boolean success = true;
//		
//		wait.ignoring(StaleElementReferenceException.class);
//		try {
//			ExpectedCondition<Boolean> expectedCondition = new ExpectedCondition<Boolean>() {
//				@Override
//				public Boolean apply(WebDriver driver) {
//					return expected.equals(inURL());
//				}
//			};
//			wait.until(expectedCondition);
//		} catch(TimeoutException e){
//			success = false;
//		}
//		
//		ReportLog.verifyTrue(success, (expected?"":"not ")+"in " + name + " page");
	}
	
	public void assertURL(final Boolean expected, int timeOutInSeconds){
		waitForProperty(URL, url, timeOutInSeconds, expected? InPage : NotInPage);
		assertURL(expected);
//		WebDriverWait wait = new WebDriverWait(WebManager.getDriver(),timeOutInSeconds);
//		Boolean success = true;
//		
//		wait.ignoring(StaleElementReferenceException.class);
//		try {
//			ExpectedCondition<Boolean> expectedCondition = new ExpectedCondition<Boolean>() {
//				@Override
//				public Boolean apply(WebDriver driver) {
//					return expected.equals(inURL());
//				}
//			};
//			wait.until(expectedCondition);
//		} catch(TimeoutException e){
//			success = false;
//		}
//		
//		ReportLog.assertTrue(success, (expected?"":"not ")+"in " + name + " page");
	}
	
	public void verifyURL(Boolean expected){
		verifyProperty(URL, url, expected? InPage : NotInPage);
		//ReportLog.verifyTrue(expected.equals(inURL()), (expected?"":"not ")+"in " + name + " page");
	}
	
	public void assertURL(Boolean expected){
		assertProperty(URL, url, expected? InPage : NotInPage);
		//ReportLog.assertTrue(expected.equals(inURL()), (expected?"":"not ")+"in " + name + " page");
	}
	
	protected static class InPage implements Condition<String> {	
		
		@Override
		public Boolean verify(String expected, Object actual) {
			try {
				return String.valueOf(actual).equalsIgnoreCase(expected) || String.valueOf(actual).matches(expected);
			} catch(PatternSyntaxException e){
				return false;
			}
		}

		@Override
		public String name() {
			return "is in";
		}
		
	};
	
	protected static class NotInPage extends InPage {	
		
		@Override
		public Boolean verify(String expected, Object actual) {
			return !super.verify(expected, actual);
		}

		@Override
		public String name() {
			return "is not in";
		}
	};
	
	/*
		public <Expected> Boolean assertProperty(String property,Condition<Expected> validation){
		if(validation==null) return assertProperty(property, Conditions.isNull);
		Object actual = getProperty(property);
		Boolean success = validation.verify(null, actual);
		String message = "[" + name + "] Verify that "+property+" "+validation.name();
		try{
			ReportLog.assertTrue(success, message);
		} catch (AssertionError e) {
			ReportLog.addInfo("Actual: "+property+"="+actual);
			throw e;
		} catch (NoSuchElementException|StaleElementReferenceException error) {
			ReportLog.failed(error.getClass().getSimpleName() + " : " + error.getMessage());
			throw error;
		}
		return success;
	};
	 */
	
	public ISearchContext findElement(String name, By by){
		return new Element(name, by);
	}
	
	public Element findElement(By by){
		return new Element(by);
	}

	@Override
	public ISearchContext getParent() {
		return null;
	}
	
	public WebControl.Window switchWithUrl(){
		return WebControl.switchWithUrl(url);
	}
	
	public WebControl.Window switchWithUrl(String url){
		return WebControl.switchWithUrl(url);
	}
	
	public String getName(){
		return name;
	}

	@Override
	public Object getProperty(String property) {
		switch(String.valueOf(property).toLowerCase()){
			case URL: return getURL();
			case TITLE: return getTitle();
		}
		return null;
	}

	
	
/*	public Boolean isTextPresent(String text){
	    WebElement bodyElement = WebManager.getDriver().findElement(By.tagName("body"));
	    return bodyElement.getText().contains(text);
	}
	
	public void verifyTextPresent(String text){
		ReportLog.verifyTrue(isTextPresent(text), "Verify that text is in " + name + " page");
	} */

	
}