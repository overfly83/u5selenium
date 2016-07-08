package com.sap.ui5.selenium.sap.fpa.ui.control.modeller.toolbar;

import java.util.List;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.sap.ui5.selenium.commons.Button;
import com.sap.ui5.selenium.core.BrowserWindow;
import com.sap.ui5.selenium.core.Frame;
import com.sap.ui5.selenium.core.UI5Object;
import com.sap.ui5.selenium.sap.fpa.ui.dashboard.DashboardPublishMenu;
import com.sap.ui5.selenium.sap.ui.unified.Menu;
import com.sap.ui5.selenium.unified.MenuItemBase;
import com.sap.ui5.selenium.utilities.SapUi5Factory;

public class NewToolbarItem extends Button {
	
	
	protected NewToolbarItem(String id, Frame frame) {
		super(id, frame);
	}



	public void assertValue(String value) {
		Assert.assertEquals(value.trim(), this.getButtonElement().getText().trim());
	}

	public boolean checkValue(String value) {
		return value.trim().equalsIgnoreCase(this.getButtonElement().getText().trim());
	}


	/**
	 * Check whether NewToolBarItem's Value shows as expected
	 */
	public boolean checkValueStatus(String value, Boolean expected){
		return this.checkValue(value);
	}
	

	/**
	 * Assert whether NewToolBarItem's Value shows as expected
	 */
	public void assertValueStatus(String value, Boolean expected){
		Boolean actual = this.checkValue(value);
		Assert.assertEquals("Value:" + value + "shows as expected", expected, actual);
	}

	public void assertEnabled(boolean enabled) {
		Assert.assertEquals(enabled, this.isEnabled());
	}

	public boolean checkEnabled(boolean enabled) {
		return isEnabled();
	}
	
	public void assertVisible(boolean enabled) {
		Assert.assertEquals(enabled, this.isVisible());
	}
	
	public boolean checkVisible(boolean visible) {
		return isVisible();
	}
	

	public void WaitUntilTextLoaded() {
		Wait<WebDriver> wait = new WebDriverWait(driver, 30);
		final NewToolbarItem thisItem = this;
		wait.until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver webdriver) {
				try{
					return !(thisItem.getButtonElement().getText().equalsIgnoreCase(""));
				}catch(Exception e){
					return false;
				}
			}
		});
	}
	
	
	
	//Button Method
	protected WebElement getButtonElement() {
		return driver.findElement(By.id(this.findRootWebElement().getAttribute("id"))).findElement(By.className("sapUiIcon"));
	}
	
	public void click(){
		Wait<WebDriver> wait = new WebDriverWait(driver, 30);
		
		wait.until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver webdriver) {
				return isVisible() && isEnabled();
			}
		});
		getButtonElement().click();
	}
	
	/**
	 * Check whether Button works as expected
	 */
	public boolean checkButtonEnabledStatus(String key, Boolean expected){
		return expected == isEnabled();
	}
	
	/**
	 * Assert whether Button works as expected 
	 */
	public void assertButtonEnabledStatus(boolean expected) {
		Assert.assertEquals("assert the status of button '"+ getId() +"', ", expected, isEnabled());
	}
	
	public void openItemMenuAndSelectEntry(final String menuId,final String item) {
		
		final BrowserWindow bw = this.windowManager.currentWindow();
		new WebDriverWait(driver, 60).until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver webdriver) {
				try {
					click();
					UI5Object o = SapUi5Factory.findUI5ObjectByIdEndsWith(bw,menuId, 5);

					if(o instanceof NewToolbarMenu) {		
						NewToolbarMenu menu =(NewToolbarMenu)o;
						menu.select(item);
						return true;
					}else if(o instanceof Menu ) {
						Menu menu = (Menu)o;
						menu.selectItem(item);
						return true;
					}else if(o instanceof DashboardPublishMenu){
						DashboardPublishMenu menu = (DashboardPublishMenu)o;
						menu.selectItem(item);
						return true;
					}else if(o instanceof com.sap.ui5.selenium.unified.Menu){
						com.sap.ui5.selenium.unified.Menu menu = (com.sap.ui5.selenium.unified.Menu)o;	
						List<MenuItemBase> items = menu.getItems();
						for(MenuItemBase itembase: items){
							WebElement option = bw.findWebElement(By.id(itembase.getId()));
							if(option.getText().equalsIgnoreCase(item)){
								option.click();
								break;
							}
						}
						return true;
					}
				}catch(Exception e){
					return false;
				}
				return false;
			}
		});
	}
}