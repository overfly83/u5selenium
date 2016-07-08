package com.sap.ui5.selenium.utilities;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.sap.ui5.selenium.core.BrowserWindow;
import com.sap.ui5.selenium.core.UI5Object;

/**
 * 
 * @author I058399
 *
 */
public class SapUi5Factory{

	private static <T extends UI5Object> T safeFind(BrowserWindow bw, By locator,int... timeout){
		int defaulttimeout = (timeout!=null && timeout.length==1)?timeout[0]:60;
		long start = System.currentTimeMillis();
		while (defaulttimeout * 1000 > System.currentTimeMillis() - start) {	
	    	try{
	    		bw.driver().manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
	    		List<WebElement> elementList = bw.driver().findElements(locator);
	    		for(WebElement el:elementList){
	    			if(el.isDisplayed()){
	    				bw.driver().manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	    				return bw.find(el.getAttribute("id"));
	    			}
	    		}
	    	}catch(ClassCastException e){
	    		throw e;
	    	}catch(Exception e){}
		}
		bw.driver().manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		throw new TimeoutException("Could not find element within timeout " + defaulttimeout + " seconds: " + locator.toString());
	}
	
	
	@SuppressWarnings("unchecked")
	public static <T extends UI5Object> List<T> findUI5Objects(BrowserWindow bw,By locator,int...timeout){
		List<T> ui5ObjList = new ArrayList<T>();
		
		int defaulttimeout = (timeout!=null && timeout.length==1)?timeout[0]:30;
		long start = System.currentTimeMillis();
		while (defaulttimeout * 1000 > System.currentTimeMillis() - start) {
			try{
				try{
					Wait<WebDriver> wait = new WebDriverWait(bw.syncAjax().driver(), 1).pollingEvery(100, TimeUnit.MILLISECONDS);
					wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
				}catch(Exception e){}
				List<WebElement> cl = bw.findWebElements(locator);
				for(WebElement cb:cl){
					if(cb.isDisplayed()){
						ui5ObjList.add((T) bw.syncAjax().find(By.id(cb.getAttribute("id"))));
					}
				}
				return ui5ObjList;
			}catch(ClassCastException e){
	    		throw new ClassCastException(e.getMessage());
	    	}catch(Exception e){}
		}
		throw new TimeoutException("Could not find elements within timeout " + defaulttimeout + " seconds: " + locator.toString());
	}
	
	
	
	
	public static <T extends UI5Object> T findUI5ObjectByIdEndsWith(BrowserWindow bw,String Id,int...timeout){
		return safeFind(bw,SapUi5Utilities.getEndsWithLocator(Id),timeout);
	}
	
	public static <T extends UI5Object> T findUI5ObjectByLocator(BrowserWindow bw,By locator,int...timeout){
		By Locator = locator;
		return safeFind(bw,Locator,timeout);
	}
	
	public static <T extends UI5Object> T findUI5ObjectById(BrowserWindow bw,String Id, int...timeout){
		By xpath = By.id(Id);
		return safeFind(bw,xpath,timeout);
	}
	
	public static <T extends UI5Object> T findUI5ObjectByClass(BrowserWindow bw,String classname,int...timeout){
		By className = By.className(classname);
		return safeFind(bw,className,timeout);
	}
	
	/*****************************************Find WebElement**************************************************/
	private static WebElement findWebElement(BrowserWindow bw, By locator,int... timeout){
		int defaulttimeout = (timeout!=null && timeout.length==1)?timeout[0]:30;
		long start = System.currentTimeMillis();
		while (defaulttimeout * 1000 > System.currentTimeMillis() - start) {
	    	try{
	    		List<WebElement> elementList = bw.syncAjax().driver().findElements(locator);
	    		for(WebElement el:elementList){
	    			if(el.isDisplayed()){
	    				return el;
	    			}
	    		}
	    	}catch(Exception e){
	    	}
		}
		throw new TimeoutException("Could not find element within timeout " + defaulttimeout + " seconds: "	+ locator.toString());
	}
	
	public static WebElement findWebElementByLocator(BrowserWindow bw,By locator,int...timeout){
		By Locator = locator;
		return findWebElement(bw,Locator,timeout);
	}
	
	public static WebElement findWebElementByIdEndsWith(BrowserWindow bw,String Id,int...timeout){
		return findWebElement(bw,SapUi5Utilities.getEndsWithLocator(Id),timeout);
	}

	public static WebElement findWebElementByClass(BrowserWindow bw,String classname,int...timeout){
		By loc = By.className(classname);
		return findWebElement(bw,loc,timeout);
	}


	public static List<WebElement> findWebElements(BrowserWindow bw,By locator,int...timeout){
		final By loc = locator;
		int defaulttimeout = (timeout!=null && timeout.length==1)?timeout[0]:30;
		long start = System.currentTimeMillis();
		while (defaulttimeout * 1000 > System.currentTimeMillis() - start) {
			try{
				try{
					Wait<WebDriver> wait = new WebDriverWait(bw.syncAjax().driver(), 1).pollingEvery(100, TimeUnit.MILLISECONDS);
					wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(loc));
				}catch(Exception e){}
				return bw.syncAjax().driver().findElements(loc);
			}catch(Exception e){}
		}
		throw new TimeoutException("Could not find elements within timeout " + defaulttimeout + " seconds: " + loc.toString());
	}
	
	public static WebElement findWebElementImmediately(BrowserWindow bw, By locator){
		bw.driver().manage().timeouts().implicitlyWait((long) 0.5, TimeUnit.SECONDS);
		try{
			WebElement element = bw.driver().findElement(locator);
			bw.driver().manage().timeouts().implicitlyWait((long) 10, TimeUnit.SECONDS);
			if(element!=null){
				return element;
			}
		
		}catch(Exception e){
			bw.driver().manage().timeouts().implicitlyWait((long) 10, TimeUnit.SECONDS);
		}
		return null;
	}
	
	public static WebElement findWebElementImmediately(BrowserWindow bw, String Id){
		bw.driver().manage().timeouts().implicitlyWait((long) 0.5, TimeUnit.SECONDS);
		try{
			WebElement element = bw.driver().findElement(SapUi5Utilities.getEndsWithLocator(Id));
			bw.driver().manage().timeouts().implicitlyWait((long) 10, TimeUnit.SECONDS);
			if(element!=null){
				return element;
			}
		
		}catch(Exception e){
			bw.driver().manage().timeouts().implicitlyWait((long) 10, TimeUnit.SECONDS);
		}
		return null;
	}
	
	
	
}
