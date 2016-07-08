package com.sap.ui5.selenium.sap.m;

import org.junit.Assert;

import org.openqa.selenium.By;

import com.sap.ui5.selenium.core.Frame;
import com.sap.ui5.selenium.sap.fpa.ui.common.CompoundControl;

public class StandardListItem extends CompoundControl {

	protected StandardListItem(String id, Frame frame) {
		super(id, frame);
	}

	public String getText() {
		return driver.findElement(By.id(this.getId())).getText();
	}
	
	public void click() {
		driver.findElement(By.id(this.getId())).click();
	}
	
	public boolean checkEnabledStatus() {
		return this.js.exec(".getEnabled()").get(Boolean.class);
	}
	
	public boolean checkText(String expected) {
		String actual = this.getText();
		if(actual == null) {
			return expected == null;
		}	
		return actual.equals(expected);
	}
	
	public void assertEnabledStatus(boolean expectStatus) {
		Assert.assertEquals("assert enabled status of StandardListItem '"+ this.getId() +"' to be ", expectStatus, checkEnabledStatus());
	}
	
	public void assertItemText(String expected) {
		Assert.assertTrue("assert text of listItem '"+ this.getId() +"' to be " + expected, checkText(expected));
	}
	
}
