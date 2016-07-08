package com.sap.ui5.selenium.sap.fpa.ui.control.memberselector;

import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.sap.ui5.selenium.commons.CheckBox;
import com.sap.ui5.selenium.commons.TreeNode;
import com.sap.ui5.selenium.commons.TriStateCheckBox;
import com.sap.ui5.selenium.commons.TriStateCheckBoxState;
import com.sap.ui5.selenium.core.Frame;
import com.sap.ui5.selenium.core.UI5By;
import com.sap.ui5.selenium.utilities.SapUi5Utilities;
/**
 * 
 * @author I058399
 *
 */
public class CustomTreeNode extends TreeNode{


	protected CustomTreeNode(String id, Frame frame) {
		super(id, frame);
	}
	
	public String getDisplayText(){
		return this.findRootWebElement().getText();
	}
	
	private void click(){
		waitUntilEnabled();
		this.findRootWebElement().click();
	}
	
	public void select(){
		if(this.hasCheckBox()){   //for user selector
			this.tickCheckBox();
		}else{  //for role selector
			this.click();
		}
	}
	
	
	public void unSelect(){
		if(this.hasCheckBox()){   //for user selector
			this.unTickCheckBox();
		}else{
			this.click();
		}
	}
	
	
	private boolean hasCheckBox(){
		WebElement cb = null;
		try{
			driver.manage().timeouts().implicitlyWait((long) 0.5, TimeUnit.SECONDS);
			cb = SapUi5Utilities.getCheckBoxfrom(driver,driver.findElement(By.id(this.getId())));
		}catch(Exception e){
		}
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		return cb!=null;
	}

	/**
	 * Check whether CheckBox is selectable.
	 */
	public boolean checkCheckBoxSelectable(){
		if (this.hasCheckBox()) return isSelectable();
		else return false;
	}
	
	/**
	 * Assert whether CheckBox is selectable
	 */
	public void assertCheckBoxSelectable(Boolean expected){
		if (this.hasCheckBox()){
			Boolean actual = isSelectable();
			Assert.assertEquals("assert the status of CheckBox: "+ getId() +" is selectable ", expected, actual);
		}
	}
		
	protected boolean hasImg()
	{	
		WebElement img = null;
		driver.manage().timeouts().implicitlyWait((long) 0.5, TimeUnit.SECONDS);
		try{
			img = driver.findElement(By.id(this.getId())).findElement(By.className("sapMImg"));
		}catch(Exception e){
		}
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		return img!=null;
	}
	
	public void tickCheckBox(){
		WebElement cb = SapUi5Utilities.getCheckBoxfrom(driver, driver.findElement(By.id(this.getId())));
		
		if(this.find(UI5By.id(cb.getAttribute("id"))).getClass().equals(com.sap.ui5.selenium.commons.TriStateCheckBox.class)){
			TriStateCheckBox chb = this.find(UI5By.id(cb.getAttribute("id")));
			if(chb.getSelectionState().equals(TriStateCheckBoxState.Unchecked)){
				cb.click();
			}
		}else if(this.find(UI5By.id(cb.getAttribute("id"))).getClass().equals(com.sap.ui5.selenium.commons.CheckBox.class)){
			CheckBox chb = this.find(UI5By.id(cb.getAttribute("id")));
			if(!chb.isChecked()) { 
				cb.findElement(By.tagName("label")).click();
			}
		}else if(this.find(UI5By.id(cb.getAttribute("id"))).getClass().equals(com.sap.ui5.selenium.sap.m.CheckBox.class)){
			com.sap.ui5.selenium.sap.m.CheckBox chb = this.find(UI5By.id(cb.getAttribute("id")));
			if(!chb.isChecked()){
				cb.findElement(By.tagName("label")).click();
			}
		}else if(this.find(UI5By.id(cb.getAttribute("id"))).getClass().equals(com.sap.ui5.selenium.sap.fpa.ui.control.commons.CheckBox.class)){
			com.sap.ui5.selenium.sap.fpa.ui.control.commons.CheckBox chb = this.find(UI5By.id(cb.getAttribute("id")));
			if(!chb.isChecked()){
				chb.check();
			}
		}else{
			System.out.println(this.find(UI5By.id(cb.getAttribute("id"))).getClass().toString());
		}
		
	}
	
	public void unTickCheckBox(){
		WebElement cb = SapUi5Utilities.getCheckBoxfrom(driver,driver.findElement(By.id(this.getId())));
		
		if(this.find(UI5By.id(cb.getAttribute("id"))).getClass().equals(com.sap.ui5.selenium.commons.TriStateCheckBox.class)){
			TriStateCheckBox chb = this.find(UI5By.id(cb.getAttribute("id")));
			if(chb.getSelectionState().equals(TriStateCheckBoxState.Checked)){
				cb.click();
			}
		}else if(this.find(UI5By.id(cb.getAttribute("id"))).getClass().equals(com.sap.ui5.selenium.commons.CheckBox.class)){
			CheckBox chb = this.find(UI5By.id(cb.getAttribute("id")));
			if(chb.isChecked()) { 
				cb.findElement(By.tagName("label")).click();
			}
		}else if(this.find(UI5By.id(cb.getAttribute("id"))).getClass().equals(com.sap.ui5.selenium.sap.m.CheckBox.class)){
			com.sap.ui5.selenium.sap.m.CheckBox chb = this.find(UI5By.id(cb.getAttribute("id")));
			if(chb.isChecked()){
				cb.findElement(By.tagName("label")).click();
			}
		}else if(this.find(UI5By.id(cb.getAttribute("id"))).getClass().equals(com.sap.ui5.selenium.sap.fpa.ui.control.commons.CheckBox.class)){
			com.sap.ui5.selenium.sap.fpa.ui.control.commons.CheckBox chb = this.find(UI5By.id(cb.getAttribute("id")));
			if(chb.isChecked()){
				chb.uncheck();
			}
		}else{
			System.out.println(this.find(UI5By.id(cb.getAttribute("id"))).getClass().toString()+" is not checkable.");
		}
	}
	
	protected void waitUntilEnabled() {
		new WebDriverWait(driver, 20).until(new ExpectedCondition<Boolean>(){
			public Boolean apply(WebDriver webdriver) {
				try{
					return isSelectable();
				}catch(Exception e){
					return false;
				}
				
			}
		});
	}
}