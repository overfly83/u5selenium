package com.sap.ui5.selenium.sap.fpa.ui.dashboard;

import java.util.List;
import java.util.concurrent.TimeUnit;



import org.junit.Assert;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.sap.ui5.selenium.core.Frame;
import com.sap.ui5.selenium.core.UI5By;

public class DashboardPublishMenu extends com.sap.ui5.selenium.unified.Menu {

	protected DashboardPublishMenu(String id, Frame frame) {
		super(id, frame);
	}


	
	private void waitToInitialized(){
		Wait<WebDriver> wait = new WebDriverWait(driver, 30).pollingEvery(100, TimeUnit.MILLISECONDS).ignoring(StaleElementReferenceException.class);
		wait.until(new ExpectedCondition<Boolean>(){
			public Boolean apply(WebDriver webdriver) {
				try{
					boolean loaded = false;
					java.util.List<com.sap.ui5.selenium.unified.MenuItem> items = getMenuItems();
					for(com.sap.ui5.selenium.unified.MenuItem it:items){
						if(it.getText().equalsIgnoreCase("")){
							loaded = false;
						}else{
							loaded = true;
						}
					}
					return loaded;
				}catch(Exception e){	
					return false;
				}	
			}
		});
	}
	
	private List<com.sap.ui5.selenium.unified.MenuItem> getMenuItems(){
		return this.js.exec(".getItems()").getList(com.sap.ui5.selenium.unified.MenuItem.class);
	}
	
	public void selectItem(String item){
		waitToInitialized();
		List<com.sap.ui5.selenium.unified.MenuItem> items = getMenuItems();
		for(final com.sap.ui5.selenium.unified.MenuItem it:items){
			if(it.getText().trim().equals(item.trim())){
				this.findWebElement(UI5By.id(it.getId())).click();
				break;
			}
		}
	}
	
	/**
	 * Check whether Item exists
	 * @param item
	 * @return
	 */
	public boolean checkItemExist(String item){
		waitToInitialized();
		List<com.sap.ui5.selenium.unified.MenuItem> items = getMenuItems();
		for(final com.sap.ui5.selenium.unified.MenuItem it:items){
			if(it.getText().trim().equals(item.trim())){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Assert whether Item exists
	 * @param item
	 * @param expected
	 */
	public void assertItemExist(String item, boolean expected){
		boolean actual = checkItemExist(item);
		Assert.assertEquals("Assert whether Item:" + item + " exsits", expected, actual);
	}
}
