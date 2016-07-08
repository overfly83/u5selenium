package com.sap.ui5.selenium.sap.ui.unified;

import java.util.List;

import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;

import com.sap.ui5.selenium.core.BrowserWindow;
import com.sap.ui5.selenium.core.Frame;
import com.sap.ui5.selenium.core.UI5By;
import com.sap.ui5.selenium.unified.MenuItemBase;
import com.sap.ui5.selenium.utilities.SapUi5Utilities;

public class Menu extends com.sap.ui5.selenium.unified.Menu {

	protected Menu(String id, Frame frame) {
		super(id, frame);
	}
	
	
	public static Menu findByIdEndsWith(BrowserWindow bw, String id,int...timeout) {
		int defaulttimeout = (timeout!=null && timeout.length==1)?timeout[0]:30;
		long start = System.currentTimeMillis();
		while (defaulttimeout * 1000 > System.currentTimeMillis() - start) {	
	    	try{
	    		List<WebElement> elementList = bw.syncAjax().driver().findElements(SapUi5Utilities.getEndsWithLocator(id));
	    		for(WebElement el:elementList){
	    			if(el.isDisplayed()){
	    				return new Menu(el.getAttribute("id"),bw.getMainFrame());
	    			}
	    		}
	    	}catch(Exception e){
	    	}
		} 		
		throw new TimeoutException("Could not find element within timeout " + defaulttimeout + " seconds: " + SapUi5Utilities.getEndsWithLocator(id).toString());
	}
	
	public void selectItem(String key){
		List<MenuItemBase> items = this.getItems();
		for(MenuItemBase itembase: items){
			WebElement item = this.findWebElement(UI5By.id(itembase.getId()));
			if(item.getText().equalsIgnoreCase(key)){
				item.click();
				break;
			}
		}
	}
	
	
	

}
