package com.sap.ui5.selenium.sap.fpa.ui.control.analyticgrid;

import org.junit.Assert;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.sap.ui5.selenium.core.Frame;
import com.sap.ui5.selenium.sap.fpa.ui.common.CompoundControl;
import com.sap.ui5.selenium.utils.Browser;

public class DialogInput extends CompoundControl {
	

	protected DialogInput(String id, Frame frame) {
		super(id, frame);
	}

	protected WebElement getInputElement() {
		return this.findRootWebElement();
	}

	
	public void setValue(String text,boolean...triggerChange) {

		final WebElement input = getInputElement();
		final String newValue = text;
		new WebDriverWait(driver, 30).until(new ExpectedCondition<Boolean>(){
			public Boolean apply(WebDriver webdriver) {
				clearValue(false); // clear the input without triggering the change event
				input.sendKeys(newValue); // set the value
				String realValue = input.getAttribute("value");
				return realValue.equals(newValue);
			}
		});
		boolean trigger = triggerChange!=null && triggerChange.length==1?triggerChange[0]:false;
		if (trigger) {
			input.sendKeys(Keys.RETURN);
		}
	}
	
	
	public void clearValue(boolean triggerChange) {

		WebElement input = getInputElement();
		if (browser.is(Browser.INTERNETEXPLORER)) {
			input.sendKeys("");
		}
		input.clear();
		input.sendKeys(Keys.BACK_SPACE);
		if (triggerChange) {
			input.sendKeys(Keys.RETURN);
		}
	}
	
	public boolean checkValue(String expected) {
		WebElement input = getInputElement();
		String actual = input.getAttribute("value");
		if(actual == null) {
			return expected == null;
		}
		return expected.equals(actual);	
	}
	
	public void assertValue(String expected) {
		WebElement input = getInputElement();
		String actual = input.getAttribute("value");
		Assert.assertEquals("assert value of dialogInput ", expected, actual);
	}
}
