package com.sap.ui5.selenium.sap.m;



import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.sap.ui5.selenium.core.Frame;
import com.sap.ui5.selenium.core.UI5By;
import com.sap.ui5.selenium.sap.fpa.ui.common.CompoundControl;

public class MultiInput extends CompoundControl{

	protected WebElement getMultiInput() {
		return this.findRootWebElement();
	}
	
	protected MultiInput(String id, Frame frame) {
		super(id, frame);
	}
	
	public void clearValue() {
		while(!this.isEmpty()) {
			try {
				this.getMultiInput().findElements(By.className("sapMTokenIcon")).get(0).click();	
			}catch(Exception e) {
			}
		}
	}
	
	private boolean isEmpty() {
		try {
			driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
			List<WebElement> users = this.findWebElement(UI5By.id(this.getId())).findElements(By.className("sapMToken"));
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			if(users.size() == 0) {
				return true;
			}else {
				return false;
			}
		}catch(Exception e) {
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			return true;
		}
	}
	
	/** 
	 * check if expected item exists in multiInput
	 * @param expected item value
	 */
	public boolean checkItemExists(String expected) {
		try {
			By byItem = By.xpath(".//span[@class='sapMTokenText' and text()='"+expected+"']");
			driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
			List<WebElement> items = this.findRootWebElement().findElements(byItem);
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			if(items.size() == 0) {
				return false;
			}
			else {
				return true;
			}
		} catch (Exception e) {
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			return false;
		}
	}
	
	/** 
	 * assert expected item exists in multiInput
	 * @param expected item value
	 */
	public void assertItemExists(String expected) {
		Assert.assertEquals("assert  multiInput '"+ this.getId() +"' contains item " +expected, 
				true, this.checkItemExists(expected));
	}
}
