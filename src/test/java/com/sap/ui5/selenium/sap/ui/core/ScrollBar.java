package com.sap.ui5.selenium.sap.ui.core;

import java.util.List;

import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;

import com.sap.ui5.selenium.core.BrowserWindow;
import com.sap.ui5.selenium.core.Frame;
import com.sap.ui5.selenium.utilities.SapUi5Utilities;

public class ScrollBar extends com.sap.ui5.selenium.core.ScrollBar {

	protected ScrollBar(String id, Frame frame) {
		super(id, frame);
	}
	
	public static ScrollBar findScrollBarByIdEndsWith(BrowserWindow bw, String id,int...timeout) {
		int defaulttimeout = (timeout!=null && timeout.length==1)?timeout[0]:30;
		long start = System.currentTimeMillis();
		while (defaulttimeout * 1000 > System.currentTimeMillis() - start) {	
	    	try{
	    		List<WebElement> elementList = bw.syncAjax().driver().findElements(SapUi5Utilities.getEndsWithLocator(id));
	    		for(WebElement el:elementList){
	    			if(el.isDisplayed()){
	    				return new ScrollBar(el.getAttribute("id"),bw.getMainFrame());
	    			}
	    		}
	    	}catch(Exception e){
	    	}
		} 		
		throw new TimeoutException("Could not find element within timeout " + defaulttimeout + " seconds: " + SapUi5Utilities.getEndsWithLocator(id).toString());
	}
	
	/** 
	 * @param pageSize
	 * 				the count of table row
	 */
	public int scrollToNextPage(int pageSize) {
		int allSteps = this.getSteps();
		int currentStep = this.getScrollPosition();
		int nextStep;
		if(currentStep<allSteps) {
			nextStep = currentStep + pageSize;
			nextStep = nextStep>allSteps ? allSteps : nextStep;
			this.setScrollPosition(nextStep);
			return nextStep - currentStep;
		}
		return -1;
	}
	
	public int getCurrentPage() {
		if(this.getSteps() == 0) {
			return 0;
		}
		return this.getScrollPosition()/this.getSteps();
	}
	
	/** 
	 * @param position
	 * 				the position of scrollbar, equals the index of table row
	 */
	public void setScrollPosition(int position) {
		this.js.exec(".setScrollPosition(arguments[0])", position);
	}
}