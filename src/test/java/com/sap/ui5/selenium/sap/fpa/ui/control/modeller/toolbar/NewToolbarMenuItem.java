package com.sap.ui5.selenium.sap.fpa.ui.control.modeller.toolbar;


import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.sap.ui5.selenium.core.Frame;
import com.sap.ui5.selenium.sap.m.Button;

public class NewToolbarMenuItem extends Button{


	protected NewToolbarMenuItem(String id, Frame frame) {
		super(id, frame);
	}

	
	public boolean isEnabled(){
		WebElement item = this.findRootWebElement().findElements(By.tagName("button")).get(0);
		return item.isEnabled();
	}
	
	/**
	 * Check Item is enabled
	 * @return
	 */
	public boolean checkItemisEnabled(){
		return isEnabled();
	}
	
	/**
	 * Assert Item is enabled
	 * @param expected
	 */
	public void assertItemisEnabled(boolean expected){
		Assert.assertEquals("Assert Item:" + getText() + "is enabled", expected, isEnabled());
	}
	
	/**
	 * Getter for property <code>text</code>
	 * 
	 * Item text
	 * 
	 * @return the value of property text  
	 */
	public String getText() {
		return this.js.exec(".getText()").get(String.class);
	}
	
}