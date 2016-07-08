package com.sap.ui5.selenium.sap.m;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.sap.ui5.selenium.core.Frame;

public class RadioButton extends com.sap.ui5.selenium.commons.RadioButton{

	/**
	 * Constructs the class <code>Button</code>
	 */
	protected RadioButton(String id, Frame frame) {
		super(id, frame);
	}

	@Override
	public void select() {

		waitUntilEnabled();
		new WebDriverWait(driver, 30).until(new ExpectedCondition<Boolean>(){
			public Boolean apply(WebDriver webdriver) {
				try{
					if(radioButton().getAttribute("class").contains("sapMRbSel")){
						return true;
					}else{
						radioButton().click();
						return false;
					}
				}catch(Exception e){
					return false;
				}
			}
		});
	}
	
	protected void waitUntilEnabled() {
		new WebDriverWait(driver, 20).until(new ExpectedCondition<Boolean>(){
			public Boolean apply(WebDriver webdriver) {
				try{
					return isVisible() && isEnabled();
				}catch(Exception e){
					return false;
				}
				
			}
		});
	}
	
	/**
	 * Check whether RadioButton is enabled
	 */
	public boolean checkRadioButtonEnabled(){
		return isEnabled();
	}
	
	/**
	 * Assert whether RadioButton is enabled
	 */
	public void assertRadioButtonEnabled(boolean expected) {
		Assert.assertEquals("assert the status of button:"+ getId() +" enabled ", expected, isEnabled());
	}
	
}