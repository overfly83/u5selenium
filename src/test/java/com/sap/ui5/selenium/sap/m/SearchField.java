package com.sap.ui5.selenium.sap.m;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

import org.junit.Assert;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.sap.ui5.selenium.core.Frame;
import com.sap.ui5.selenium.core.UI5By;

public class SearchField extends com.sap.ui5.selenium.commons.SearchField{

	protected SearchField(String id, Frame frame) {
		super(id, frame);
	}
	
	private SearchField getThisSearchField(){
		return this;
	}
	@Override
	public void clearValue(){
		final WebElement input = getThisSearchField().findWebElement(UI5By.idSuffix("-reset"));
		if(input.isDisplayed()){
			input.click();
		}
	}
	
	@Override
	public void setValue(String value){
		final String textValue = value;
		final WebElement input = getThisSearchField().findWebElement(UI5By.idSuffix("-I"));
		new WebDriverWait(driver, 20).until(new ExpectedCondition<Boolean>(){
			public Boolean apply(WebDriver webdriver) {
				try{
					if(input.getAttribute("value").equals(textValue)){	
						return true;
					}else{
						clearValue();
						StringSelection stringSelection = new StringSelection(textValue);
						Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
						clpbrd.setContents(stringSelection, stringSelection);
						
						input.sendKeys(Keys.CONTROL,"v");
						//input.sendKeys(textValue);
						return false;
					}
				}catch(Exception e){
					return false;
				}
			}
		});
	}
	@Override
	public void triggerSearch(){
		getThisSearchField().findWebElement(UI5By.idSuffix("-search")).click();
	}
	
	/** 
	 * check if search button is enable
	 * @return 
	 */
	public boolean checkSearchable() {
		return getThisSearchField().findWebElement(UI5By.idSuffix("-search")).isEnabled();
	}
	
	/** 
	 * assert if search button is enable
	 */
	public void assertSearchable(boolean expected) {
		Assert.assertEquals("assert searchable of SearchField '" +this.getId(), expected, checkSearchable());
	}
	
	/** 
	 * check if text of SearchText is as expected
	 * @return 
	 */
	public boolean checkSearchText(String expected) {
		String actual = this.findWebElement(UI5By.idSuffix("-I")).getAttribute("value");
		if(actual == null) {
			return expected == null;
		}
		return actual.equals(expected);
	}
	
	/** 
	 * assert if text of SearchText is as expected
	 */
	public void assertSearchText(String expected) {
		String actual = this.findWebElement(UI5By.idSuffix("-I")).getAttribute("value");
		Assert.assertEquals("assert search text of SearchField '" +this.getId(), expected, actual);
	}
}
