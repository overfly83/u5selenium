package com.sap.ui5.selenium.sap.fpa.ui.control.commons;


import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.sap.ui5.selenium.core.Frame;
import com.sap.ui5.selenium.utils.Browser;

public class Input extends com.sap.ui5.selenium.core.Control {
	

	protected Input(String id, Frame frame) {
		super(id, frame);
	}

	private WebElement getInputElement() {
		return this.findRootWebElement().findElements(By.className("sapEpmUiInputTextArea")).get(0);
	}
	
	public String getValue(){
		WebElement input = this.getInputElement();
		String value = null;
		if(input.getTagName().equals("div")){
			value = input.getText();
		}else if(input.getTagName().equals("input")){
			if(input.getAttribute("type").equalsIgnoreCase("password")){
				String tempID = this.getId().replaceAll("\\.", "\\\\\\\\.");
				String script = "return $('#"+tempID+"').find('.password').val()";
				value = (String) ((JavascriptExecutor) driver).executeScript(script);
			}else{
				value = input.getAttribute("value");
			}
		}
		
		
		return value;
	}
	
	
		
	public void setValue(String text,boolean...triggerChange) {

		
		final String newValue = text;
		new WebDriverWait(driver, 30).until(new ExpectedCondition<Boolean>(){
			public Boolean apply(WebDriver webdriver) {
				try{
					WebElement input = getInputElement();
					
					// make sure that the input element has the focus by clicking it
					input.click();
					input.clear();
					input.sendKeys(newValue); // set the value
					Thread.sleep(1000);
					return getValue().equals(newValue);
					
				}catch(Exception e){
					//e.printStackTrace();
					return false;
				}
				
			}
		});
		boolean trigger = triggerChange!=null && triggerChange.length==1?triggerChange[0]:false;
		if (trigger) {
			getInputElement().sendKeys(Keys.RETURN);
		}
	}
	
	
	public void clearValue(boolean triggerChange) {

		
		
		if (browser.is(Browser.INTERNETEXPLORER)) {
			getInputElement().sendKeys("");
		}else{
			new WebDriverWait(driver, 30).until(new ExpectedCondition<Boolean>(){
				public Boolean apply(WebDriver webdriver) {
					try{
						getInputElement().clear();
						Thread.sleep(500);
						return getValue().length() == 0;
					}catch(Exception e){
						e.printStackTrace();
						return false;
					}
					
				}
			});
		}
		if (triggerChange) {
			getInputElement().sendKeys(Keys.RETURN);
		}
	}
	
}
