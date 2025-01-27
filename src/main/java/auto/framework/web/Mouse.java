package auto.framework.web;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.MoveTargetOutOfBoundsException;

import auto.framework.WebManager;

public class Mouse {

	protected Element element;
	
	public Mouse(Element element) {
		this.element = element;
	}
	
	protected Actions getActions(){
		return new Actions(WebManager.getDriver());
	}
	
	public void click(){
		element.click();
	}
	
	public void doubleClick(){
		WebElement target = element.toWebElement();
		getActions().moveToElement(target).doubleClick(target).perform();
	}
	
	public void hover(){
		WebElement target = element.toWebElement();
		element.waitForDisplay(true, 2);
		try{
			getActions().moveToElement(target).perform();
		} catch(MoveTargetOutOfBoundsException e){
//			System.err.println(e);
//			System.err.println("Debug: Scroll");
			element.scrollToElement();
			getActions().moveToElement(target).perform();
		} catch(WebDriverException e){
			try {
				fireMouseEvent("mouseover");
			} catch (Exception e2){}
		}
		try {
			//element.fireIEEvent("focus");
			element.fireEvent("focus");
		} catch (Exception e){}
		//element.fireIEMouseEvent("mouseover");
	}
	
	protected void fireMouseEvent(String event){
		JavascriptExecutor js = (JavascriptExecutor) WebManager.getDriver();
		String script = Element.getMouseEventScript(event);
		js.executeScript(script, element.toWebElement());
	}

}
