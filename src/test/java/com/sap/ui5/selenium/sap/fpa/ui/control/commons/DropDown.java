package com.sap.ui5.selenium.sap.fpa.ui.control.commons;

import java.util.List;

import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.sap.ui5.selenium.core.Frame;
import com.sap.ui5.selenium.core.ListItem;
import com.sap.ui5.selenium.core.exception.ControlInitializationException;


public class DropDown extends  com.sap.ui5.selenium.commons.DropdownBox{


	protected DropDown(String id, Frame frame) {
		super(id, frame);
	}

	
	public void selectItemByKey(String key) {
		waitUntilEnabled();
		// Get list of all element on list
		List<ListItem> items = this.getItems();

		// Check if list is not empty
		if (items.isEmpty()) {
			throw new ControlInitializationException("Empty list");
		}
		innerSelect(key);
		this.frame.sync();
	}
	
	/**
	 * Checks the control properties to identify if an action can be performed
	 */
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

	/*public java.util.List<com.sap.ui5.selenium.core.Item> getItems() {
		return this.js.exec(".getItems()").getList(com.sap.ui5.selenium.core.Item.class);
	}*/
	
	private void innerSelect(String key) {
		this.js.exec(".open()");
		this.js.exec(".synchronizeSelection()");
		com.sap.ui5.selenium.sap.m.List list = (this.js.exec(".getList()").get(com.sap.ui5.selenium.sap.m.List.class));
		list.selectItem(key);
		this.frame.sync();
	}
	
	/**
	 * Check whether DropDown is enabled 
	 */
	public boolean checkDropDownEnabled(String key){
		return isVisible() && isEnabled();
	}
	
	/**
	 * Assert whether DropDown is enabled 
	 */
	public void assertDropDownEnabled(String key, Boolean expected){
		Assert.assertEquals("Assert whether DropDown: "+ key +" enabled", expected, isVisible() && isEnabled());
	}
	
	/**
	 * Check whether Item exists in DropDown 
	 * @param key
	 */
	public boolean checkItemExist(String key){
		waitUntilEnabled();
		List<ListItem> items = this.getItems();
		for (ListItem it:items){
			if(it.getText().trim().equalsIgnoreCase(key.trim())){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Assert whether Item exists in DropDown
	 * @param expected
	 * @param key
	 */
	public void assertItemExist(Boolean expected, String key){
		boolean assertItemExist = false;
		List<ListItem> items = this.getItems();
		for (ListItem it:items){
			if(it.getText().trim().equalsIgnoreCase(key.trim())){
				assertItemExist = true;
			}
		}
		Assert.assertEquals("Assert whether item: "+ key +" exist", expected, assertItemExist);
	}
	
}
