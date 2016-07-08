package com.sap.ui5.selenium.sap.fpa.ui.control.menu;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.sap.ui5.selenium.core.Control;
import com.sap.ui5.selenium.core.Frame;

public class Item extends Control{


	protected Item(String id, Frame frame) {
		super(id, frame);
	}

	private Item getItem(){
		return this;
	}
	public boolean isEnabled(){
		
		return this.findRootWebElement().isEnabled();
	}
	
	public void click(){
		new WebDriverWait(driver,3).until(new ExpectedCondition<Boolean>(){
			public Boolean apply(WebDriver webdriver) {
				if(isEnabled()){
					webdriver.findElement(By.id(getItem().getId())).click();
					return true;
				}
				return false;
			}
			
		});
		
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