package auto.framework.web;

import org.openqa.selenium.By;

import auto.framework.assertion.Conditions;

public class MsgBox extends Element {

	public MsgBox(By by) {
		super(by);
	}
	
	public MsgBox(String name, By by) {
		super(name, by);
	}
	
	public MsgBox(String name, By by, Element parent) {
		super(name, by, parent);
	}
	
	public MsgBox(By by, Element parent) {
		super(by, parent);
	}
	
	@Deprecated public Boolean contains(String substring){
		return substring!=null && getText().contains(substring);
	}

	public void verifyContains(String substring,Boolean expected){
		verifyProperty(Element.TEXT,substring,expected?Conditions.contains:Conditions.notContains);
	}
	
	public void assertContains(String substring,Boolean expected){
		assertProperty(Element.TEXT,substring,expected?Conditions.contains:Conditions.notContains);
	}
	
	public void verifyContains(String substring){
		verifyProperty(Element.TEXT,substring,Conditions.contains);
	}
	
	public void assertContains(String substring){
		assertProperty(Element.TEXT,substring,Conditions.contains);
	}
	
}
