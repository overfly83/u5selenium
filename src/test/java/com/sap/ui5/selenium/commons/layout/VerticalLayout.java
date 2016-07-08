package com.sap.ui5.selenium.commons.layout;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;

import com.sap.ui5.selenium.core.BrowserWindow;
import com.sap.ui5.selenium.core.Frame;
import com.sap.ui5.selenium.sap.fpa.ui.common.CompoundControl;

public class VerticalLayout extends CompoundControl {

	protected VerticalLayout(String id, Frame frame) {
		super(id, frame);
	}

	public static VerticalLayout findVerticalLayoutByClass(BrowserWindow bw, String classname,int...timeout) {
		int defaulttimeout = (timeout!=null && timeout.length==1)?timeout[0]:30;
		long start = System.currentTimeMillis();
		while (defaulttimeout * 1000 > System.currentTimeMillis() - start) {	
	    	try{
	    		List<WebElement> elementList = bw.syncAjax().driver().findElements(By.className(classname));
	    		for(WebElement el:elementList){
	    			if(el.isDisplayed()){
	    				return new VerticalLayout(el.getAttribute("id"),bw.getMainFrame());
	    			}
	    		}
	    	}catch(Exception e){
	    	}
		} 		
		throw new TimeoutException("Could not find element within timeout " + defaulttimeout + " seconds: with class name" + classname);
	}
	
}
