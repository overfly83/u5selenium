package com.sap.ui5.selenium.sap.fpa.ui.control.modeller;

import java.util.List;

//import junit.framework.Assert;


import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.sap.ui5.selenium.core.Frame;
import com.sap.ui5.selenium.sap.fpa.ui.common.CompoundControl;

public class OptionSlider extends CompoundControl{

	protected OptionSlider(String id, Frame frame) {
		super(id, frame);
	}

	
	public List<WebElement> getItems() {
		return this.findRootWebElement().findElements(By.className("sapEpmOptionSliderItem"));
	}
	
	/**
	 * Check whether OptionSlider's item exists as expected
	 */
	public boolean checkItemExistsStatus(String key, Boolean expected){
		List<WebElement> items = this.getItems();
		Boolean actual = false;
		for (WebElement it:items){
			if(it.getText().trim().equalsIgnoreCase(key.trim())){
				actual = true;
			}
		}
		return actual == expected;
	}
	
	/**
	 * Assert whether OptionSlider's item exists as expected
	 */
	public void assertItemExistsStatus(String key, Boolean expected){
		List<WebElement> items = this.getItems();
		Boolean actual = false;
		for (WebElement it:items){
			if(it.getText().trim().equalsIgnoreCase(key.trim())){
				actual = true;
			}
		}
		Assert.assertEquals("assert Item:" + key +" exists", expected, actual);
	}
	
	/**
	 * 
	 * @param index start from 0
	 */
	public void select(int index){
		List<WebElement> items = this.getItems();
		WebElement we = items.get(index);
		we.click();
				
	}
	/**
	 * 
	 * @param key
	 */
	public void select(String key){
		List<WebElement> items = this.getItems();
		for(final WebElement we:items){
			if(we.getText().equalsIgnoreCase(key)){
				new WebDriverWait(driver, 20).until(new ExpectedCondition<Boolean>(){
					public Boolean apply(WebDriver webdriver) {
						try{
							Thread.sleep(1000);
							we.click();
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
	
	
	
}
