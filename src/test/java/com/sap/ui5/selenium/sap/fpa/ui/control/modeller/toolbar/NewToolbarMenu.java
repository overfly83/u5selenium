package com.sap.ui5.selenium.sap.fpa.ui.control.modeller.toolbar;


import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.sap.ui5.selenium.core.Control;
import com.sap.ui5.selenium.core.Frame;
import com.sap.ui5.selenium.sap.fpa.ui.common.CompoundControl;


public class NewToolbarMenu extends CompoundControl{


	protected NewToolbarMenu(String id, Frame frame) {
		super(id, frame);
	}
	

	/**
	 * 
	 * @param index start from 1
	 */
	public void selectByIndex(int index){
		WaitUntilDataLoaded();
		List<NewToolbarMenuItem> items = this.getItems();
		items.get(index-1).click();
	}
	
	public void select(String key){
		WaitUntilDataLoaded();
		List<NewToolbarMenuItem> items = this.getItems();
		for(final NewToolbarMenuItem it:items){
			if(it.getText().trim().equalsIgnoreCase(key.trim())){
				new WebDriverWait(driver, 30).until(new ExpectedCondition<Boolean>(){
					public Boolean apply(WebDriver webdriver) {
						try{
							it.click();
							return true;
						}catch(Exception e){
							return false;
						}
					}
				});

				return;
			}
		}
	}

	public List<NewToolbarMenuItem> getItems(){
		List<Control> items = this.js.exec(".getItems()").getList(Control.class);
		List<NewToolbarMenuItem> menuItems = new ArrayList<NewToolbarMenuItem>();
		for(Control it:items){
			if(it.getClass().getName().equals(com.sap.ui5.selenium.sap.fpa.ui.control.modeller.toolbar.NewToolbarMenuItem.class.getName())){
				menuItems.add((NewToolbarMenuItem) it);
			}
			
		}
		return menuItems;
	}

	public void assertItemVisible(String key,boolean visible){
		List<NewToolbarMenuItem> items = this.getItems();
		for(NewToolbarMenuItem it:items){
			if(it.getText().trim().equalsIgnoreCase(key.trim())){
				Assert.assertEquals("Key: "+ key+" is found in menu list", visible,true);
				return;
			}	
		}
		Assert.assertEquals("Key: "+ key+" is not found in menu list", visible, false);
	}
	
	
	public boolean checkItemVisible(String key){
		List<NewToolbarMenuItem> items = this.getItems();
		for(NewToolbarMenuItem it:items){
			if(it.getText().trim().equalsIgnoreCase(key.trim())){
				return true;
			}	
		}
		return false;
	}
	
	public void assertItemEnabled(String key,boolean enabled){
		List<NewToolbarMenuItem> items = this.getItems();
		boolean itemenabled = false;
		for(NewToolbarMenuItem it:items){
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
		List<NewToolbarMenuItem> items = this.getItems();
		boolean itemenabled = false;
		for(NewToolbarMenuItem it:items){
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
		
		Wait<WebDriver> wait = new WebDriverWait(driver, 20);
		List<NewToolbarMenuItem> menuitems = this.getItems();
		for(final NewToolbarMenuItem it:menuitems){
			
			wait.until(new ExpectedCondition<Boolean>(){
				public Boolean apply(WebDriver webdriver) {
					return !it.getText().equalsIgnoreCase("");
				}
			});
		}
	}
	
	
}