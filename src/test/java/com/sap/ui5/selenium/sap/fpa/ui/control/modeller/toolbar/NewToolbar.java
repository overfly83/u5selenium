package com.sap.ui5.selenium.sap.fpa.ui.control.modeller.toolbar;

import java.util.List;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.sap.ui5.selenium.core.Frame;
import com.sap.ui5.selenium.sap.fpa.ui.common.CompoundControl;

public class NewToolbar extends CompoundControl{

	protected NewToolbar(String id, Frame frame) {
		super(id, frame);
	}
	
	
	private List<WebElement> getItems() {
		return this.findRootWebElement().findElements(By.xpath(".//div[contains(@class, 'sapEpmModelerToolbarItem')]"));
	}
	
	private NewToolbar getThisNewToolbar(){
		return this;
	}
	
	
	
	/**
	 * 
	 * @param index start from 1
	 */
	public void select(final int index){
		new WebDriverWait(driver, 10).until(new ExpectedCondition<Boolean>(){
			public Boolean apply(WebDriver input) {
				try{
					List<WebElement> items = getThisNewToolbar().getItems();
					WebElement we = items.get(index-1);
					NewToolbarItem item = getThisNewToolbar().getUI5Object(By.id(we.getAttribute("id")));
					item.click();
					return true;
				}catch(Exception e){
					return false;
				}
			}
			
		});
		
	}
	
	/**
	 * Check NewToolbarItem exists in the position(index)
	 * @param index start from 1
	 * @param key, item's value
	 * @return if item exists in the position(index)
	 */
	
	public boolean checkNewToolbarItemExists(int index, String key){
		List<WebElement> items = this.getItems();
		WebElement we = items.get(index-1);
		NewToolbarItem item = this.getUI5Object(By.id(we.getAttribute("id")));
		return (item.getText().trim().equalsIgnoreCase(key.trim()));
	}
	
	/**
	 * Assert NewToolbarItem exists in the position(index)
	 * @param index start from 1
	 * @param key, item's value
	 * @param expected
	 */
	public void assertNewToolbarItemExists(int index, String key, Boolean expected){
		List<WebElement> items = this.getItems();
		WebElement we = items.get(index-1);
		NewToolbarItem item = this.getUI5Object(By.id(we.getAttribute("id")));
		Boolean actual = false;
		actual = item.getText().trim().equalsIgnoreCase(key.trim());
		Assert.assertEquals("assert NetToolbarItem:" + key + " exists in" + index +"position", expected, actual);
	}
	
	
	public void waitUntilTextEqualsTo(String text){
		final String value = text;
		Wait<WebDriver> wait = new WebDriverWait(driver, 10);
		wait.until(new ExpectedCondition<Boolean>(){
			public Boolean apply(WebDriver webdriver) {
				try{
					return getThisNewToolbar().findRootWebElement().getText().equalsIgnoreCase(value);
				}catch(Exception e){
					return false;
				}
			}
		});

	}
	
	public void waitUntilTextEndsWith(String text){
		final String value = text;
		Wait<WebDriver> wait = new WebDriverWait(driver, 10);
		wait.until(new ExpectedCondition<Boolean>(){
			public Boolean apply(WebDriver webdriver) {
				try{
					return getThisNewToolbar().findRootWebElement().getText().endsWith(value);
				}catch(Exception e){
					return false;
				}
			}
		});
	}
	
}
