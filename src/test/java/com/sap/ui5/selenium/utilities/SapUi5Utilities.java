package com.sap.ui5.selenium.utilities;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.sap.ui5.selenium.core.BrowserWindow;
import com.sap.ui5.selenium.sap.m.Dialog;
/**
 * For a good best practice, below methods are not supposed to use in test cases directly.
 */
public class SapUi5Utilities{

	public static By getEndsWithLocator(String Id){
		return  By.xpath("//*['" + Id + "'=substring(@id, string-length(@id)- string-length('" + Id + "')+1) and not(contains(@style,'display: none'))]");
	}

	/**
	 * 
	 * @param driver
	 * @param node
	 * @return
	 */
	public static WebElement getCheckBoxfrom(WebDriver driver, WebElement node){
		driver.manage().timeouts().implicitlyWait((long) 0.5, TimeUnit.SECONDS);
		WebElement cb = null;
		try{
			cb = node.findElements(By.xpath(".//span[@role='checkbox']")).get(0);
		}catch(Exception e){
			try{
				cb = node.findElement(By.className("sapMCb"));
			}catch(Exception ex){
				cb = node.findElement(By.className("sapEpmUiCheckBox"));
			}
		}
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		return cb!=null?cb:null;
	}
	 
	 
	/**
	  *
	  * @param driver
	  */
	public static void waitUntilMessageDisappear(WebDriver driver)
	{
		Wait<WebDriver> wait = new WebDriverWait(driver, 10);
	    try{
	    	driver.manage().timeouts().implicitlyWait((long) 0.3, TimeUnit.SECONDS);
	    	By messageclass = By.className("sapEpmUiShellInfoMessageSuccess");
    		if(driver.findElement(messageclass)!=null){
    			wait.until(ExpectedConditions.invisibilityOfElementLocated((messageclass)));
    		}
	    	
	    }catch(Exception e){
	    	
	    }
	    driver.manage().timeouts().implicitlyWait((long) 10, TimeUnit.SECONDS);
	}

	/**
	 * 
	 * @param driver
	 * @param timeout
	 */
	public static void waitUntilPageBusy(WebDriver driver,int... timeout){
		int defaultTimeout = timeout!=null &&timeout.length==1?timeout[0]:30;
		new WebDriverWait(driver, defaultTimeout).until(new ExpectedCondition<Boolean>(){
			public Boolean apply(WebDriver webdriver) {
				WebElement busyindicator,busydlg;
				webdriver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
				try{
					busyindicator = webdriver.findElement(By.className("sapUiLocalBusyIndicator"));
				}catch(Exception e){
					busyindicator=null;
				}
				try{
					busydlg = webdriver.findElement(By.className("sapMBusyDialog"));
				}catch(Exception e){
					busydlg=null;
				}
				webdriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
				return busyindicator!=null||busydlg!=null;
			}
		});
	 }
	 
	
	public static void waitUntilBlockLayerDisappear(WebDriver driver,int... timeout){
		 int defaultTimeout = timeout!=null && timeout.length==1?timeout[0]:30;
		 driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
		 new WebDriverWait(driver, defaultTimeout).until(new ExpectedCondition<Boolean>(){
			public Boolean apply(WebDriver input) {
				input.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
				try{
					if(input.findElement(By.className("sapUiBLy")).isDisplayed()){
						return false;
					}
				}catch(Exception e){
				}
				input.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
				return true;
			} 
		 });

	 }
	/**
	 * 
	 * @param driver
	 * @param timeout
	 */
	 public static void waitWhenPageBusy(WebDriver driver,int... timeout){
		 int defaultTimeout = timeout!=null && timeout.length==1?timeout[0]:30;
		 driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
		 new WebDriverWait(driver, defaultTimeout).until(new ExpectedCondition<Boolean>(){
			public Boolean apply(WebDriver input) {
				input.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
				try{
					if(input.findElement(By.className("sapUiLocalBusyIndicator")).isDisplayed()){
						return false;
					}
				}catch(Exception e){
				}
				try{
					if(input.findElement(By.className("sapMBusyDialog")).isDisplayed()){
						return false;
					}
				}catch(Exception e){
				}
				input.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
				return true;
			} 
		 });

	 }
	 
	 public static void leaveWithoutSave(BrowserWindow browserwindow){
		 if(SapUi5Factory.findWebElementImmediately(browserwindow, getEndsWithLocator("sap-fpa-ui-main-save-prompt"))!=null){
			 Dialog dlg = SapUi5Factory.findUI5ObjectById(browserwindow, "sap-fpa-ui-main-save-prompt");
			 dlg.ok();
		 }
	 }
	 

	 

	 
}
