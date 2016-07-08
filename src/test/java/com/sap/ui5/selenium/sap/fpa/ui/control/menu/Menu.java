package com.sap.ui5.selenium.sap.fpa.ui.control.menu;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.sap.ui5.selenium.core.Control;
import com.sap.ui5.selenium.core.Frame;
import com.sap.ui5.selenium.sap.fpa.ui.control.menu.Item;

public class Menu extends Control {

	protected Menu(String id, Frame frame) {
		super(id, frame);
	}
	
	public void selectByIndex(int index){
		WaitUntilDataLoaded();
		List<Item> items = this.getItems();
		items.get(index).click();
	}
	
	public void select(String key){
		WaitUntilDataLoaded();
		List<Item> items = this.getItems();
		for(Item it:items){
			if(it.getText().trim().equalsIgnoreCase(key.trim())){
				it.click();
				return;
			}
		}
	}

	public List<Item> getItems(){
		List<Control> items = this.js.exec(".getItems()").getList(Control.class);
		List<Item> menuItems = new ArrayList<Item>();
		for(Control it:items){
			if(it.getClass().getName().equals(com.sap.ui5.selenium.sap.fpa.ui.control.menu.Item.class.getName())){
				menuItems.add((Item) it);
			}
			
		}
		return menuItems;
	}

	public void assertItemVisible(String key,boolean visible){
		List<Item> items = this.getItems();
		for(Item it:items){
			if(it.getText().trim().equalsIgnoreCase(key.trim())){
				Assert.assertEquals("Key: "+ key+" is found in menu list", visible,true);
				return;
			}	
		}
		Assert.assertEquals("Key: "+ key+" is not found in menu list", visible, false);
	}
	
	
	public boolean checkItemVisible(String key){
		List<Item> items = this.getItems();
		for(Item it:items){
			if(it.getText().trim().equalsIgnoreCase(key.trim())){
				return true;
			}	
		}
		return false;
	}
	
	public void assertItemEnabled(String key,boolean enabled){
		List<Item> items = this.getItems();
		boolean itemenabled = false;
		for(Item it:items){
			if(it.getText().trim().equalsIgnoreCase(key.trim())){
				if(it.isEnabled()){
					itemenabled = true;
				}
				Assert.assertEquals("Key: "+ key+" is enabled in menu list", enabled,itemenabled);
				return;
			}
		}
		Assert.fail("Key: "+ key+" is not found in menu list");
	}
	
	public boolean checkItemEnabled(String key){
		List<Item> items = this.getItems();
		boolean itemenabled = false;
		for(Item it:items){
			if(it.getText().trim().equalsIgnoreCase(key.trim())){
				if(it.isEnabled()){
					itemenabled = true;
				}
				return itemenabled;
			}
		}
		return false;
	}
	
	private void WaitUntilDataLoaded(){
		List<Item> menuitems = this.getItems();
		Wait<WebDriver> wait = new WebDriverWait(driver, 20);
		for(final Item it:menuitems){
			wait.until(new ExpectedCondition<Boolean>(){
				public Boolean apply(WebDriver webdriver) {
					return !it.getText().equalsIgnoreCase("");
				}
			});
		}
	}
}
