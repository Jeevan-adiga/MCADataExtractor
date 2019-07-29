package auto.framework.web;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import auto.framework.ReportLog;
import auto.framework.WebManager;
import auto.framework.assertion.Conditions;
import auto.framework.assertion.Conditions.Condition;
import auto.framework.assertion.WaitFor;

public abstract class AbstractWebObject {
	
	public abstract Object getProperty(String property);
	
	public abstract String getName();
	
	public <Expected> Boolean waitForProperty(final String property,final Expected expected,long timeOutInSeconds){
		return waitForProperty(property, expected, timeOutInSeconds, Conditions.equals);
	}
	
//	public <Expected> Boolean verifyProperty(Enum<?> property,Expected expected){
//		return verifyProperty(property.toString(), expected, Conditions.equals);
//	};
	
	public <Expected> Boolean verifyProperty(String property,Expected expected){
		return verifyProperty(property, expected, Conditions.equals);
	};
	
//	public <Expected> Boolean verifyProperty(Enum<?> property,Condition<Expected> validation){
//		return verifyProperty(property.toString(), validation);
//	};
//	
//	public <Expected> Boolean verifyProperty(Enum<?> property,Expected expected,Condition<Expected> validation){
//		return verifyProperty(property.toString(), expected, validation);
//	};
	
	public <Expected> Boolean verifyProperty(String property,Condition<Expected> validation){
		try { 
			return assertProperty(property, validation);
		} catch (AssertionError e) {
			return false;
		} catch (NoSuchElementException|StaleElementReferenceException error) {
			//ReportLog.failed(error.getClass().getSimpleName() + " : " + error.getMessage());
			ReportLog.logError(error);
			return false;
		}
	};
	
	public <Expected> Boolean verifyProperty(String property,Expected expected,Condition<Expected> validation){
		try { 
			return assertProperty(property, expected, validation);
		} catch (AssertionError e) {
			return false;
		} catch (NoSuchElementException|StaleElementReferenceException error) {
			//ReportLog.failed(error.getClass().getSimpleName() + " : " + error.getMessage());
			ReportLog.logError(error);
			return false;
		}
	};
	
	public <Expected> Boolean assertProperty(String property,Condition<Expected> validation, WaitFor.Timeout timeout){
		return assertProperty(property, validation);
	}
	
	public <Expected> Boolean assertProperty(String property,Condition<Expected> validation){
		if(validation==null) return assertProperty(property, Conditions.isNull);
		Object actual = getProperty(property);
		Boolean success = validation.verify(null, actual);
		String message = "[" + getName() + "] Verify that "+property+" "+validation.name();
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
	
	public <Expected> Boolean assertProperty(String property,Expected expected,Condition<Expected> validation){
		Object actual = getProperty(property);
		Boolean success = validation.verify(expected, actual);
		String message = "[" + getName() + "] Verify that "+property+" "+validation.name()+" "+expected;
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
	
	public <Expected> Boolean waitForProperty(final String property,final Expected expected,long timeOutInSeconds,final Condition<Expected> validation){
		WebDriverWait wait = new WebDriverWait(WebManager.getDriver(),timeOutInSeconds);
		Boolean success = true;
		
		//wait.ignoring(StaleElementReferenceException.class, NoSuchElementException.class);
		wait.ignoring(StaleElementReferenceException.class);
		try {
			ExpectedCondition<Boolean> expectedCondition = new ExpectedCondition<Boolean>() {
				@Override
				public Boolean apply(WebDriver driver) {
					return validation.verify(expected, getProperty(property));
				}
			};
			wait.until(expectedCondition);
		} catch(TimeoutException e){
			success = false;
		}
		return success;
	};

}
