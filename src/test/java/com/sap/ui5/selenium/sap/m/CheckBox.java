package com.sap.ui5.selenium.sap.m;



import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.sap.ui5.selenium.core.Frame;
import com.sap.ui5.selenium.core.UI5By;

public class CheckBox extends com.sap.ui5.selenium.commons.CheckBox {

	/**
	 * CheckBox element
	 */
	protected WebElement getCheckBoxElement() {
		return this.findRootWebElement();
	}

	/**
	 * Constructs the class <code>CheckBox</code>
	 */
	protected CheckBox(String id, Frame frame) {
		super(id, frame);
	}

	@Override
	public boolean isChecked(){
		try{
			String checked = driver.findElement(By.id(this.getId())).findElement(By.tagName("input")).getAttribute("checked");
			if(checked.trim().length()>0){
				return true;
			}
		
		}catch(Exception e){
			return false;
		}
		return false;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void check() {
		Wait<WebDriver> wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.elementToBeClickable(getCheckBoxElement()));
		if (this.findWebElement(UI5By.idSuffix("-CB")).isSelected()) {
			//throw new InvalidControlStateException("CheckBox is already checked.");
		} else {
			getCheckBoxElement().click();
		}
	}
	@Override
	public void uncheck() {
		Wait<WebDriver> wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.elementToBeClickable(getCheckBoxElement()));
		if (!this.findWebElement(UI5By.idSuffix("-CB")).isSelected()) {
			//throw new InvalidControlStateException("CheckBox is already unchecked.");
		} else {
			getCheckBoxElement().click();
		}
	}
	
	/** 
	 * check if CheckBox check status is as expected
	 * @param expected CheckBox check status, true indicates checked, false indicates unchecked
	 */
	public boolean checkStatus(boolean expected) {
		return this.isChecked() == expected;
	}
	
	/** 
	 * assert if CheckBox check status is as expected
	 * @param expected CheckBox check status, true indicates checked, false indicates unchecked
	 */
	public void assertStatus(boolean expected) {
		Assert.assertEquals("assert check status of checkbox '" +this.getId()+ "'", expected, this.isChecked());
	}
	
	/** 
	 * check if CheckBox enable status is as expected
	 * @param expected CheckBox enable status, true indicates enabled, false indicates disabled
	 */
	public boolean checkEnable(boolean expected) {
		return this.isEnabled() == expected;
	}
	
	/** 
	 * assert if CheckBox enable status is as expected
	 * @param expected CheckBox enable status, true indicates enabled, false indicates disabled
	 */
	public void assertEnable(boolean expected) {
		Assert.assertEquals("assert enable status of checkbox '" +this.getId()+ "'", expected, this.isEnabled());
	}
}
