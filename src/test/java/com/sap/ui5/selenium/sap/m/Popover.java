package com.sap.ui5.selenium.sap.m;


import java.util.List;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.sap.ui5.selenium.core.Frame;
import com.sap.ui5.selenium.sap.fpa.ui.common.CompoundControl;


public class Popover extends CompoundControl{

	protected Popover(String id, Frame frame) {
		super(id, frame);
	}
	
	
	
	public void select(String key){
		List<WebElement> items = this.getItems();
		for(final WebElement it:items){
			if(it.getText().trim().equalsIgnoreCase(key.trim())){
				new WebDriverWait(driver, 20).until(new ExpectedCondition<Boolean>(){
					public Boolean apply(WebDriver webdriver) {
						try{
							Thread.sleep(1000);
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

	/**
	 * This method return item as a list
	 * @return
	 */
	private List<WebElement> getItems(){
		List<WebElement> items = this.findRootWebElement().findElements(By.xpath(".//div[contains(@class,'sapMPopoverScroll')]//div[contains(@class,'Item')]"));	
		return items;
	}

	public void assertItemVisible(String key,boolean visible){
		List<WebElement> items = this.getItems();
		for(WebElement it:items){
			if(it.getText().trim().equalsIgnoreCase(key.trim())){
				Assert.assertEquals("Key: "+ key+" is found in menu list", visible,true);
				return;
			}	
		}
		Assert.assertEquals("Key: "+ key+" is not found in menu list", visible, false);
	}
	
	
	public boolean checkItemVisible(String key){
		List<WebElement> items = this.getItems();
		for(WebElement it:items){
			if(it.getText().trim().equalsIgnoreCase(key.trim())){
				return true;
			}	
		}
		return false;
	}
	
	public void assertItemEnabled(String key,boolean enabled){
		List<WebElement> items = this.getItems();
		boolean itemenabled;
		for(WebElement it:items){
			if(it.getText().trim().equalsIgnoreCase(key.trim())){
				if(it.isEnabled()){
					itemenabled = true;
				}else{
					itemenabled = false;
				}
				Assert.assertEquals("Key: "+ key+" is enabled in menu list", enabled,itemenabled);
				return;
			}
		}
		Assert.fail("Key: "+ key+" is not found in menu list");
	}
	
	public boolean checkItemEnabled(String key){
		List<WebElement> items = this.getItems();
		boolean itemenabled;
		for(WebElement it:items){
			if(it.getText().trim().equalsIgnoreCase(key.trim())){
				if(it.isEnabled()){
					itemenabled = true;
				}else{
					itemenabled = false;
				}
				return itemenabled;
			}
		}
		return false;
	}
	
	
	
	
}