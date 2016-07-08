package com.sap.ui5.selenium.sap.fpa.ui.control.repeat;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.sap.ui5.selenium.core.BrowserWindow;
import com.sap.ui5.selenium.core.Frame;
import com.sap.ui5.selenium.sap.fpa.ui.common.CompoundControl;
import com.sap.ui5.selenium.utilities.SapUi5Utilities;

public class Repeat extends CompoundControl{
	
	protected Repeat(String id, Frame frame) {
		super(id, frame);
		
	}
	
	
	public static Repeat findRepeatByIdEndsWith(BrowserWindow bw, String id,int...timeout) {
		By locator = SapUi5Utilities.getEndsWithLocator(id);
		int defaulttimeout = (timeout!=null && timeout.length==1)?timeout[0]:30;
		long start = System.currentTimeMillis();
		while (defaulttimeout * 1000 > System.currentTimeMillis() - start) {	
	    	try{
	    		List<WebElement> elementList = bw.syncAjax().driver().findElements(locator);
	    		for(WebElement el:elementList){
	    			if(el.isDisplayed()){
	    				return new Repeat(el.getAttribute("id"),bw.getMainFrame());
	    			}
	    		}
	    	}catch(Exception e){
	    	}
		} 		
		throw new TimeoutException("Could not find element within timeout " + defaulttimeout + " seconds: " + locator.toString());
	}
	
	private Repeat getThisRepeat(){
		return this;
	}
	
	public boolean selectRows(String... values) {
		waitUntilInitialized();
		
		List<WebElement> rows = driver.findElement(By.id(this.getId())).findElement(By.className("sapUiRrPage")).findElements(By.className("sapUiRrRow"));
		int selected = 0;
		for(int i=0;i<rows.size();i++){
			WebElement row = rows.get(i);
			List<WebElement> childwrappers = row.findElements(By.className("sapUiHLayoutChildWrapper"));
			for(int j=0;j<values.length;j++){
				if(childwrappers.get(2).findElement(By.tagName("a")).getText().equalsIgnoreCase(values[j])){
					((com.sap.ui5.selenium.sap.m.CheckBox)this.getUI5Object(By.id(SapUi5Utilities.getCheckBoxfrom(driver,childwrappers.get(0)).getAttribute("id")))).check();
					selected++;
					break;
				}
			}
			if(selected==values.length){
				break;
			}
		}
		return selected>0;
	}
	
	public void clickRow(final String value) {
		waitUntilInitialized();
		new WebDriverWait(driver, 30).until(new ExpectedCondition<Boolean>(){
			public Boolean apply(WebDriver input) {
				try{
					List<WebElement> rows = driver.findElement(By.id(getThisRepeat().getId())).findElement(By.className("sapUiRrPage")).findElements(By.className("sapUiRrRow"));

					for(int i=0;i<rows.size();i++){
						WebElement row = rows.get(i);
						List<WebElement> childwrappers = row.findElements(By.className("sapUiHLayoutChildWrapper"));
							if(childwrappers.get(2).findElement(By.tagName("a")).getText().equalsIgnoreCase(value)){
								getThisRepeat().getWebElement(By.linkText(childwrappers.get(2).findElement(By.tagName("a")).getText())).click();
								return true;
							}
					}
					return false;
				}catch(Exception e){
					return false;
				}
				
			}
			
		});
	}
	

	public boolean checkItemExist(String value) {
		waitUntilInitialized();
		List<WebElement> rows = driver.findElement(By.id(this.getId())).findElement(By.className("sapUiRrPage")).findElements(By.className("sapUiRrRow"));
		for(int i=0;i<rows.size();i++){
			WebElement row = rows.get(i);
			List<WebElement> childwrappers = row.findElements(By.className("sapUiHLayoutChildWrapper"));
				if(childwrappers.get(2).findElement(By.tagName("a")).getText().equalsIgnoreCase(value)){
					return true;
				}
		}
		return false;
	}
	
	/**
	 * check only these items exist 
	 * @param values
	 * @return
	 */
	public boolean checkItemsExistOnly(String... values){
		waitUntilInitialized();
		int length = values.length;
		List<WebElement> rows = driver.findElement(By.id(this.getId())).findElement(By.className("sapUiRrPage")).findElements(By.className("sapUiRrRow"));
		if (values.length!=rows.size()) return false;
		for(int i=0;i<values.length;i++){
			for(int j=0;j<rows.size();j++){
				WebElement row = rows.get(i);
				List<WebElement> childwrappers = row.findElements(By.className("sapUiHLayoutChildWrapper"));
				if(childwrappers.get(2).findElement(By.tagName("a")).getText().equalsIgnoreCase(values[i])){
					length--;
					break;
				}	
			}
		}
		if (length==0) return true; 
			else return false;
	}

	/**
	 * Assert Item exist
	 * @param value
	 * @param expected
	 */
	public void assertItemExist(String value, boolean expected) {
		boolean actual = checkItemExist(value);
		Assert.assertEquals("Assert Item:" + value + " exists", expected, actual);
		
	}

	
	
	public void waitUntilInitialized(){
		Wait<WebDriver> wait = new WebDriverWait(driver, 30).pollingEvery(100, TimeUnit.MILLISECONDS);
		wait.until(new ExpectedCondition<Boolean>(){
			public Boolean apply(WebDriver webdriver) {
				try{
					webdriver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
					List<WebElement> nodata = webdriver.findElement(By.id(getThisRepeat().getId())).findElements(By.className("sapUiRrNoData"));
					webdriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
					if(nodata.size()==1){
						return true;
					}

					List<WebElement> items = webdriver.findElement(By.id(getThisRepeat().getId())).findElement(By.className("sapUiRrPage")).findElements(By.className("sapUiRrRow"));
					return items.get(items.size()-1).getText().length()>0;
					
				}catch(Exception e){
					webdriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
				}
				return false;
			}
		});
		
	}

	public void watiUntilRefreshed(){
		Wait<WebDriver> wait = new WebDriverWait(driver, 30);
		wait.until(ExpectedConditions.refreshed(new ExpectedCondition<Boolean>(){
			public Boolean apply(WebDriver webdriver) {
				try{
					boolean initialized = false;
					List<WebElement> nodata = webdriver.findElement(By.id(getThisRepeat().getId())).findElements(By.className("sapUiRrNoData"));
					if(nodata.size()==1){
						return true;
					}
					List<WebElement> items = webdriver.findElement(By.id(getThisRepeat().getId())).findElements(By.className("sapUiRrRow"));
					for(WebElement item:items){
						if(item.getText().length()>0){
							initialized = true;
						}else{
							initialized = false;
						}
					}
					return initialized;
				}catch(Exception e){
					return false;
				}
			}
		}));
		
	}
	
	
}
