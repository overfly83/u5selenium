package com.sap.ui5.selenium.sap.fpa.ui.control.commons;

import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.sap.ui5.selenium.core.Frame;
import com.sap.ui5.selenium.core.UI5By;


public class Button extends com.sap.ui5.selenium.commons.Button{

	

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

	/**
	 * Button element
	 */
	protected WebElement getButtonElement() {
		return this.findWebElement(UI5By.id(this.getId()));
	}
	
	/**
	 * Check whether Button is enabled
	 */
	public boolean checkButtonEnabled(){
		return isEnabled();
	}
	
	/**
	 * Assert whether Button is enabled 
	 */
	public void assertButtonEnabledStatus(boolean expected) {
		Assert.assertEquals("Assert whether button:"+ getText() +" is enabled", expected, isEnabled());
	}
	
}