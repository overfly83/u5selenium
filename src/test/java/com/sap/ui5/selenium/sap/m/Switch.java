package com.sap.ui5.selenium.sap.m;

import org.junit.Assert;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.sap.ui5.selenium.core.Frame;

public class Switch extends com.sap.ui5.selenium.core.Control {

	/**
	 * CheckBox element
	 */
	protected WebElement getThisElement() {
		return driver.findElement(By.id(this.getId())).findElement(By.className("sapMSwt"));
	}

	/**
	 * Constructs the class <code>CheckBox</code>
	 */
	protected Switch(String id, Frame frame) {
		super(id, frame);
	}	
	
	public boolean checkEnabled() {
		return this.js.exec(".getEnabled()").get(Boolean.class);
	}
	
	public void assertEnabeld(boolean expected) {
		Assert.assertEquals("Assert switch enable status", expected, this.checkEnabled());
	}
	
	public void switchOn(){
		new WebDriverWait(driver, 30).until(new ExpectedCondition<Boolean>(){
			public Boolean apply(WebDriver webdriver) {
				try{
					if(getThisElement().getAttribute("class").contains("sapMSwtOn")){
						return true;
					}else{
						getThisElement().click();
						return false;
					}
				}catch(Exception e){
					return false;
				}
			}
		});
	}
	

	public void switchOff(){
		new WebDriverWait(driver, 30).until(new ExpectedCondition<Boolean>(){
			public Boolean apply(WebDriver webdriver) {
				try{
					if(getThisElement().getAttribute("class").contains("sapMSwtOff")){
						return true;
					}else{
						getThisElement().click();
						return false;
					}
				}catch(Exception e){
					return false;
				}
			}
		});
	}
	
	public boolean isSwitchOn(){
		return getThisElement().getAttribute("class").contains("sapMSwtOn");
	}
	
	
	public void assertSwithStatus(boolean switchOn){
		boolean actualstatus = getThisElement().getAttribute("class").contains("sapMSwtOn");
		Assert.assertEquals("Checking the expected status:"+switchOn+ " with actual status:"+actualstatus, switchOn, actualstatus);
	}
}
