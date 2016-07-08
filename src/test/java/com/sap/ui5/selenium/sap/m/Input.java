package com.sap.ui5.selenium.sap.m;


import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.sap.ui5.selenium.core.Frame;
import com.sap.ui5.selenium.core.UI5By;



public class Input extends com.sap.ui5.selenium.commons.TextField {
	/**
	 * TextField element
	 */
	protected WebElement getTextFieldElement() {
		return this.findRootWebElement();
	}

	/**
	 * Constructs the class <code>Button</code>
	 */
	protected Input(String id, Frame frame) {
		super(id, frame);
	}


	protected WebElement getInputElement() {
		return this.findWebElement(UI5By.idSuffix("-inner"));
	}
	
	@Override
	public void setValue(String text) {
		final WebElement input = getInputElement();
		final String newValue = text;
		new WebDriverWait(driver, 30).until(new ExpectedCondition<Boolean>(){
			public Boolean apply(WebDriver webdriver) {
				try{
					clearValue(false); // clear the input without triggering the change event
					input.sendKeys(newValue); // set the value
					return input.getAttribute("value").equals(newValue);
				}catch(Exception e){
					return false;
				}
			}
		});

	}
	
	@Override
	public void clearValue(){
		final WebElement input = getInputElement();
		new WebDriverWait(driver, 30).until(new ExpectedCondition<Boolean>(){
			public Boolean apply(WebDriver webdriver) {
				try{
					input.clear();
					return input.getAttribute("value").length() == 0;
				}catch(Exception e){
					return false;
				}
				
			}
		});
	}
	
	/** 
	 * check if input value is as expected
	 * @param expected input value
	 */
	public boolean checkText(String expected) {
		String actual = this.getValue();
		if(actual == null) {
			return expected == null;
		}	
		
		return actual.equals(expected);
	}
	
	/** 
	 * assert if input value is as expected
	 * @param expected input value
	 */
	public void assertText(String expected) {
		Assert.assertEquals("assert text value of input '"+ this.getId() +"', ", expected, this.getValue());
	}
	
	/** 
	 * check if input enabled status is as expected
	 * @param expected input status, true indicates enabled, false indicates disabled
	 */
	public boolean checkEnable(boolean expected) {
		return this.isEnabled() == expected;
	}
	
	/** 
	 * assert if button enabled status is as expected
	 * @param expected button status, true indicates enabled, false indicates disabled
	 */
	public void assertEnable(boolean expected) {
		Assert.assertEquals("assert enable status of button '"+ this.getId() +"', ", expected, this.isEnabled());
	}
}
