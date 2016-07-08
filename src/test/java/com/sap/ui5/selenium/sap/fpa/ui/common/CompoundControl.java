package com.sap.ui5.selenium.sap.fpa.ui.common;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.By.ByXPath;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.sap.ui5.selenium.core.Control;
import com.sap.ui5.selenium.core.Frame;
import com.sap.ui5.selenium.core.UI5By;
import com.sap.ui5.selenium.core.UI5Object;
import com.sap.ui5.selenium.utilities.SapUi5Utilities;

public class CompoundControl extends Control {

	protected CompoundControl(String id, Frame frame) {
		super(id, frame);
	}
	
	public  <T extends UI5Object> T getUI5ObjectByIdEndsWith(String Id,int...timeout){
		By locator = SapUi5Utilities.getEndsWithLocator(Id);
		locator = changeToHierarchyMode(locator);
		return this.getUI5Object(locator,timeout);
	}
	
	public  WebElement getWebElementByIdEndsWith(String Id,int...timeout){
		By locator = SapUi5Utilities.getEndsWithLocator(Id);
		locator = changeToHierarchyMode(locator);
		return this.getWebElement(locator,timeout);
	}

	private By changeToHierarchyMode(By locator){
		if(locator.getClass().equals(ByXPath.class)){
			String newlocator = locator.toString().replace("By.xpath: ", "");
			if(!newlocator.startsWith(".")){
				newlocator = "."+newlocator;
			}
			return By.xpath(newlocator);
		}
		return locator;
	}
	public <T extends UI5Object> T getUI5Object(By locator,int...timeout){
		locator = changeToHierarchyMode(locator);
		long start = System.currentTimeMillis();
		int defaultTimeout = timeout!=null && timeout.length==1?timeout[0]:30;
		while (defaultTimeout * 1000 > System.currentTimeMillis() - start) {	
	    	try{
	    		java.util.List<WebElement> elementList = this.findRootWebElement().findElements(locator);
	    		for(WebElement el:elementList){
	    			if(el.isDisplayed()){
	    				return this.find(UI5By.id(el.getAttribute("id")));
	    			}
	    		}
	    	}catch(ClassCastException e){
	    		throw new ClassCastException(e.getMessage());
	    	}catch(Exception e){}
		} 	
		throw new TimeoutException("Could not find element within timeout 30 seconds: " + locator.toString());
	}
	
	@SuppressWarnings("unchecked")
	public <T extends UI5Object> List<T> getUI5Objects(By locator,int...timeout){
		locator = changeToHierarchyMode(locator);
		long start = System.currentTimeMillis();
		int defaultTimeout = timeout!=null && timeout.length==1?timeout[0]:30;
		List<T> elements = new ArrayList<T>();
		while (defaultTimeout * 1000 > System.currentTimeMillis() - start) {	
			try{
				try{
					Wait<WebDriver> wait = new WebDriverWait(driver, 1).pollingEvery(100, TimeUnit.MILLISECONDS);
					wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
				}catch(Exception e){}
	    		List<WebElement> elementList = this.findRootWebElement().findElements(locator);
	    		for(WebElement el:elementList){
	    			if(el.isDisplayed()){
	    				elements.add((T) this.find(UI5By.id(el.getAttribute("id"))));
	    			}	
	    		}
			    return elements;
			 }catch(ClassCastException e){
		    		throw new ClassCastException(e.getMessage());
			 }catch(Exception e){}
		} 	
		throw new TimeoutException("Could not find element within timeout "+defaultTimeout+" seconds: " + locator.toString());
	}
	
	public WebElement getWebElement(By locator,int...timeout){
		locator = changeToHierarchyMode(locator);
		long start = System.currentTimeMillis();
		int defaultTimeout = timeout!=null && timeout.length==1?timeout[0]:30;
		while (defaultTimeout * 1000 > System.currentTimeMillis() - start) {
	    	try{
	    		java.util.List<WebElement> elementList = this.findRootWebElement().findElements(locator);
	    		for(WebElement el:elementList){
	    			if(el.isDisplayed()){
	    				return el;
	    			}
	    		}
	    	}catch(Exception e){
	    	}
		}
		throw new TimeoutException("Could not find element within timeout 30 seconds: " + locator.toString());
	}
	
	public List<WebElement> getWebElements(By locator,int...timeout){
		locator = changeToHierarchyMode(locator);
		long start = System.currentTimeMillis();
		int defaulttimeout = (timeout!=null && timeout.length==1)?timeout[0]:30;
		
		while (defaulttimeout * 1000 > System.currentTimeMillis() - start) {
			try{
				try{
					Wait<WebDriver> wait = new WebDriverWait(driver, 1).pollingEvery(100, TimeUnit.MILLISECONDS);
					wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
				}catch(Exception e){}
				return this.findRootWebElement().findElements(locator);
			}catch(Exception e){}
		}
		throw new TimeoutException("Could not find elements within timeout " + defaulttimeout + " seconds: " + locator.toString());
	}
	
	public void clickWebElement(By locator,int...timeout){
		locator = changeToHierarchyMode(locator);
		long start = System.currentTimeMillis();
		int defaultTimeout = timeout!=null && timeout.length==1?timeout[0]:30;
		while (defaultTimeout * 1000 > System.currentTimeMillis() - start) {
	    	try{
	    		List<WebElement> elementList = this.findRootWebElement().findElements(locator);
	    		for(WebElement el:elementList){
	    			if(el.isDisplayed()){
	    				el.click();
	    				return;
	    			}
	    		}
	    	}catch(Exception e){}
		}
		throw new TimeoutException("Could not find element within timeout "+defaultTimeout+" seconds: " + locator.toString());
	}
}
