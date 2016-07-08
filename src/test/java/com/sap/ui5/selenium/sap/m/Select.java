package com.sap.ui5.selenium.sap.m;

import org.junit.Assert;

import com.sap.ui5.selenium.core.Frame;
import com.sap.ui5.selenium.core.exception.ControlInitializationException;
import com.sap.ui5.selenium.core.exception.ControlNotEnabledException;
import com.sap.ui5.selenium.core.exception.ControlNotVisibleException;


public class Select extends Selector_Base {


	protected Select(String id, Frame frame) {
		super(id, frame);
	}

	
	public void selectItemByKey(String key) {

		// Check key value
		if (key.length() == 0) {
			throw new IllegalArgumentException("Empty Key value");
		}

		checkProperties();

		// Get list of all element on list
		java.util.List<com.sap.ui5.selenium.core.Item> items = this.getItems();

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
	protected void checkProperties() {

		if (!isVisible()) {
			throw new ControlNotVisibleException();
		}

		if (!isEnabled()) {
			throw new ControlNotEnabledException();
		}
	}
	
	public java.util.List<com.sap.ui5.selenium.core.Item> getItems() {
		return this.js.exec(".getItems()").getList(com.sap.ui5.selenium.core.Item.class);
	}
	
	private void innerSelect(String key) {
		this.js.exec(".open()");
		this.js.exec(".synchronizeSelection()");
		com.sap.ui5.selenium.sap.m.List list = (this.js.exec(".getList()").get(com.sap.ui5.selenium.sap.m.List.class));
		list.selectItem(key);
		this.frame.sync();
	}
	
	public boolean checkVisibleStatus() {
		return this.isVisible();
	}
	
	public boolean checkEnabledStatus() {
		return this.isEnabled();
	}
	
	public void assertVisibleStatus(boolean expectStatus) {
		Assert.assertEquals("assert visible status of selector '"+ this.getId() +"' to be ", expectStatus, checkVisibleStatus());
	}
	
	public void assertEnabledStatus(boolean expectStatus) {
		Assert.assertEquals("assert enabled status of selector '"+ this.getId() +"' to be ", expectStatus, checkEnabledStatus());
	}
}
