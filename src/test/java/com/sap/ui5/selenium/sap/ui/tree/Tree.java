package com.sap.ui5.selenium.sap.ui.tree;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import com.sap.ui5.selenium.core.BrowserWindow;
import com.sap.ui5.selenium.core.Frame;
import com.sap.ui5.selenium.sap.fpa.ui.common.CompoundControl;
import com.sap.ui5.selenium.utilities.SapUi5Utilities;

public class Tree extends CompoundControl{
	protected Tree(String id, Frame frame) {
		super(id, frame);
	}

	
	public static Tree findTreeByIdEndsWith(BrowserWindow bw, String id,int...timeout) {
		int defaulttimeout = (timeout!=null && timeout.length==1)?timeout[0]:30;
		long start = System.currentTimeMillis();
		while (defaulttimeout * 1000 > System.currentTimeMillis() - start) {	
	    	try{
	    		List<WebElement> elementList = bw.syncAjax().driver().findElements(SapUi5Utilities.getEndsWithLocator(id));
	    		for(WebElement el:elementList){
	    			if(el.isDisplayed()){
	    				return new Tree(el.getAttribute("id"),bw.getMainFrame());
	    			}
	    		}
	    	}catch(Exception e){
	    	}
		} 		
		throw new TimeoutException("Could not find element within timeout " + defaulttimeout + " seconds: " + SapUi5Utilities.getEndsWithLocator(id).toString());
	}
	
	private Tree getThisTree(){
		return this;
	}
	/**
	 * 	goto the leaf node and single click, path split by /
	 * @param bw
	 * @param nodepath
	 * @throws InterruptedException
	 */
	public void gotoNode(String nodepath){
		String[] fullpath = nodepath.split("/");
		boolean nodefound = false;
		WebElement treeCont = getThisTree().findRootWebElement();
		List<WebElement> liList;
		try{
			driver.manage().timeouts().implicitlyWait(1,TimeUnit.SECONDS);
			liList =  treeCont.findElements(By.xpath(".//ul/li[@aria-level='1']"));
		}catch(Exception e){
			driver.manage().timeouts().implicitlyWait(10,TimeUnit.SECONDS);
			return;
		}
		driver.manage().timeouts().implicitlyWait(10,TimeUnit.SECONDS);
		for(int i=0;i<fullpath.length;i++){
			for(WebElement li:liList){	
				if(fullpath[i].equalsIgnoreCase(li.getText())){
					
					if(i==fullpath.length-1){
						if(!li.isSelected()){
							li.click();
						}
						SapUi5Utilities.waitWhenPageBusy(driver);
						nodefound = true;
						break;
					}else if(i<fullpath.length-1){
						if(li.getAttribute("aria-expanded")!=null){
							if(li.getAttribute("aria-expanded").equalsIgnoreCase("false")){
								li.sendKeys(Keys.RIGHT);
								SapUi5Utilities.waitWhenPageBusy(driver);
							}
						}else{
							li.sendKeys(Keys.RIGHT);
							SapUi5Utilities.waitWhenPageBusy(driver);
						}
						int nodelevel = i+2;
						treeCont = getThisTree().findRootWebElement();
						liList = treeCont.findElements(By.xpath(".//ul/li[@aria-level='"+ nodelevel +"']"));
						break;
					}
				}
			}
			if(i==fullpath.length-1 && nodefound){
				Assert.assertEquals("Selecting node: "+fullpath[i], true,nodefound);
				break;
			}else{
				if(i==fullpath.length-1){
					Assert.assertEquals("Selecting node: "+fullpath[i], true,nodefound);
				}
			}
		}
	}
	
	/**
	 * goto the leaf node and double click, path split by /
	 * @param sapUiTreeContId
	 * @param nodepath
	 * @throws InterruptedException
	 */
	public void doubleClickTreeNode(String nodepath){
		String[] fullpath = nodepath.split("/");
		boolean nodefound = false;
		WebElement treeCont = this.getThisTree().findRootWebElement();
		Actions action = new Actions(driver);
		List<WebElement> liList;
		try{
			driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
			liList =  treeCont.findElements(By.xpath(".//ul/li[@aria-level='1']"));
		}catch(Exception e){
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			return;
		}
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		for(int i=0;i<fullpath.length;i++){
			
			for(WebElement li:liList){
				
				if(fullpath[i].equalsIgnoreCase(li.getText())){
					try{
						if(li.getAttribute("aria-expanded").equalsIgnoreCase("false")){
							li.sendKeys(Keys.RIGHT);
							SapUi5Utilities.waitWhenPageBusy(driver);
						}	
					
						
					}catch(Exception e){
						if(i!=fullpath.length-1){
							li.click();
							li.sendKeys(Keys.RIGHT);
							SapUi5Utilities.waitWhenPageBusy(driver);
						}
					}
					
					if(i==fullpath.length-1){
						action.doubleClick(li).perform();
						nodefound = true;
						break;
					}else if(i<fullpath.length-1){
						int nodelevel = i+2;
						treeCont = this.getThisTree().findRootWebElement();
						liList = treeCont.findElements(By.xpath(".//ul/li[@aria-level='"+ nodelevel +"']"));
						break;
					}
				}
			}
			if(i==fullpath.length-1 && nodefound){
				Assert.assertEquals("Selecting node: "+fullpath[i], true,nodefound);
				break;
			}else{
				if(i==fullpath.length-1){
					Assert.assertEquals("Selecting node: "+fullpath[i], true,nodefound);
				}
			}
		}
	}
	
}



