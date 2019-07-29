package auto.framework.web;

//import java.util.List;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import auto.framework.WebManager;

public class Elements  {
	
	protected String name;
	protected List<Element> elemList;
		
	public Elements(String name, String xpath){
		this.name = name;				
		
		List<WebElement> rowElements = WebManager.getDriver().findElements(By.xpath(xpath));
		
		for(int i=0;i<rowElements.size();i++){
			elemList.add(new Element("Row "+i,By.xpath(xpath+"["+i+"]")));
		}
		
	}
	
	public List<Element> getList(){
		return elemList;
	}
	
	public int getRowCount(){
		return elemList.size();
	}
		
}
