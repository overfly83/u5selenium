package com.sap.ui5.selenium.sap.m;

import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.sap.ui5.selenium.core.Frame;


public class Button extends com.sap.ui5.selenium.commons.Button{

	/**
	 * Button element
	 */
	protected WebElement getButtonElement() {
		return this.findRootWebElement();
	}

	/**
	 * Constructs the class <code>Button</code>
	 */
	protected Button(String id, Frame frame) {
		super(id, frame);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void click() {
		Wait<WebDriver> wait = new WebDriverWait(driver, 5);
		wait.until(ExpectedConditions.elementToBeClickable(getButtonElement()));
		getButtonElement().click();
	}

	public void clickUntilTextChange(String text) {
		final String expectedText = text;
		driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
		new WebDriverWait(driver, 20).until(new ExpectedCondition<Boolean>(){
			public Boolean apply(WebDriver webdriver) {
				try{
					if(getButtonElement().isDisplayed() && !getButtonElement().getText().equalsIgnoreCase(expectedText)) {
						getButtonElement().click();
						return false;
					}else{
						return true;
					}
				}catch(Exception e){
					return false;
				}
			}
		});
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}
	
	/** 
	 * check if button enabled status is as expected
	 * @param expected button status, true indicates enabled, false indicates disabled
	 */
	public boolean checkEnableStatus(boolean expected) {
		return this.isEnabled() == expected;
	}
	
	/** 
	 * assert if button enabled status is as expected
	 * @param expected button status, true indicates enabled, false indicates disabled
	 */
	public void assertEnableStatus(boolean expected) {
		Assert.assertEquals("assert enable status of button '"+ this.getId() +"', ", expected, this.isEnabled());
	}
	
	/** 
	 * check if button text is as expected
	 * @param expected button text
	 */
	public boolean checkText(String expected) {
		String actual = this.getText();
		if(actual == null) {
			return expected == null;
		}	
		
		return actual.equals(expected);
	}
	
	/** 
	 * assert if button text is as expected
	 * @param expected button text
	 */
	public void assertText(String expected) {
		Assert.assertEquals("assert text value of button '"+ this.getId() +"', ", expected, this.getText());
	}
}