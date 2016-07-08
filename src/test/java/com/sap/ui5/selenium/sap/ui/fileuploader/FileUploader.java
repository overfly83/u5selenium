package com.sap.ui5.selenium.sap.ui.fileuploader;

import java.util.List;

import org.junit.Assert;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;

import com.sap.ui5.selenium.core.BrowserWindow;
import com.sap.ui5.selenium.core.Control;
import com.sap.ui5.selenium.core.Frame;
import com.sap.ui5.selenium.core.UI5By;
import com.sap.ui5.selenium.utilities.SapUi5Utilities;

public class FileUploader extends Control{
	protected FileUploader(String id, Frame frame) {
		super(id, frame);
	}
	
	

	
	public static FileUploader findFileUploaderByIdEndsWith(BrowserWindow bw, String id,int...timeout) {
		int defaulttimeout = (timeout!=null && timeout.length==1)?timeout[0]:30;
		long start = System.currentTimeMillis();
		while (defaulttimeout * 1000 > System.currentTimeMillis() - start) {	
	    	try{
	    		List<WebElement> elementList = bw.syncAjax().driver().findElements(SapUi5Utilities.getEndsWithLocator(id));
	    		for(WebElement el:elementList){
	    			if(el.isDisplayed()){
	    				return new FileUploader(el.getAttribute("id"),bw.getMainFrame());
	    			}
	    		}
	    	}catch(Exception e){
	    	}
		} 		
		throw new TimeoutException("Could not find element within timeout " + defaulttimeout + " seconds: " + SapUi5Utilities.getEndsWithLocator(id).toString());
	}
	
	
	public void upload(String path){
		this.findWebElement(UI5By.id(this.getId()+"-fu")).sendKeys(path);
	}
	public boolean isUploadOnChange() {
		return this.js.exec(".getUploadOnChange()").get(Boolean.class);
	}
	
	public boolean checkEnabled() {
		return this.js.exec(".getEnabled()").get(Boolean.class);
	}
	
	public void assertEnabledStatus(boolean expectStatus) {
		Assert.assertEquals("assert enabled status of fileUploader '"+ this.getId() +"' to be ", expectStatus, this.checkEnabled());
	}
	
}



